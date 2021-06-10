package sample.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * klasa model majaca na celu odwzorowac dane adresowe
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {

    private String country;
    private String voivodeship;
    private String town;
    private String street;
    private String houseNumber;
}
