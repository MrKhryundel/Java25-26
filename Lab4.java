import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public class Lab4 {
    public static void main(String[] args) {

        LionCage lionCage = new LionCage(2);
        BirdCage birdCage = new BirdCage(3);
        HoofedCage hoofedCage = new HoofedCage(4);

        Lion lion1 = new Lion("Simba");
        Lion lion2 = new Lion("Nala");
        Eagle eagle1 = new Eagle("Aquila");
        Eagle eagle2 = new Eagle("Zephyr");
        Zebra zebra1 = new Zebra("Marty");
        Giraffe giraffe1 = new Giraffe("Melman");

        lionCage.addAnimal(lion1);
        lionCage.addAnimal(lion2);

        birdCage.addAnimal(eagle1);
        birdCage.addAnimal(eagle2);

        hoofedCage.addAnimal(zebra1);
        hoofedCage.addAnimal(giraffe1);

        Zoo zoo = new Zoo();
        zoo.addCage(lionCage);
        zoo.addCage(birdCage);
        zoo.addCage(hoofedCage);

        System.out.println("Total animals in zoo: " + zoo.getCountOfAnimals());
        System.out.println("Lion cage occupied: " + lionCage.getOccupiedCount() + "/" + lionCage.getMaxCapacity());
        System.out.println("Bird cage occupied: " + birdCage.getOccupiedCount() + "/" + birdCage.getMaxCapacity());
        System.out.println("Hoofed cage occupied: " + hoofedCage.getOccupiedCount() + "/" + hoofedCage.getMaxCapacity());

        hoofedCage.removeAnimal(zebra1); 
        System.out.println("After removing zebra, hoofed occupied: " + hoofedCage.getOccupiedCount());

        try {
            hoofedCage.removeAnimal(zebra1); 
        } catch (NoSuchElementException e) {
            System.out.println("Expected exception on removing non-existing animal: " + e.getMessage());
        }

        try {

            lionCage.addAnimal(new Lion("Scar")); 
        } catch (IllegalStateException e) {
            System.out.println("Expected exception on adding to full cage: " + e.getMessage());
        }

        System.out.println("Final total animals in zoo: " + zoo.getCountOfAnimals());
    }
}

abstract class Animal {
    private final String name;

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

abstract class Mammal extends Animal {
    public Mammal(String name) {
        super(name);
    }
}

abstract class Bird extends Animal {
    public Bird(String name) {
        super(name);
    }
}

abstract class Hoofed extends Mammal {
    public Hoofed(String name) {
        super(name);
    }
}


class Lion extends Mammal {
    public Lion(String name) {
        super(name);
    }
}

class Zebra extends Hoofed {
    public Zebra(String name) {
        super(name);
    }
}

class Giraffe extends Hoofed {
    public Giraffe(String name) {
        super(name);
    }
}

class Eagle extends Bird {
    public Eagle(String name) {
        super(name);
    }
}


class Cage<T extends Animal> {
    private final int maxCapacity;
    private final List<T> animals = new ArrayList<>();

    public Cage(int maxCapacity) {
        if (maxCapacity < 0) throw new IllegalArgumentException("Capacity must be non-negative");
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getOccupiedCount() {
        return animals.size();
    }

    public void addAnimal(T animal) {
        if (animals.size() >= maxCapacity) {
            throw new IllegalStateException("Cage is full");
        }
        animals.add(animal);
    }

    public void removeAnimal(T animal) {
        boolean removed = animals.remove(animal);
        if (!removed) {
            throw new NoSuchElementException("Animal not found in cage");
        }
    }

    public List<T> getAnimals() {
        return new ArrayList<>(animals);
    }
}

class LionCage extends Cage<Lion> {
    public LionCage(int maxCapacity) {
        super(maxCapacity);
    }
}

class BirdCage extends Cage<Bird> {
    public BirdCage(int maxCapacity) {
        super(maxCapacity);
    }
}

class HoofedCage extends Cage<Hoofed> {
    public HoofedCage(int maxCapacity) {
        super(maxCapacity);
    }
}

class Zoo {
    public final List<Cage<? extends Animal>> cages = new ArrayList<>();

    public int getCountOfAnimals() {
        int total = 0;
        for (Cage<? extends Animal> c : cages) {
            total += c.getOccupiedCount();
        }
        return total;
    }

    public void addCage(Cage<? extends Animal> cage) {
        cages.add(cage);
    }
}


