package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Indexer - Class responsible for indexing text files and performing searches based on trigrams.
 */
public class Indexer {
    private final Map<String, Map<Trigram, Set<Integer>>> index = new HashMap<>();
    private final Tokenizer tokenizer;

    public Indexer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    /**
     * Indexes a text file at the specified path.
     * @param filePath the path to the file to be indexed.
     */
    public void indexFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            // Read file content
            String content = Files.readString(path);
            // Tokenize the content into a list of words
            List<String> tokens = tokenizer.tokenize(content);

            // Build the trigram index for the file
            Map<Trigram, Set<Integer>> fileMap = buildTrigramIndex(tokens);
            // Add the file's trigram index to the main index
            index.put(filePath, fileMap);

        } catch (IOException e) {
            // Log the error to System.err without stopping the program
            System.err.println("Error reading file " + path + ": " + e.getMessage());
            // Continue execution, application does not stop here
        }
    }

    /**
     * Indexes all files in a directory and its subdirectories.
     * @param directory the directory to index.
     */
    public void indexDirectory(File directory) {
        if (!directory.isDirectory()) {
            // Print an error message and continue if it's not a directory
            System.err.println("Provided path is not a directory: " + directory.getAbsolutePath());
            return;
        }

        // Loop through the files in the directory
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                // Recursively index subdirectories
                indexDirectory(file);
            } else if (file.isFile()) {
                // Index individual files
                indexFile(file.getAbsolutePath());
            }
        }
    }

    /**
     * Searches for the given word in the index and returns a set of files containing the word.
     * @param word the word to search for in the index.
     * @return a set of files that contain the word.
     */
    public Set<String> search(String word) {
        Set<String> result = new HashSet<>();
        // Tokenize the search term
        List<String> tokens = tokenizer.tokenize(word);

        if (tokens == null || tokens.isEmpty()) {
            // If no tokens, return all indexed files
            return new HashSet<>(index.keySet());
        }

        // Loop through all files in the index
        for (Map.Entry<String, Map<Trigram, Set<Integer>>> entry : index.entrySet()) {
            String fileName = entry.getKey();
            Map<Trigram, Set<Integer>> fileMap = entry.getValue();

            // Check if the file contains the token sequence
            if (containsSequence(fileMap, tokens)) {
                result.add(fileName);
            }
        }
        return result;
    }

    /**
     * Builds a trigram index for the given list of tokens.
     * @param tokens the list of tokens (words).
     * @return a map of trigrams and their positions in the text.
     */
    private Map<Trigram, Set<Integer>> buildTrigramIndex(List<String> tokens) {
        Map<Trigram, Set<Integer>> fileMap = new HashMap<>();
        // Iterate through all possible trigrams in the token list
        for (int i = 0; i <= tokens.size() - 3; i++) {
            Trigram trigram = new Trigram(tokens.get(i), tokens.get(i + 1), tokens.get(i + 2));
            fileMap.computeIfAbsent(trigram, k -> new TreeSet<>()).add(i);
        }
        return fileMap;
    }

    /**
     * Checks if the file contains the given sequence of tokens.
     * @param fileMap the trigram index of the file.
     * @param tokens the list of tokens to search for.
     * @return true if the sequence is found in the file, otherwise false.
     */
    private boolean containsSequence(Map<Trigram, Set<Integer>> fileMap, List<String> tokens) {
        // If the tokens list has 2 or fewer tokens, check by trigram
        if (tokens.size() <= 2) {
            return fileMap.keySet().stream().anyMatch(trigram -> trigram.containSequence(tokens));
        }

        List<Set<Integer>> trigramPositions = new ArrayList<>();
        // Build a list of trigram positions
        for (int i = 0; i <= tokens.size() - 3; i++) {
            Trigram trigram = new Trigram(tokens.get(i), tokens.get(i + 1), tokens.get(i + 2));
            trigramPositions.add(fileMap.getOrDefault(trigram, Collections.emptySet()));
        }

        // If no trigram positions exist, return false
        if (trigramPositions.isEmpty() || trigramPositions.get(0).isEmpty()) return false;

        // Check if the trigram positions are consecutive
        for (int pos : trigramPositions.get(0)) {
            if (isConsecutive(pos, trigramPositions)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the trigram positions are consecutive in the text.
     * @param startPos the starting position
     * @param positions the list of trigram positions
     * @return true if the positions are consecutive, otherwise false.
     */
    private boolean isConsecutive(int startPos, List<Set<Integer>> positions) {
        for (int j = 1; j < positions.size(); j++) {
            if (!positions.get(j).contains(startPos + j)) {
                return false;
            }
        }
        return true;
    }
}
