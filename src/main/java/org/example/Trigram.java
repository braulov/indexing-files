package org.example;

import java.util.List;
import java.util.Objects;

/**
 * Trigram class represents a sequence of three tokens (words).
 * Used for indexing and searching in the context of text processing.
 */
class Trigram {
    private final List<String> tokensInGram;

    /**
     * Constructor for creating a Trigram from three tokens.
     * @param t1 the first token
     * @param t2 the second token
     * @param t3 the third token
     */
    public Trigram(String t1, String t2, String t3) {
        // Initialize tokens list with provided tokens
        this.tokensInGram = List.of(t1, t2, t3);
    }

    /**
     * Checks if the provided object is equal to this Trigram.
     * Two Trigrams are equal if their tokens are the same.
     * @param o the object to compare
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trigram trigram = (Trigram) o;
        return Objects.equals(tokensInGram, trigram.tokensInGram);
    }

    /**
     * Calculates the hash code for this Trigram based on its tokens.
     * @return hash code as an integer
     */
    @Override
    public int hashCode() {
        return Objects.hash(tokensInGram);
    }

    /**
     * Returns a string representation of the Trigram.
     * @return a string with tokens in the format (token1, token2, token3)
     */
    @Override
    public String toString() {
        return "(" + tokensInGram.get(0) + ", " + tokensInGram.get(1) + ", " + tokensInGram.get(2) + ")";
    }

    /**
     * Checks if this trigram contains the provided sequence of tokens.
     * @param tokens a list of tokens to check, with length up to 3
     * @return true if the tokens sequence is contained within this trigram
     */
    public boolean containSequence(List<String> tokens) {
        // Check if the size of tokens exceeds the trigram length
        if (tokens.size() > 3) return false;

        // Look for the sequence within the trigram
        for (int i = 0; i <= tokensInGram.size() - tokens.size(); i++) {
            if (tokensInGram.subList(i, i + tokens.size()).equals(tokens)) {
                return true;
            }
        }
        return false;
    }
}
