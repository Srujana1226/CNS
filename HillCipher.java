import java.util.*;

public class HillCipher {

    // Function to get the determinant of a matrix
    public static int determinant(int[][] matrix, int n) {
        int det = 0;
        if (n == 1) {
            return matrix[0][0];
        }
        int[][] temp = new int[n][n];
        int sign = 1;
        for (int f = 0; f < n; f++) {
            getCofactor(matrix, temp, 0, f, n);
            det += sign * matrix[0][f] * determinant(temp, n - 1);
            sign = -sign;
        }
        return det;
    }

    // Function to get the cofactor of a matrix
    public static void getCofactor(int[][] matrix, int[][] temp, int p, int q, int n) {
        int i = 0, j = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row != p && col != q) {
                    temp[i][j++] = matrix[row][col];
                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    // Function to get the adjoint of a matrix
    public static void adjoint(int[][] matrix, int[][] adj, int n) {
        if (n == 1) {
            adj[0][0] = 1;
            return;
        }
        int sign = 1;
        int[][] temp = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                getCofactor(matrix, temp, i, j, n);
                sign = ((i + j) % 2 == 0) ? 1 : -1;
                adj[j][i] = (sign) * (determinant(temp, n - 1));
            }
        }
    }

    // Function to find the inverse of a matrix modulo 26
    public static boolean inverse(int[][] matrix, int[][] inverse, int n) {
        int det = determinant(matrix, n);
        det = mod(det, 26);
        int detInverse = modInverse(det, 26);
        if (detInverse == -1) {
            return false;
        }
        int[][] adj = new int[n][n];
        adjoint(matrix, adj, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse[i][j] = mod(adj[i][j] * detInverse, 26);
            }
        }
        return true;
    }

    // Function to perform modulo operation
    public static int mod(int a, int m) {
        return (a % m + m) % m;
    }

    // Function to find modular inverse
    public static int modInverse(int a, int m) {
        a = mod(a, m);
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1;
    }

    // Function to encrypt a message
    public static String encrypt(String plaintext, int[][] key) {
        int n = key.length;
        int[] msg = new int[plaintext.length()];
        for (int i = 0; i < plaintext.length(); i++) {
            msg[i] = plaintext.charAt(i) - 'A';
        }

        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < msg.length; i += n) {
            int[] block = new int[n];
            for (int j = 0; j < n && (i + j) < msg.length; j++) {
                block[j] = msg[i + j];
            }
            int[] encryptedBlock = new int[n];
            for (int j = 0; j < n; j++) {
                encryptedBlock[j] = 0;
                for (int k = 0; k < n; k++) {
                    encryptedBlock[j] += key[j][k] * block[k];
                }
                encryptedBlock[j] = mod(encryptedBlock[j], 26);
            }
            for (int j = 0; j < n; j++) {
                ciphertext.append((char) (encryptedBlock[j] + 'A'));
            }
        }
        return ciphertext.toString();
    }

    // Function to decrypt a message
    public static String decrypt(String ciphertext, int[][] key) {
        int n = key.length;
        int[][] inverseKey = new int[n][n];
        if (!inverse(key, inverseKey, n)) {
            return "Inverse not possible!";
        }

        int[] msg = new int[ciphertext.length()];
        for (int i = 0; i < ciphertext.length(); i++) {
            msg[i] = ciphertext.charAt(i) - 'A';
        }

        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < msg.length; i += n) {
            int[] block = new int[n];
            for (int j = 0; j < n && (i + j) < msg.length; j++) {
                block[j] = msg[i + j];
            }
            int[] decryptedBlock = new int[n];
            for (int j = 0; j < n; j++) {
                decryptedBlock[j] = 0;
                for (int k = 0; k < n; k++) {
                    decryptedBlock[j] += inverseKey[j][k] * block[k];
                }
                decryptedBlock[j] = mod(decryptedBlock[j], 26);
            }
            for (int j = 0; j < n; j++) {
                plaintext.append((char) (decryptedBlock[j] + 'A'));
            }
        }
        return plaintext.toString();
    }

    // Main method for taking input dynamically
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Enter the size of the matrix (n x n)
        System.out.print("Enter the size of the key matrix (n): ");
        int n = sc.nextInt();

        // Input the key matrix
        int[][] key = new int[n][n];
        System.out.println("Enter the key matrix (" + n + "x" + n + "): ");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                key[i][j] = sc.nextInt();
            }
        }

        // Input the plaintext message
        sc.nextLine();  // Consume the newline character after nextInt
        System.out.print("Enter the plaintext (letters only, no spaces): ");
        String plaintext = sc.nextLine().toUpperCase();
        sc.close();
        // Ensure the length of the plaintext is a multiple of n
        if (plaintext.length() % n != 0) {
            System.out.println("Error: The length of the plaintext must be a multiple of " + n);
            return;
        }

        System.out.println("Plaintext: " + plaintext);

        // Encrypt the plaintext
        String ciphertext = encrypt(plaintext, key);
        System.out.println("Ciphertext: " + ciphertext);

        // Decrypt the ciphertext
        String decryptedText = decrypt(ciphertext, key);
        System.out.println("Decrypted Text: " + decryptedText);
    }
}