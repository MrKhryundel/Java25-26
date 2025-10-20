import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Модель: інтерфейс Drawable та ієрархія Shape
 */
interface Drawable {
    void draw();
}

abstract class Shape implements Drawable {
    protected String shapeColor;

    public Shape(String shapeColor) {
        this.shapeColor = shapeColor;
    }

    public abstract double calcArea();

    @Override
    public String toString() {
        return String.format("%s (color=%s, area=%.2f)", this.getClass().getSimpleName(), shapeColor, calcArea());
    }

    public String getShapeColor() {
        return shapeColor;
    }
}

class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public double calcArea() {
        return width * height;
    }

    @Override
    public void draw() {
        System.out.println(String.format("[Rectangle] color=%s, w=%.2f, h=%.2f", shapeColor, width, height));
    }

    @Override
    public String toString() {
        return String.format("Rectangle(color=%s, w=%.2f, h=%.2f, area=%.2f)", shapeColor, width, height, calcArea());
    }
}

class Triangle extends Shape {
    private double base;
    private double height;

    public Triangle(String color, double base, double height) {
        super(color);
        this.base = base;
        this.height = height;
    }

    @Override
    public double calcArea() {
        return 0.5 * base * height;
    }

    @Override
    public void draw() {
        System.out.println(String.format("[Triangle] color=%s, base=%.2f, h=%.2f", shapeColor, base, height));
    }

    @Override
    public String toString() {
        return String.format("Triangle(color=%s, base=%.2f, h=%.2f, area=%.2f)", shapeColor, base, height, calcArea());
    }
}

class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double calcArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public void draw() {
        System.out.println(String.format("[Circle] color=%s, r=%.2f", shapeColor, radius));
    }

    @Override
    public String toString() {
        return String.format("Circle(color=%s, r=%.2f, area=%.2f)", shapeColor, radius, calcArea());
    }
}

/**
 * Подання: допомога для виводу в консоль
 */
class ConsoleView {
    public void printHeader(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }

    public void printShapes(List<Shape> shapes) {
        if (shapes == null || shapes.isEmpty()) {
            System.out.println("No shapes available.");
            return;
        }
        for (int i = 0; i < shapes.size(); i++) {
            System.out.println((i + 1) + ". " + shapes.get(i).toString());
        }
    }

    public void printTotalArea(double total) {
        System.out.println(String.format("Total area of all shapes: %.2f", total));
    }

    public void printTotalAreaForType(String typeName, double total) {
        System.out.println(String.format("Total area for type %s: %.2f", typeName, total));
    }

    public void printMessage(String msg) {
        System.out.println(msg);
    }
}

/**
 * Контролер: створює набір даних і обробляє його
 */
public class Lab3 {
    // заздалегідь підготовлені набори даних
    private static final String[] COLORS = {"Red", "Green", "Blue", "Yellow", "Black", "White", "Orange", "Purple"};
    private static final double[] RECT_W = {2.0, 3.5, 4.0, 5.2, 1.5};
    private static final double[] RECT_H = {1.0, 2.0, 3.0, 4.5, 2.2};
    private static final double[] TRI_B = {3.0, 4.0, 5.5, 2.0, 6.0};
    private static final double[] TRI_H = {2.0, 3.0, 4.0, 1.5, 3.5};
    private static final double[] CIRCLE_R = {1.0, 1.5, 2.0, 2.5, 3.0};

    private ConsoleView view = new ConsoleView();
    private Random rnd = new Random();

    public static void main(String[] args) {
        Lab3 app = new Lab3();
        List<Shape> shapes = app.createShapeDataset(12); // принаймні 10 елементів
        app.view.printHeader("Original dataset (after creation)");
        app.view.printShapes(shapes);

        app.view.printHeader("Processing results");

        // загальна площа всіх фігур
        double totalAll = app.calculateTotalArea(shapes);
        app.view.printTotalArea(totalAll);

        // випадковий вибір типу фігури для обробки (Rectangle, Triangle, Circle) згідно вимоги
        String[] types = {"Rectangle", "Triangle", "Circle"};
        String chosenType = types[app.rnd.nextInt(types.length)];
        double totalByType = app.calculateTotalAreaByType(shapes, chosenType);
        if (totalByType == 0.0) {
            app.view.printMessage("No shapes of type " + chosenType + " were found.");
        } else {
            app.view.printTotalAreaForType(chosenType, totalByType);
        }

        // сортування за площею за зростанням з використанням Comparator
        List<Shape> byArea = new ArrayList<>(shapes);
        Collections.sort(byArea, Comparator.comparingDouble(Shape::calcArea));
        app.view.printHeader("Shapes sorted by area (ascending)");
        app.view.printShapes(byArea);

        // сортування за кольором (лексикографічно) з використанням Comparator
        List<Shape> byColor = new ArrayList<>(shapes);
        Collections.sort(byColor, Comparator.comparing(Shape::getShapeColor));
        app.view.printHeader("Shapes sorted by color (lexicographic)");
        app.view.printShapes(byColor);
    }

    // створення набору фігур з використанням підготовлених даних, значення обираються випадково з наборів
    public List<Shape> createShapeDataset(int size) {
        List<Shape> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int shapeKind = rnd.nextInt(3); // 0 - прямокутник, 1 - трикутник, 2 - круг
            String color = COLORS[rnd.nextInt(COLORS.length)];
            switch (shapeKind) {
                case 0:
                    double w = RECT_W[rnd.nextInt(RECT_W.length)];
                    double h = RECT_H[rnd.nextInt(RECT_H.length)];
                    list.add(new Rectangle(color, w, h));
                    break;
                case 1:
                    double b = TRI_B[rnd.nextInt(TRI_B.length)];
                    double th = TRI_H[rnd.nextInt(TRI_H.length)];
                    list.add(new Triangle(color, b, th));
                    break;
                case 2:
                default:
                    double r = CIRCLE_R[rnd.nextInt(CIRCLE_R.length)];
                    list.add(new Circle(color, r));
                    break;
            }
        }
        return list;
    }

    // обчислити загальну площу всіх фігур
    public double calculateTotalArea(List<Shape> shapes) {
        return shapes.stream().mapToDouble(Shape::calcArea).sum();
    }

    // обчислити сумарну площу фігур заданого типу (наприклад, Rectqngle)
    public double calculateTotalAreaByType(List<Shape> shapes, String typeName) {
        return shapes.stream()
                .filter(s -> s.getClass().getSimpleName().equalsIgnoreCase(typeName))
                .mapToDouble(Shape::calcArea)
                .sum();
    }
}
