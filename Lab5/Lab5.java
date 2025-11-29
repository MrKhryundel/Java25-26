import java.util.*;
import java.io.*;

/**
 * Головний клас для інтеграції всіх функцій лабораторної роботи
 */
public class Lab5 {

    private static final String[] COLORS = {"Red", "Green", "Blue", "Yellow", "Black", "White", "Orange", "Purple"};
    private static final double[] RECT_W = {2.0, 3.5, 4.0, 5.2, 1.5};
    private static final double[] RECT_H = {1.0, 2.0, 3.0, 4.5, 2.2};
    private static final double[] TRI_B = {3.0, 4.0, 5.5, 2.0, 6.0};
    private static final double[] TRI_H = {2.0, 3.0, 4.0, 1.5, 3.5};
    private static final double[] CIRCLE_R = {1.0, 1.5, 2.0, 2.5, 3.0};

    private Scanner sc = new Scanner(System.in);
    private Random rnd = new Random();
    private List<Shape> shapes = new ArrayList<>();

    public static void main(String[] args) {
        new Lab5().run();
    }

    private void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nMenu:");
            System.out.println("1. Create random shapes dataset");
            System.out.println("2. Show shapes");
            System.out.println("3. Save shapes to file");
            System.out.println("4. Load shapes from file");
            System.out.println("5. Encrypt text to file");
            System.out.println("6. Decrypt text from file");
            System.out.println("7. Count tags from URL");
            System.out.println("8. Find line with max words in file");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    shapes = createShapeDataset(12);
                    System.out.println("Random shapes dataset created.");
                    break;
                case "2":
                    printShapes();
                    break;
                case "3":
                    System.out.print("Enter filename to save shapes: ");
                    String saveFile = sc.nextLine();
                    ShapeFileManager.saveShapesToFile(shapes, saveFile);
                    break;
                case "4":
                    System.out.print("Enter filename to load shapes: ");
                    String loadFile = sc.nextLine();
                    shapes = ShapeFileManager.loadShapesFromFile(loadFile);
                    System.out.println("Shapes loaded from file.");
                    break;
                case "5":
                    System.out.print("Enter text to encrypt: ");
                    String text = sc.nextLine();
                    System.out.print("Enter key character: ");
                    char keyEnc = sc.nextLine().charAt(0);
                    System.out.print("Enter filename to save encrypted text: ");
                    String encFile = sc.nextLine();
                    Encryptor.encrypt(text, keyEnc, encFile);
                    break;
                case "6":
                    System.out.print("Enter key character for decryption: ");
                    char keyDec = sc.nextLine().charAt(0);
                    System.out.print("Enter filename of encrypted text: ");
                    String decFile = sc.nextLine();
                    String decrypted = Encryptor.decrypt(keyDec, decFile);
                    System.out.println("Decrypted text: " + decrypted);
                    break;
                case "7":
                    System.out.print("Enter URL: ");
                    String url = sc.nextLine();
                    Map<String, Integer> freq = TagCounter.countTags(url);
                    TagCounter.printTagsLex(freq);
                    TagCounter.printTagsByFrequency(freq);
                    break;
                case "8":
                    System.out.print("Enter filename to analyze: ");
                    String filename = sc.nextLine();
                    String maxLine = MaxWordsFinder.findMaxWordsLine(filename);
                    System.out.println("Line with max words: " + maxLine);
                    break;
                case "9":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private List<Shape> createShapeDataset(int size) {
        List<Shape> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int shapeKind = rnd.nextInt(3);
            String color = COLORS[rnd.nextInt(COLORS.length)];
            switch (shapeKind) {
                case 0:
                    list.add(new Rectangle(color, RECT_W[rnd.nextInt(RECT_W.length)], RECT_H[rnd.nextInt(RECT_H.length)]));
                    break;
                case 1:
                    list.add(new Triangle(color, TRI_B[rnd.nextInt(TRI_B.length)], TRI_H[rnd.nextInt(TRI_H.length)]));
                    break;
                case 2:
                default:
                    list.add(new Circle(color, CIRCLE_R[rnd.nextInt(CIRCLE_R.length)]));
                    break;
            }
        }
        return list;
    }

    private void printShapes() {
        if (shapes.isEmpty()) {
            System.out.println("No shapes available.");
            return;
        }
        for (int i = 0; i < shapes.size(); i++) {
            System.out.println((i + 1) + ". " + shapes.get(i));
        }
    }
}

