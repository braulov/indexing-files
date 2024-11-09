package org.example;

import java.io.File;
import java.util.Scanner;
import java.util.Set;

/**
 * ConsoleInterface - Class that provides a console-based interface
 * for user interaction with the file indexer.
 * It handles user commands for indexing files/directories and searching the index.
 */
public class ConsoleInterface {
    private final Indexer indexer;

    // Supported commands
    private static final String COMMAND_INDEX = "index";
    private static final String COMMAND_FIND = "find";
    private static final String COMMAND_EXIT = "exit";

    /**
     * Constructor for ConsoleInterface.
     * @param indexer the Indexer object that manages file indexing.
     */
    public ConsoleInterface(Indexer indexer) {
        this.indexer = indexer;
    }

    /**
     * Starts the main loop of the console interface, taking user commands
     * and executing appropriate actions based on the input.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Text File Indexer");

        while (true) {
            System.out.print("Enter command: ");
            String commandLine = scanner.nextLine().trim();
            String[] parts = commandLine.split(" ", 2);
            String command = parts[0];
            String argument = parts.length > 1 ? parts[1] : "";

            // Determine and handle command type
            switch (command) {
                case COMMAND_INDEX -> handleIndexCommand(argument);
                case COMMAND_FIND -> handleFindCommand(argument);
                case COMMAND_EXIT -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Unknown command.");
            }
        }
    }

    /**
     * Handles the "index" command to index a file or directory.
     * @param path the path to the file or directory to be indexed.
     */
    private void handleIndexCommand(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            // Index all files in the directory
            indexer.indexDirectory(file);
            System.out.println("Indexed directory: " + path);
        } else if (file.isFile()) {
            // Index a single file
            indexer.indexFile(path);
            System.out.println("Indexed file: " + path);
        } else {
            System.out.println("Invalid path.");
        }
    }

    /**
     * Handles the "find" command to search for a word or phrase in the indexed files.
     * @param word the word or phrase to search for in the index.
     */
    private void handleFindCommand(String word) {
        Set<String> results = indexer.search(word);
        if (results.isEmpty()) {
            System.out.println("No files contain the word: " + word);
        } else {
            System.out.println("Files containing the word '" + word + "':");
            results.forEach(System.out::println);
        }
    }
}
