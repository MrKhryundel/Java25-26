import java.io.*;
import java.util.*;

/**
 * Інтерфейс для об’єктів, які можна малювати
 */
interface Drawable {
    void draw();
}

/**
 * Базовий клас фігури
 */
abstract class Shape implements Drawable, Serializable {
    protected String shapeColor;

    public Shape(String shapeColor) {
        this.shapeColor = shapeColor;
    }

    public abstract double calcArea();

    public String getShapeColor() {
        return shapeColor;
    }

    @Override
    public String toString() {
        return String.format("%s(color=%s, area=%.2f)", getClass().getSimpleName(), shapeColor, calcArea());
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
 * Клас для роботи з файлами Shape
 */
class ShapeFileManager {

    public static void saveShapesToFile(List<Shape> shapes, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(shapes);
            System.out.println("Shapes saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving shapes: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Shape> loadShapesFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<Shape>) obj;
            } else {
                System.out.println("File does not contain a valid shapes list.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading shapes: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}

