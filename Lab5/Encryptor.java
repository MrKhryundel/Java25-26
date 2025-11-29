import java.io.*;

/**
 * Клас для шифрування та дешифрування тексту
 */
public class Encryptor {

    /**
     * Допоміжний клас, що дозволяє використати FilterInputStream
     */
    private static class MyFilterInputStream extends FilterInputStream {
        protected MyFilterInputStream(InputStream in) {
            super(in); // легальне використання protected конструктора
        }
    }

    /**
     * Допоміжний клас, що дозволяє використати FilterOutputStream
     */
    private static class MyFilterOutputStream extends FilterOutputStream {
        protected MyFilterOutputStream(OutputStream out) {
            super(out);
        }
    }

    /**
     * Шифрування тексту у файл
     */
    public static void encrypt(String inputText, char keyChar, String filename) {
        try (MyFilterOutputStream fos = new MyFilterOutputStream(new FileOutputStream(filename))) {

            for (char c : inputText.toCharArray()) {
                fos.write(c + keyChar);
            }

            System.out.println("Text successfully encrypted to file: " + filename);

        } catch (IOException e) {
            System.out.println("Error encrypting text: " + e.getMessage());
        }
    }

    /**
     * Дешифрування тексту з файлу
     */
    public static String decrypt(char keyChar, String filename) {
        StringBuilder sb = new StringBuilder();

        try (MyFilterInputStream fis = new MyFilterInputStream(new FileInputStream(filename))) {

            int ch;
            while ((ch = fis.read()) != -1) {
                sb.append((char) (ch - keyChar));
            }

        } catch (IOException e) {
            System.out.println("Error decrypting text: " + e.getMessage());
        }

        return sb.toString();
    }
}







