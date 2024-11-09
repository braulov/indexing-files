package org.example;

import java.util.Arrays;
import java.util.List;

public class SimpleTokenizer implements Tokenizer {
    /**
     * Tokenizes the input text by splitting it into words.
     * Uses regular expression `\\W+` to separate words, removing punctuation and whitespace.
     *
     * @param text The input text to be tokenized.
     * @return A list of word tokens from the input text.
     */
    @Override
    public List<String> tokenize(String text) {
        return Arrays.asList(text.split("\\W+"));
    }
}
