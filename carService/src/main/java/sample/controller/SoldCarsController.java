package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.model.Car;
import sample.service.CarService;

import java.io.IOException;

/**
 * klasa conroller pozwalajaca na zarzadzanie widokiem soldCars.fxml
 */
public class SoldCarsController {

    @FXML
    public ListView<Car> carList;

    private CarService carService;

    /**
     * metoda uruchamiana podczas wlaczenia widoku, zadanie inicjalizacja pol klasy
     */
    public void initialize() {
        carService = new CarService();
        carList.getItems().setAll(carService.soldCars());
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
