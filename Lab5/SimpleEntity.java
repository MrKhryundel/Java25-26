import java.io.Serializable;

/**
 * Клас, що представляє просту сутність із двома полями
 */
public class SimpleEntity implements Serializable {
    private String name;
    private int value;

    public SimpleEntity(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public void display() {
        System.out.println("Name: " + name + ", Value: " + value);
    }
}


