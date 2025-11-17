import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

public class Lab4Test {

    private LionCage lionCage;
    private BirdCage birdCage;
    private HoofedCage hoofedCage;

    private Lion lion1;
    private Lion lion2;
    private Lion lion3;

    private Eagle eagle1;
    private Eagle eagle2;

    private Zebra zebra1;
    private Giraffe giraffe1;

    @BeforeEach
    void setup() {
        lionCage = new LionCage(2);
        birdCage = new BirdCage(3);
        hoofedCage = new HoofedCage(4);

        lion1 = new Lion("Simba");
        lion2 = new Lion("Nala");
        lion3 = new Lion("Scar");

        eagle1 = new Eagle("Aquila");
        eagle2 = new Eagle("Zephyr");

        zebra1 = new Zebra("Marty");
        giraffe1 = new Giraffe("Melman");
    }

    // -------------------------- Cage tests --------------------------

    @Test
    void testAddAnimalToCage() {
        lionCage.addAnimal(lion1);
        lionCage.addAnimal(lion2);

        assertEquals(2, lionCage.getOccupiedCount());
    }

    @Test
    void testAddAnimalOverCapacityThrowsException() {
        lionCage.addAnimal(lion1);
        lionCage.addAnimal(lion2);

        assertThrows(IllegalStateException.class, () -> {
            lionCage.addAnimal(lion3);
        });
    }

    @Test
    void testRemoveAnimal() {
        birdCage.addAnimal(eagle1);
        birdCage.removeAnimal(eagle1);

        assertEquals(0, birdCage.getOccupiedCount());
    }

    @Test
    void testRemoveNonExistingAnimalThrowsException() {
        birdCage.addAnimal(eagle1);

        assertThrows(NoSuchElementException.class, () -> {
            birdCage.removeAnimal(eagle2);
        });
    }

    @Test
    void testHoofedCageAcceptsOnlyHoofedAnimals() {
        hoofedCage.addAnimal(zebra1);
        hoofedCage.addAnimal(giraffe1);

        assertEquals(2, hoofedCage.getOccupiedCount());
    }

    @Test
    void testBirdCageAcceptsOnlyBirds() {
        birdCage.addAnimal(eagle1);

        assertEquals(1, birdCage.getOccupiedCount());
    }

    @Test
    void testLionCageAcceptsOnlyLions() {
        lionCage.addAnimal(lion1);

        assertEquals(1, lionCage.getOccupiedCount());
    }

    // -------------------------- Zoo tests --------------------------

    @Test
    void testZooCountAnimals() {
        Zoo zoo = new Zoo();
        zoo.addCage(lionCage);
        zoo.addCage(birdCage);
        zoo.addCage(hoofedCage);

        lionCage.addAnimal(lion1);
        birdCage.addAnimal(eagle1);
        hoofedCage.addAnimal(zebra1);

        int count = zoo.getCountOfAnimals();
        assertEquals(3, count);
    }

    @Test
    void testZooWithEmptyCages() {
        Zoo zoo = new Zoo();
        zoo.addCage(lionCage);
        zoo.addCage(birdCage);

        assertEquals(0, zoo.getCountOfAnimals());
    }

    @Test
    void testZooAddCageIncreasesCageList() {
        Zoo zoo = new Zoo();

        zoo.addCage(lionCage);
        zoo.addCage(birdCage);

        assertEquals(2, zoo.cages.size());
    }

    // -------------------------- Generics / Type safety tests --------------------------

    @Test
    void testCannotPutBirdInLionCage() {
        assertThrows(ClassCastException.class, () -> {
            Cage raw = lionCage;        // raw type
            raw.addAnimal(eagle1);      // bypass generics at compile time
        });
    }

    @Test
    void testCannotPutLionInBirdCage() {
        assertThrows(ClassCastException.class, () -> {
            Cage raw = birdCage;
            raw.addAnimal(lion1);
        });
    }

    @Test
    void testCannotPutLionInHoofedCage() {
        assertThrows(ClassCastException.class, () -> {
            Cage raw = hoofedCage;
            raw.addAnimal(lion1);
        });
    }

    // -------------------------- Additional tests --------------------------

    @Test
    void testGetAnimalsReturnsCopyNotOriginal() {
        lionCage.addAnimal(lion1);

        var list = lionCage.getAnimals();
        list.clear();  // modify returned list

        assertEquals(1, lionCage.getOccupiedCount());
    }

    @Test
    void testMaxCapacityIsCorrect() {
        assertEquals(2, lionCage.getMaxCapacity());
        assertEquals(3, birdCage.getMaxCapacity());
    }
}

