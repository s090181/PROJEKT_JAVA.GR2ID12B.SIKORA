package sample.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * klasa model majaca na celu odwzorowac dane klienta
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {

    private String firstName;
    private String lastName;
    private String pesel;
    private String cardNumber;

    private Address address;

    @Override
    public String toString() {
        return "Imię : " + firstName
                + ", Nazwisko : " + lastName;
    }
}
