package sample.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 * klasa model majaca na celu odwzorowac dane samochodu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Car {

    @BsonProperty(value = "car_id")
    private String id;
    private String brand;
    private String model;
    private String bodyType;
    private int engineCapacity;
    private int manufactureYear;
    private int mileage;
    private double price;
    private String gearboxType;
    private String vinNumber;
    private FuelType fuelType;
    private String color;
    private String country;

    private Customer customer;

    @Override
    public String toString() {
        String string = "Marka : " + brand
                + ", Model: " + model
                + ", Typ nadwozia: " + bodyType
                + ", Pojemność silnika: " + engineCapacity
                + ", Rok produkcji : " + manufactureYear
                + ", Przebieg : " + mileage
                + ", Cena : " + price
                + ", Skrzynia biegów : " + gearboxType
                + ", VIN : " + vinNumber
                + ", Rodzaj paliwa : " + fuelType
                + ", Kolor : " + color
                + ", Kraj : " + country;

        if (customer != null) {
            string += ", Klient : " + customer;
        }

        return string;
    }
}
