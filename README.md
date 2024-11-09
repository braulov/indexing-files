# Text File Indexer
This project is a **text indexing and search application** that allows users to index text files or directories and perform word or phrase searches on them. The application reads files, tokenizes the content, and organizes data into an efficient searchable structure using **trigram indexing**.

Additionally, the project supports easy extension for adding **custom tokenization methods**. The project also provides a straightforward way to **add regular expression-based search functionality**, enabling more advanced and flexible search queries if needed.

## Compile and run the project

```bash
./gradlew build
./gradlew run
```

### Available Commands:
- **`index [path]`**: Index a file or directory located at the specified path.
- **`find [word]`**: Search for the given word or phrase.
- **`exit`**: Exit the program.

## Why Trigram Indexing Was Chosen

I was choosing between **suffix structures** (suffix array, suffix automaton, suffix tree) and **trigrams**. The main two reasons I chose the latter are:

### Suffix-based indexing is harder to maintain: 
For example, if queries need to account for changes in files after indexing, the entire structure would need to be rebuilt. With trigrams, you can simply add a lazy update (when a line is removed from a file, all subsequent lines shift one position to the right).

And a trigram-based solution is much simpler than one based on suffix structures.
### Performance
Using trigrams, searching for a single word in a single file works in **O(1)** time, which is faster than suffix structures (such as suffix arrays or suffix automata), where search time its **O(p\)**, where p - size of word.

Searching for phrases (e.g., a sequence of words) in my index works in **O(p * log p + (t/p) * p * log t) = O(t * log t * log p)** (sort all trigram sets in phraze by size and check that needed positions contains in each set), where:
- **t** is the total number of trigrams in the file,
- **p** is the number of trigrams matching the search phrase,
- **p log p** is the time to sort all trigram sets in phrase by size.
- **t/p** is the upper bound of smallest trigram set.
- **log t** is the time to search in a sorted sets positions of trigram.


If hash sets are used to store the positions of trigrams, the time complexity can be reduced to **O(t log p)**. However, this can negatively affect scalability (for example, handling changes in files after indexing using hash sets will be slow).

Overall, this approach is acceptable for relatively small files, as search time for a phrase remains within reasonable limits for files up to a few megabytes in size.

## Example Usage
### Files in `src/main/resources`:
#### file1.txt
```bash
ma mama,; pa, pama
```
#### file2.txt
```bash
mas ma mamasama pa papa
ma pa ma
```
1. **Indexing a directory**:
 ```sh
 Enter command: index src/main/resources
 Indexed directory: src/main/resources
```
2. **Searching for the word 'ma':**
 ```sh
Enter command: find ma
Files containing the word 'ma':
/home/ogesse/goland/src/main/resources/file2.txt
/home/ogesse/goland/src/main/resources/file1.txt
```
3. **Searching for the word 'pa':**
 ```sh
Enter command: find pa
Files containing the word 'pa':
/home/ogesse/goland/src/main/resources/file2.txt
/home/ogesse/goland/src/main/resources/file1.txt
```
4. **Searching for the word 'mama':**
 ```sh
Enter command: find mama
Files containing the word 'mama':
/home/ogesse/goland/src/main/resources/file2.txt
```
5. **Searching for the word 'papa':**
 ```sh
Enter command: find papa
Files containing the word 'papa':
/home/ogesse/goland/src/main/resources/file1.txt
```
6. **Searching for the word 'ma pa ma':**
 ```sh
Enter command: find ma pa ma
Files containing the word 'ma pa ma':
/home/ogesse/goland/src/main/resources/file2.txt
```
7. **Searching for the word 'mama pa':**
```sh
Enter command: find mama pa
Files containing the word 'mama pa':
/home/ogesse/goland/src/main/resources/file1.txt
```
8. **Searching for the word 'pa pama':**
```sh
Enter command: find pa pama
Files containing the word 'pa pama':
/home/ogesse/goland/src/main/resources/file1.txt
```
9. **Searching for the word 'pa pama':**
```sh
Enter command: find pa pama
Files containing the word 'pa pama':
/home/ogesse/goland/src/main/resources/file1.txt
```
10. **Searching for the word 'text':**
```sh
Enter command: find text
No files contain the word: text
```
11. **Exiting**
```sh
Enter command: exit
Exiting...
```
