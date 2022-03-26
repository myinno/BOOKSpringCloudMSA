package se.magnus.microservices.core.review;

import java.util.ArrayList;

class LandAnimal { public void crying() { System.out.println("��������"); } }
class Cat extends LandAnimal { public void crying() { System.out.println("�Ŀ˳Ŀ�"); } }
class Dog extends LandAnimal { public void crying() { System.out.println("�۸�"); } }
class Sparrow { public void crying() { System.out.println("±±"); } }

class AnimalList<T> {
    ArrayList<T> al = new ArrayList<T>();
    void add(T animal) { al.add(animal); }
    T get(int index) { return al.get(index); }
    boolean remove(T animal) { return al.remove(animal); }
    int size() { return al.size(); }
}

public class Generic01 {
    public static void main(String[] args) {
        AnimalList<LandAnimal> landAnimal = new AnimalList<>(); // Java SE 7���� ����������.
        landAnimal.add(new LandAnimal());
        landAnimal.add(new Cat());
        landAnimal.add(new Dog());
        // landAnimal.add(new Sparrow()); // ������ �߻���.
        for (int i = 0; i < landAnimal.size() ; i++) {
            landAnimal.get(i).crying();
        }
    }
}