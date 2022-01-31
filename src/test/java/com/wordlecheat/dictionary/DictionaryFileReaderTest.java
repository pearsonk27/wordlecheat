package com.wordlecheat.dictionary;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class DictionaryFileReaderTest {

    private DictionaryFileReader dictionaryFileUtilities = new DictionaryFileReader();
    private List<String> dictionaryEntries = new ArrayList<>();
    
    @Test
    void testGetDictionaryEntries() throws Exception {
        dictionaryEntries.add("A () The first letter of the English and of many other alphabets. The capital A of the alphabets of Middle and Western Europe, as also the small letter (a), besides the forms in Italic, black letter, etc., are all descended from the old Latin A, which was borrowed from the Greek Alpha, of the same form; and this was made from the first letter (/) of the Phoenician alphabet, the equivalent of the Hebrew Aleph, and itself from the Egyptian origin. The Aleph was a consonant letter, with a guttural breath sound that was not an element of Greek articulation; and the Greeks took it to represent their vowel Alpha with the a sound, the Phoenician alphabet having no vowel symbols.");
        dictionaryEntries.add("A () The name of the sixth tone in the model major scale (that in C), or the first tone of the minor scale, which is named after it the scale in A minor. The second string of the violin is tuned to the A in the treble staff. -- A sharp (A/) is the name of a musical tone intermediate between A and B. -- A flat (A/) is the name of a tone intermediate between A and G.");
        dictionaryEntries.add("A () An adjective, commonly called the indefinite article, and signifying one or any, but less emphatically.");
        dictionaryEntries.add("A () In each; to or for each; as, \"\"twenty leagues a day\"\", \"\"a hundred pounds a year\"\", \"\"a dollar a yard\"\", etc.");
        dictionaryEntries.add("A (prep.) In; on; at; by.");
        dictionaryEntries.add("A (prep.) In process of; in the act of; into; to; -- used with verbal substantives in -ing which begin with a consonant. This is a shortened form of the preposition an (which was used before the vowel sound); as in a hunting, a building, a begging.");
        dictionaryEntries.add("A () Of.");
        dictionaryEntries.add("A () A barbarous corruption of have, of he, and sometimes of it and of they.");
        dictionaryEntries.add("A () An expletive, void of sense, to fill up the meter");
        dictionaryEntries.add("A- () A, as a prefix to English words, is derived from various sources. (1) It frequently signifies on or in (from an, a forms of AS. on), denoting a state, as in afoot, on foot, abed, amiss, asleep, aground, aloft, away (AS. onweg), and analogically, ablaze, atremble, etc. (2) AS. of off, from, as in adown (AS. ofd/ne off the dun or hill). (3) AS. a- (Goth. us-, ur-, Ger. er-), usually giving an intensive force, and sometimes the sense of away, on, back, as in arise, abide, ago. (4) Old English y- or i- (corrupted from the AS. inseparable particle ge-, cognate with OHG. ga-, gi-, Goth. ga-), which, as a prefix, made no essential addition to the meaning, as in aware. (5) French a (L. ad to), as in abase, achieve. (6) L. a, ab, abs, from, as in avert. (7) Greek insep. prefix / without, or privative, not, as in abyss, atheist; akin to E. un-.");
        dictionaryEntries.add("A 1 () A registry mark given by underwriters (as at Lloyd's) to ships in first-class condition. Inferior grades are indicated by A 2 and A 3.");
        dictionaryEntries.add("Aam (n.) A Dutch and German measure of liquids, varying in different cities, being at Amsterdam about 41 wine gallons, at Antwerp 36 1/2, at Hamburg 38 1/4.");
        dictionaryEntries.add("Aard-vark (n.) An edentate mammal, of the genus Orycteropus, somewhat resembling a pig, common in some parts of Southern Africa. It burrows in the ground, and feeds entirely on ants, which it catches with its long, slimy tongue.");
        dictionaryEntries.add("Aard-wolf (n.) A carnivorous quadruped (Proteles Lalandii), of South Africa, resembling the fox and hyena. See Proteles.");
        dictionaryEntries.add("Aaronic (a.) Alt. of Aaronical");
        dictionaryEntries.add("Aaronical (a.) Pertaining to Aaron, the first high priest of the Jews.");
        dictionaryEntries.add("Aaron's rod () A rod with one serpent twined around it, thus differing from the caduceus of Mercury, which has two.");
        dictionaryEntries.add("Aaron's rod () A plant with a tall flowering stem; esp. the great mullein, or hag-taper, and the golden-rod.");
        dictionaryEntries.add("Ab- () A prefix in many words of Latin origin. It signifies from, away , separating, or departure, as in abduct, abstract, abscond. See A-(6).");
        dictionaryEntries.add("Ab (n.) The fifth month of the Jewish year according to the ecclesiastical reckoning, the eleventh by the civil computation, coinciding nearly with August.");

        String entry;
        int i = 0;
        while ((entry = dictionaryFileUtilities.getNextDictionaryEntry()) != null && i < 20) {
            assertThat(entry).isEqualTo(dictionaryEntries.get(i));
            i++;
        }
    }
}
