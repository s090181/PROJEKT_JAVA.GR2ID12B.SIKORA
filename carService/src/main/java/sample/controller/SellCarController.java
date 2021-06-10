package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.Car;
import sample.service.CarService;
import sample.service.DataBaseService;

import java.io.IOException;
import java.util.HashMap;

/**
 * klasa conroller pozwalajaca na zarzadzanie widokiem sellCar.fxml
 */
public class SellCarController {

    @FXML
    public Label lblMessage;

    @FXML
    public TextField tfFirstName, tfLastName, tfPesel, tfCardNumber, tfCountry, tfVoivodeship, tfTown, tfStreet, tfHouseNumber;

    private CarService carService;
    private DataBaseService dBS;

    /**
     * metoda uruchamiana podczas wlaczenia widoku, zadanie inicjalizacja pol klasy
     */
    public void initialize() {
        carService = new CarService();
        dBS = new DataBaseService();
    }

    /**
     * metoda pozwalajaca na sprzedanie samochodu
     * nalezy wprowadzic dane klienta oraz jego adres
     * wymagana jest etapowa walidacja, w przypadku niepoprawnosci etapu zwracana jest informacja o bledzie
     * w celu sprzedania auta konieczna jest 100% poprawnosc danych
     */
    public void sell() {
        HashMap<String, String> userDataMap = new HashMap<>();
        userDataMap.put("firstName", tfFirstName.getText());
        userDataMap.put("lastName", tfLastName.getText());
        userDataMap.put("pesel", tfPesel.getText());
        userDataMap.put("cardNumber", tfCardNumber.getText());
        userDataMap.put("country", tfCountry.getText());
        userDataMap.put("voivodeship", tfVoivodeship.getText());
        userDataMap.put("town", tfTown.getText());
        userDataMap.put("street", tfStreet.getText());
        userDataMap.put("houseNumber", tfHouseNumber.getText());

        if (MainViewController.selectedCar != null) {
            Car car = carService.sell(userDataMap, MainViewController.selectedCar);
            if (car != null) {
                dBS.update(car);
                lblMessage.setText("Samochód sprzedany");
            } else {
                lblMessage.setText("Wszystkie pola muszą być wypełnione");
            }
        } else {
            lblMessage.setText("Samochód już sprzedany");
        }
    }

    /**
     * metoda pozwalajaca na zmiane widoku na mainView.fxml
     *
     * @param actionEvent event pozwalajacy na przechwycenie aktualnie otwartego okna
     * @throws IOException rzucany w momencie gdy scieżka do pliku fxml jest nie poprawna
     */
    public void back(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/mainView.fxml"));
        Scene scene = new Scene(parent);

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
