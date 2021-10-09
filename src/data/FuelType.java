package data;

import java.io.Serializable;

/**
 * Enum of FuelType
 */
public enum FuelType implements Serializable {
    KEROSENE("KEROSENE"),
    NUCLEAR("NUCLEAR"),
    PLASMA("PLASMA");
    private final String name;

    FuelType(String name) {
        this.name = name;
    }

    /**
     * Show all values in enum
     * @return String with all Values
     */
    public static String showAllValues() {
        StringBuilder s = new StringBuilder("");
        for (FuelType env : FuelType.values()) {
            s.append(env.getName()).append(" ");
        }
        return s.toString();
    }

    private String getName() {
        return name;
    }
}
