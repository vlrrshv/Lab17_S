package collection;

import data.Vehicle;

import java.util.Comparator;

public class NameComparator implements Comparator<Vehicle> {

    @Override
    public int compare(Vehicle o1, Vehicle o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
