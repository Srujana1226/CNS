import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class MonoalphabeticCipher {

    // Function to create the substitution map
    public static Map<Character, Character> createSubstitutionMap(String key) {
        Map<Character, Character> substitutionMap = new HashMap<>();
        char currentChar = 'a';

        for (char c : key.toCharArray()) {
            substitutionMap.put(currentChar, c);
            currentChar++;
        }

        return substitutionMap;
    }

    // Function to create the reverse substitution map for decryption
    public static Map<Character, Character> createReverseSubstitutionMap(Map<Character, Character> substitutionMap) {
        Map<Character, Character> reverseMap = new HashMap<>();
        for (Map.Entry<Character, Character> entry : substitutionMap.entrySet()) {
            reverseMap.put(entry.getValue(), entry.getKey());
        }
        return reverseMap;
    }

    // Function to encrypt the plaintext
    public static String encrypt(String plaintext, Map<Character, Character> substitutionMap) {
        StringBuilder encryptedText = new StringBuilder();

        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char encryptedChar = substitutionMap.get(Character.toLowerCase(c));
                encryptedText.append((char) (encryptedChar - 'a' + base));
            } else {
                encryptedText.append(c); // Keep non-alphabet characters as is
            }
        }
        return encryptedText.toString();
    }

    // Function to decrypt the ciphertext
    public static String decrypt(String ciphertext, Map<Character, Character> reverseMap) {
        StringBuilder decryptedText = new StringBuilder();

        for (char c : ciphertext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char decryptedChar = reverseMap.get(Character.toLowerCase(c));
                decryptedText.append((char) (decryptedChar - 'a' + base));
            } else {
                decryptedText.append(c); // Keep non-alphabet characters as is
            }
        }
        return decryptedText.toString();
    }

    // Function to validate the key
    public static boolean isValidKey(String key) {
        if (key.length() != 26) {
            return false;
        }
        Set<Character> uniqueChars = new HashSet<>();
        for (char c : key.toCharArray()) {
            if (!Character.isLowerCase(c) || !uniqueChars.add(c)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the plaintext: ");
        String plaintext = scanner.nextLine();

        System.out.print("Enter the key (26 unique letters): ");
        String key = scanner.nextLine().toLowerCase();

        if (!isValidKey(key)) {
            System.out.println("Invalid key! Please ensure it contains exactly 26 unique lowercase letters.");
        }

        Map<Character, Character> substitutionMap = createSubstitutionMap(key);
        Map<Character, Character> reverseMap = createReverseSubstitutionMap(substitutionMap);

        String encryptedText = encrypt(plaintext, substitutionMap);
        System.out.println("Encrypted Text: " + encryptedText);

        String decryptedText = decrypt(encryptedText, reverseMap);
        System.out.println("Decrypted Text: " + decryptedText);

        scanner.close();
    }
}