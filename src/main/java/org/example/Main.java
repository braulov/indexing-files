package org.example;

public class Main {
    public static void main(String[] args) {
        // Initialize the tokenizer to split text into tokens (words).
        Tokenizer tokenizer = new SimpleTokenizer();

        // Initialize the indexer with the tokenizer to build searchable indexes.
        Indexer indexer = new Indexer(tokenizer);

        // Set up the console interface with the indexer for user interaction.
        ConsoleInterface consoleInterface = new ConsoleInterface(indexer);

        // Start the console interface to listen for user commands.
        consoleInterface.start();
    }
}
