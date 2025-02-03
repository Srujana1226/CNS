import java.util.Scanner;

public class AdvancedColumnarTransposition {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter plaintext: ");
        String plaintext = scanner.nextLine().replaceAll("\\s+", "").toUpperCase();

        System.out.print("Enter the first keyword: ");
        String key1 = scanner.nextLine().toUpperCase();

        System.out.print("Enter the second keyword: ");
        String key2 = scanner.nextLine().toUpperCase();

        String intermediateCiphertext = columnarTransposition(plaintext, key1);
        System.out.println("Ciphertext after applying first key: " + intermediateCiphertext);

        String finalCiphertext = columnarTransposition(intermediateCiphertext, key2);
        System.out.println("Final ciphertext after applying second key: " + finalCiphertext);

        String decryptedIntermediateText = decryptColumnarTransposition(finalCiphertext, key2);
        String decryptedPlaintext = decryptColumnarTransposition(decryptedIntermediateText, key1);

        System.out.println("Decrypted plaintext: " + decryptedPlaintext);

        scanner.close();
    }

    public static String columnarTransposition(String text, String key) {
        int keyLength = key.length();
        int numRows = (int) Math.ceil((double) text.length() / keyLength);

        char[][] grid = new char[numRows][keyLength];
        for (int i = 0; i < text.length(); i++) {
            grid[i / keyLength][i % keyLength] = text.charAt(i);
        }

        for (int i = text.length(); i < numRows * keyLength; i++) {
            grid[i / keyLength][i % keyLength] = 'X';
        }

        int[] order = getColumnOrder(key);

        StringBuilder ciphertext = new StringBuilder();
        for (int col : order) {
            for (int row = 0; row < numRows; row++) {
                ciphertext.append(grid[row][col]);
            }
        }

        return ciphertext.toString();
    }

    public static String decryptColumnarTransposition(String text, String key) {
        int keyLength = key.length();
        int numRows = (int) Math.ceil((double) text.length() / keyLength);

        int[] order = getColumnOrder(key);

        char[][] grid = new char[numRows][keyLength];
        int index = 0;
        for (int col : order) {
            for (int row = 0; row < numRows; row++) {
                grid[row][col] = text.charAt(index++);
            }
        }

        StringBuilder plaintext = new StringBuilder();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < keyLength; col++) {
                plaintext.append(grid[row][col]);
            }
        }

        return plaintext.toString().replaceAll("X+$", "");
    }

    public static int[] getColumnOrder(String key) {
        int keyLength = key.length();
        int[] order = new int[keyLength];
        boolean[] used = new boolean[keyLength];

        for (int i = 0; i < keyLength; i++) {
            char minChar = Character.MAX_VALUE;
            int minIndex = -1;

            for (int j = 0; j < keyLength; j++) {
                if (!used[j] && key.charAt(j) < minChar) {
                    minChar = key.charAt(j);
                    minIndex = j;
                }
            }

            order[i] = minIndex;
            used[minIndex] = true;
        }

        return order;
    }
}