package org.example;
import java.util.List;

public interface Tokenizer {
    /**
     * Splits the given text into a list of tokens (e.g., words or phrases).
     * This method defines a contract for how text should be divided into smaller parts
     * (tokens) for further processing, such as indexing or searching.
     *
     * @param text The input text to be tokenized.
     * @return A list of strings where each string is an individual token.
     */
    List<String> tokenize(String text);
}
