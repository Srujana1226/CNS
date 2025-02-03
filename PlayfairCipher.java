import java.util.Scanner;

public class PlayfairCipher {
    private char[][] matrix;
    private String key;
    
    public PlayfairCipher(String key) {
        this.key = key.toUpperCase().replaceAll("J", "I");
        this.matrix = generateMatrix(this.key);
    }

    private char[][] generateMatrix(String key) {
        boolean[] used = new boolean[26];
        char[][] matrix = new char[5][5];
        int index = 0;
        
        key = key.replaceAll("[^A-Z]", "");
        StringBuilder keyBuilder = new StringBuilder();
        for (char c : key.toCharArray()) {
            if (!used[c - 'A']) {
                keyBuilder.append(c);
                used[c - 'A'] = true;
            }
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            if (c == 'J') continue;
            if (!used[c - 'A']) {
                keyBuilder.append(c);
                used[c - 'A'] = true;
            }
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = keyBuilder.charAt(index++);
            }
        }
        return matrix;
    }
    
    private String formatText(String text) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "").replaceAll("J", "I");
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            formatted.append(text.charAt(i));
            if (i < text.length() - 1 && text.charAt(i) == text.charAt(i + 1)) {
                formatted.append('X');
            }
        }
        if (formatted.length() % 2 != 0) {
            formatted.append('X');
        }
        return formatted.toString();
    }
    
    private String encryptPair(char a, char b) {
        int rowA = -1, colA = -1, rowB = -1, colB = -1;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == a) {
                    rowA = i; colA = j;
                }
                if (matrix[i][j] == b) {
                    rowB = i; colB = j;
                }
            }
        }
        
        if (rowA == rowB) {
            return "" + matrix[rowA][(colA + 1) % 5] + matrix[rowB][(colB + 1) % 5];
        } else if (colA == colB) {
            return "" + matrix[(rowA + 1) % 5][colA] + matrix[(rowB + 1) % 5][colB];
        } else {
            return "" + matrix[rowA][colB] + matrix[rowB][colA];
        }
    }
    
    private String decryptPair(char a, char b) {
        int rowA = -1, colA = -1, rowB = -1, colB = -1;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == a) {
                    rowA = i; colA = j;
                }
                if (matrix[i][j] == b) {
                    rowB = i; colB = j;
                }
            }
        }
        
        if (rowA == rowB) {
            return "" + matrix[rowA][(colA + 4) % 5] + matrix[rowB][(colB + 4) % 5];
        } else if (colA == colB) {
            return "" + matrix[(rowA + 4) % 5][colA] + matrix[(rowB + 4) % 5][colB];
        } else {
            return "" + matrix[rowA][colB] + matrix[rowB][colA];
        }
    }
    
    public String encrypt(String plaintext) {
        plaintext = formatText(plaintext);
        StringBuilder ciphertext = new StringBuilder();
        
        for (int i = 0; i < plaintext.length(); i += 2) {
            ciphertext.append(encryptPair(plaintext.charAt(i), plaintext.charAt(i + 1)));
        }
        return ciphertext.toString();
    }
    
    public String decrypt(String ciphertext) {
        StringBuilder plaintext = new StringBuilder();
        
        for (int i = 0; i < ciphertext.length(); i += 2) {
            plaintext.append(decryptPair(ciphertext.charAt(i), ciphertext.charAt(i + 1)));
        }
        return plaintext.toString();
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter keyword: ");
        String key = scanner.nextLine();
        
        PlayfairCipher cipher = new PlayfairCipher(key);
        
        System.out.print("Enter plaintext: ");
        String plaintext = scanner.nextLine();
        
        String encryptedText = cipher.encrypt(plaintext);
        System.out.println("Ciphertext: " + encryptedText);
        
        String decryptedText = cipher.decrypt(encryptedText);
        System.out.println("Decrypted Text: " + decryptedText);
        
        scanner.close();
    }
}