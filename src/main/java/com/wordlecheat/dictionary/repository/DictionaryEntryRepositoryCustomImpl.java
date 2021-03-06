package com.wordlecheat.dictionary.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.wordlecheat.dictionary.object.DictionaryEntry;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictionaryEntryRepositoryCustomImpl implements DictionaryEntryRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(DictionaryEntryRepositoryCustomImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder cb;
    private CriteriaQuery<DictionaryEntry> query;
    private CriteriaQuery<Long> countQuery;
    private Root<DictionaryEntry> dictionaryEntryRoot;
    private Path<String> wordPath;
    private List<Predicate> predicates;
    private Path<Double> frequencyPath;

    @Override
    public DictionaryEntry findNextGuess(String[] knownLetterPlacements, Set<String> containedLetters,
            Set<String> notContainedLetters, Set<String[]> knownNonLetterPlacements) {
        initQueryProps(false);

        buildLetterHintPredicates(knownLetterPlacements, containedLetters, notContainedLetters,
                knownNonLetterPlacements);

        query.select(dictionaryEntryRoot).where(cb.and(predicates.toArray(new Predicate[predicates.size()])))
                .orderBy(cb.desc(frequencyPath));

        TypedQuery<DictionaryEntry> selectQuery = entityManager.createQuery(query);
        selectQuery.setMaxResults(1);

        return selectQuery.getSingleResult();
    }

    private void buildLetterHintPredicates(String[] knownLetterPlacements, Set<String> containedLetters,
            Set<String> notContainedLetters, Set<String[]> knownNonLetterPlacements) {
        predicates.add(cb.equal(cb.length(wordPath), knownLetterPlacements.length));

        StringBuilder sb = new StringBuilder();
        for (String knownLetterPlacement : knownLetterPlacements) {
            if (knownLetterPlacement == null) {
                sb.append("_");
            } else {
                sb.append(knownLetterPlacement.toLowerCase());
            }
        }
        predicates.add(cb.like(cb.lower(wordPath), sb.toString()));

        for (String[] knownNonLetterPlacementSets : knownNonLetterPlacements) {
            sb = new StringBuilder();
            for (String knownNonLetterPlacement : knownNonLetterPlacementSets) {
                if (knownNonLetterPlacement == null) {
                    sb.append("_");
                } else {
                    sb.append(knownNonLetterPlacement.toLowerCase());
                }
            }
            predicates.add(cb.notLike(cb.lower(wordPath), sb.toString()));
        }

        for (String containedLetter : containedLetters) {
            predicates.add(cb.like(cb.lower(wordPath), "%" + containedLetter.toLowerCase() + "%"));
        }

        for (String notContainedLetter : notContainedLetters) {
            predicates.add(cb.notLike(cb.lower(wordPath), "%" + notContainedLetter.toLowerCase() + "%"));
        }
    }

    @Transactional
    @Override
    public List<DictionaryEntry> findAllPossibleWords(String[] knownLetterPlacements, Set<String> containedLetters,
            Set<String> notContainedLetters, Set<String[]> knownNonLetterPlacements) {
        initQueryProps(false);
        buildLetterHintPredicates(knownLetterPlacements, containedLetters, notContainedLetters,
                knownNonLetterPlacements);
        query.select(dictionaryEntryRoot).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        TypedQuery<DictionaryEntry> selectQuery = entityManager.createQuery(query);

        String hqlQueryString = selectQuery.unwrap(org.hibernate.query.Query.class).getQueryString();
        ASTQueryTranslatorFactory queryTranslatorFactory = new ASTQueryTranslatorFactory();
        SessionImplementor hibernateSession = entityManager.unwrap(SessionImplementor.class);
        QueryTranslator queryTranslator = queryTranslatorFactory.createQueryTranslator("", hqlQueryString,
                java.util.Collections.EMPTY_MAP, hibernateSession.getFactory(), null);
        queryTranslator.compile(java.util.Collections.EMPTY_MAP, false);
        String sqlQueryString = queryTranslator.getSQLString();
        log.info(sqlQueryString);

        return selectQuery.getResultList();
    }

    @Override
    public long getCountOfPossibleWords(String[] knownLetterPlacements, Set<String> containedLetters,
            Set<String> notContainedLetters, Set<String[]> knownNonLetterPlacements) {
        initQueryProps(true);
        buildLetterHintPredicates(knownLetterPlacements, containedLetters, notContainedLetters,
                knownNonLetterPlacements);
        countQuery.select(cb.count(wordPath)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private void initQueryProps(boolean isCountQuery) {
        cb = entityManager.getCriteriaBuilder();
        if (isCountQuery) {
            countQuery = cb.createQuery(Long.class);
            dictionaryEntryRoot = countQuery.from(DictionaryEntry.class);
        } else {
            query = cb.createQuery(DictionaryEntry.class);
            dictionaryEntryRoot = query.from(DictionaryEntry.class);
        }

        wordPath = dictionaryEntryRoot.get("word");
        frequencyPath = dictionaryEntryRoot.get("frequency");

        predicates = new ArrayList<>();
        predicates.add(cb.isNotNull(frequencyPath));
    }

    @Override
    public DictionaryEntry getRandomNLetterWord(int n) {
        long count = getCount(n);

        initQueryProps(false);

        predicates.add(cb.equal(cb.length(wordPath), n));
        predicates.add(cb.greaterThan(frequencyPath, 5.0));

        Random random = new Random();
        int number = random.nextInt((int) count);

        query.select(dictionaryEntryRoot).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        TypedQuery<DictionaryEntry> selectQuery = entityManager.createQuery(query);
        selectQuery.setFirstResult(number);
        selectQuery.setMaxResults(1);
        return selectQuery.getSingleResult();
    }

    private Long getCount(int n) {
        initQueryProps(true);

        predicates.add(cb.equal(cb.length(wordPath), n));
        predicates.add(cb.greaterThan(frequencyPath, 5.0));

        countQuery.select(cb.count(wordPath)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

}
