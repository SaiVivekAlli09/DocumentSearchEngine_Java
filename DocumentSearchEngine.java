import java.io.*;
import java.util.*;

public class SimpleSearchEngine {
    // Inverted index: word -> set of document names
    private Map<String, Set<String>> invertedIndex = new HashMap<>();
    // Store document names for display
    private List<String> documentNames = new ArrayList<>();

    // Build the index from documents in a folder
    public void buildIndex(String folderPath) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) {
            System.out.println("No .txt files found in the folder.");
            return;
        }
        for (File file : files) {
            documentNames.add(file.getName());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                // Split line into words, remove punctuation, convert to lowercase
                for (String word : line.toLowerCase().split("\\W+")) {
                    if (!word.isEmpty()) {
                        invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(file.getName());
                    }
                }
            }
            reader.close();
        }
        System.out.println("Index built for " + documentNames.size() + " documents.");
    }

    // Search for a word and print matching documents
    public void search(String query) {
        query = query.toLowerCase();
        Set<String> results = invertedIndex.getOrDefault(query, new HashSet<>());
        if (results.isEmpty()) {
            System.out.println("No documents found for: " + query);
        } else {
            System.out.println("Documents containing '" + query + "':");
            for (String doc : results) {
                System.out.println(" - " + doc);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        SimpleSearchEngine engine = new SimpleSearchEngine();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the path to your documents folder (e.g., documents):");
        String folderPath = scanner.nextLine();
        engine.buildIndex(folderPath);

        while (true) {
            System.out.print("\nEnter a search keyword (or 'exit' to quit): ");
            String query = scanner.nextLine();
            if (query.equalsIgnoreCase("exit")) break;
            engine.search(query);
        }
        scanner.close();
        System.out.println("Search engine closed.");
    }
}
// This code implements a simple search engine that builds an inverted index from text files in a specified folder.
// It allows users to search for keywords and displays the documents containing those keywords.
