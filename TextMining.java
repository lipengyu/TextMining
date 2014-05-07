import java.util.HashMap;

/**
 * A base class for text mining.
 *
 * @author K.M.J. Jacobs, TU/e <k.m.j.jacobs@student.tue.nl>
 * @since  7-5-2014
 */
public class TextMining {
    
    // List of words fetched from the text
    String[] words;
    // Text used for text mining
    private String text = new String();
    // Similarity treshold (between 0 and 1)
    private double similarityTreshold = 0.7;
    
    /**
     * Set up the text mining instance.
     * 
     * @param text text used for text mining
     */
    public TextMining(String text) {
        this.parse(text);
    }
    
    /**
     * Parse a string.
     * 
     * @param text text to parse 
     */
    public void parse(String text) {
        // Set the text
        this.setText(text);
        // Sanatize the text
        String sanitized = text;
        sanitized = sanitizeWhitespace(sanitized);
        // Extract the words
        this.words = sanitized.split(" ");
    }
    
    /**
     * Check whether the text contains the given word.
     * 
     * @param word word to check for in the text
     * @return true if the word is found with a given similarity treshold
     */
    public boolean contains(String word) {
        String[] words = sanitizeWhitespace(word).split(" ");
        int wordCount = words.length;
        for (int i = 0; i <= this.words.length - wordCount; i++) {
            double score = 0;
            for (int j = 0; j < wordCount; j++) {
                score += compareWords(words[j], this.words[i + j]) / (double)wordCount;
            }
            
            if (score >= this.getSimilarityTreshold()) {
                return true;
            }
        }
        // Check for the word with no spaces
        word = sanitizeWhitespace(word);
        for (String sentenceWord: this.words) {
            double score = compareWords(word, sentenceWord);
            if (score >= this.getSimilarityTreshold()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check which words from a list of words are contained in the text.
     * @param words a list with words, the words are used as keys in the result
     * @return a hashmap where every (key, value) pair where key is a given word
     *      from the words list and the value equals whether that word is contained
     *      in the text
     */
    public HashMap<String, Boolean> contains(String[] words) {
        HashMap<String, Boolean> result = new HashMap<String, Boolean>();
        for (String word: words) {
            result.put(word, contains(word));
        }
        return result;
    }
    
    /**
     * Compare the character similarity of two words.
     * 
     * @param word1
     * @param word2
     * @return the score (between 0 and 1) of the word similarity
     */
    public double compareWords(String word1, String word2) {
        String w1nc = sanitizeDuplicates(sanitizeNonConsonant(word1.toLowerCase()));
        String w2nc = sanitizeDuplicates(sanitizeNonConsonant(word2.toLowerCase()));
        String w1c = sanitizeDuplicates(sanitizeConsonant(word1.toLowerCase()));
        String w2c = sanitizeDuplicates(sanitizeConsonant(word2.toLowerCase()));
        // Calculate the minimum score));
        return Math.min(stringScore(w1nc, w2nc), stringScore(w1c, w2c));
    }
    
    /**
     * Calculate the number of equal characters of two strings.
     */
    public double stringScore(String word1, String word2) {
        int counter = 0;
        int sameChars = 0;
        String longest;
        String shortest;
        if (word1.length() > word2.length()) {
            longest = word1;
            shortest = word2;
        } else {
            longest = word2;
            shortest = word1;
        }
        for (int i = 0; i < longest.length(); i++) {
            if (i < longest.length()) {
                counter++;
                if (i < shortest.length()) {
                    if (longest.charAt(i) == shortest.charAt(i)) {
                        sameChars++;
                    }
                }
            }
        }
        return (double)sameChars / (double)counter;
    }
    
    /**
     * Remove duplicate neighbouring characters from string.
     * 
     * @param text text to sanitize
     * @return sanitized text
     */
    public String sanitizeDuplicates(String text) {
        String newText = text;
        newText = newText.replaceAll("(\\w)\\1+", "$1");
        return newText;
    }
    
    /**
     * Converts to non-consonant string.
     * 
     * @param text text to sanitize
     * @return sanitized text
     */
    public String sanitizeNonConsonant(String text) {
        String newText = text;
        newText = newText.replaceAll("[^eiuoaj]+", "");
        return newText;
    }
    
    /**
     * Converts to consonant-only string.
     * 
     * @param text text to sanitize
     * @return sanitized text
     */
    public String sanitizeConsonant(String text) {
        String newText = text;
        newText = newText.replaceAll("[^qwrtpsdfghklzxcvbnm]+", "");
        return newText;
    }
    
    /**
     * Converts whitespaces to a single space.
     * 
     * @param text text to sanitize
     * @return sanitized text
     */
    public String sanitizeWhitespace(String text) {
        String newText = text;
        newText = newText.replaceAll("\\s{1,}", " ");
        return newText;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the similarityTreshold
     */
    public double getSimilarityTreshold() {
        return similarityTreshold;
    }

    /**
     * @param similarityTreshold the similarityTreshold to set
     */
    public void setSimilarityTreshold(double similarityTreshold) {
        this.similarityTreshold = similarityTreshold;
    }
    
    // An example
    public static void example() {
        String sentence = "ik reis vandaag naar den bosch en naar eindhovuuuh!";
        String[] words = new String[]{"eindhoven", "amsterdam", "den bosch", "den haag"};
        TextMining tm = new TextMining(sentence);
        System.out.println("Sentence: " + sentence);
        System.out.println(tm.contains(words));
    }
    
    // Run the example
    public static void main(String[] args) {
        TextMining.example();
    }
    
}
