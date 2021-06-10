package sample.model;

/**
 * klasa enum majaca na celu odwzorowac dane rodzaju paliwa w samochodzie
 */
public enum FuelType {

    DIESEL("Diesel"),
    PETROL("Benzyna"),
    PETROL_GAS("Benzyna + LPG");

    String polishName;

    FuelType(String polishName) {
        this.polishName = polishName;
    }

    @Override
    public String toString() {
        return polishName;
    }
}
