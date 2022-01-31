package com.wordlecheat.dictionary.object;

/**
 * English Language Letters and how ofen they are used in words
 * @param frequency
 */
public enum LetterEnum {

    E(11.1607),	M(3.0129),
    A(8.4966), H(3.0034),
    R(7.5809), G(2.4705),
    I(7.5448), B(2.0720),
    O(7.1635), F(1.8121),
    T(6.9509), Y(1.7779),
    N(6.6544), W(1.2899),
    S(5.7351), K(1.1016),
    L(5.4893), V(1.0074),
    C(4.5388), X(0.2902),
    U(3.6308), Z(0.2722),
    D(3.3844), J(0.1965),
    P(3.1671), Q(0.1962);	

    private final double frequency;

    LetterEnum(double frequency) {
        this.frequency = frequency;
    }

    public double getFrequency() {
        return frequency;
    }
}
