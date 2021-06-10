package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.Car;
import sample.model.FuelType;
import sample.service.CarService;
import sample.service.DataBaseService;

import java.io.IOException;
import java.util.HashMap;

/**
 * klasa conroller pozwalajaca na zarzadzanie widokiem editCar.fxml
 */
public class EditCarController {

    @FXML
    public Label lblMessage;

    @FXML
    public TextField tfBrand, tfModel, tfBodyType, tfEngineCapacity, tfManufactureYear, tfMileage, tfPrice, tfGearboxType, tfVINNumber, tfColor, tfCountry;

    @FXML
    public ChoiceBox<FuelType> cbFuelType;

    private CarService carService;
    private DataBaseService dBS;
    private Car car;

    /**
     * metoda uruchamiana podczas wlaczenia widoku, zadanie inicjalizacja pol klasy
     */
    public void initialize() {
        carService = new CarService();
        dBS = new DataBaseService();
        cbFuelType.getItems().addAll(FuelType.values());
        car = MainViewController.selectedCar;

        tfBrand.setText(car.getBrand());
        tfModel.setText(car.getModel());
        tfBodyType.setText(car.getBodyType());
        tfEngineCapacity.setText("" + car.getEngineCapacity());
        tfManufactureYear.setText("" + car.getManufactureYear());
        tfMileage.setText("" + car.getMileage());
        tfPrice.setText("" + car.getPrice());
        tfGearboxType.setText(car.getGearboxType());
        tfVINNumber.setText(car.getVinNumber());
        tfColor.setText(car.getColor());
        tfCountry.setText(car.getCountry());

        cbFuelType.getSelectionModel().select(car.getFuelType());
    }

    /**
     * metoda pozwalajaca na edycje juz istniejacego samochodu
     * wymagana jest etapowa walidacja, w przypadku niepoprawnosci etapu zwracana jest informacja o bledzie
     * w celu zapisania edycji konieczna jest 100% poprawnosc danych
     */
    public void save() {
        HashMap<String, String> map = new HashMap<>();
        map.put("brand", tfBrand.getText());
        map.put("model", tfModel.getText());
        map.put("bodyType", tfBodyType.getText());
        map.put("engineCapacity", tfEngineCapacity.getText());
        map.put("manufactureYear", tfManufactureYear.getText());
        map.put("mileage", tfMileage.getText());
        map.put("price", tfPrice.getText());
        map.put("gearboxType", tfGearboxType.getText());
        map.put("vINNumber", tfVINNumber.getText());
        map.put("color", tfColor.getText());
        map.put("country", tfCountry.getText());
        FuelType fuelType = cbFuelType.getValue();

        try {
            if (carService.isFieldsValid(map)) {
                if (!carService.isManufactureYearValid(Integer.parseInt(map.get("manufactureYear")))) {
                    responseMessage("Rok produkcji powinien być pomiędzy 1870 a 2021");
                    return;
                }

                if (!carService.isMileageValid(Integer.parseInt(map.get("mileage")))) {
                    responseMessage("Przebieg musi być większy od 0");
                    return;
                }

                if (!carService.isPriceValid(Double.parseDouble(map.get("price")))) {
                    responseMessage("Cena musi być większa od 0");
                    return;
                }

                if (!carService.isVinValid(map.get("vINNumber"))) {
                    responseMessage("VIN musi składaćsię z 17 znaków, liczb i liter, bez liter i,I,o,O,q,Q");
                    return;
                }

                if (fuelType == null) {
                    responseMessage("Rodzaj paliwa musi być wybrany");
                    return;
                }

                Car carToUpdate = carService.edit(car, map, fuelType);
                if (carToUpdate != null) {
                    dBS.update(carToUpdate);
                    responseMessage("Samochód zapisany");
                }
            } else {
                responseMessage("Wszystkie pola muszą być wypełnione");
            }
        } catch (NumberFormatException e) {
            responseMessage("Zły typ danych ( Pojemność silnika, Rok produkcji, Przebieg, Cena są polami numerycznymi )");
        }
    }

    /**
     * metoda pozwalajaca na wyswietlenie wiadomosci o bledzie na widoku addCar.fxml
     *
     * @param message wartosc wyswietlanej wiadomosci
     */
    private void responseMessage(String message) {
        lblMessage.setText(message);
    }

    /**
     * metoda pozwalajaca na powrot do glownego widoku aplikacji mainView.fxml
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
