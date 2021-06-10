package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.Car;
import sample.model.FuelType;
import sample.service.CarService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * klasa conroller pozwalajaca na zarzadzanie widokiem mainView.fxml
 */
public class MainViewController {

    @FXML
    public ListView<Car> carList;

    @FXML
    public TextField tfBrand, tfModel, tfManufactureYear, tfMileage, tfPrice, tfGearbox;

    @FXML
    public ChoiceBox<String> cbSortType, cbSortValue;

    @FXML
    public ChoiceBox<FuelType> cbFuel;

    @FXML
    public CheckBox cheBBrand, cheBModel, cheBManufactureYear, cheBMileage, cheBPrice, cheBGearbox, cheBFuel;

    private CarService carService;
    public static Car selectedCar;

    /**
     * metoda uruchamiana podczas wlaczenia widoku, zadanie inicjalizacja pol klasy oraz ustawienie tzw. ChangeListener
     */
    public void initialize() {
        carService = new CarService();
        carList.getItems().addAll(carService.unsoldCars());

        if (selectedCar != null) {
            carList.getSelectionModel().select(selectedCar);
        }

        cbFuel.getItems().add(FuelType.DIESEL);
        cbFuel.getItems().add(FuelType.PETROL);
        cbFuel.getItems().add(FuelType.PETROL_GAS);

        List<String> sortType = new ArrayList<>();
        sortType.add("Marka");
        sortType.add("Cena");
        sortType.add("Rok produkcji");
        sortType.add("Przebieg");
        cbSortType.getItems().addAll(sortType);

        cbSortValue.getItems().add("Rosnąco");
        cbSortValue.getItems().add("Malejąco");

        carList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Car>() {
            @Override
            public void changed(ObservableValue<? extends Car> observable, Car oldValue, Car newValue) {
                selectedCar = carList.getSelectionModel().getSelectedItem();
            }
        });
    }

    /**
     * metoda pozwalajaca na usuniecie wskazanego samochodu
     */
    public void removeCar() {
        if (selectedCar != null) {
            carService.remove(selectedCar);
            carList.getItems().remove(selectedCar);
        }
    }

    /**
     * metoda pozwalajaca na wyszukanie listy samochodow po wybranych wartosciach
     */
    public void search() {
        List<Car> cars = carService.unsoldCars();

        if (cheBBrand.isSelected()) {
            String value = tfBrand.getText();
            if (!value.isEmpty())
                cars = cars.stream().filter(e -> e.getBrand().equals(value)).collect(Collectors.toList());
        }

        if (cheBModel.isSelected()) {
            String value = tfModel.getText();
            if (!value.isEmpty())
                cars = cars.stream().filter(e -> e.getModel().equals(value)).collect(Collectors.toList());
        }

        if (cheBManufactureYear.isSelected()) {
            try {
                String value = tfManufactureYear.getText();
                if (!value.isEmpty())
                    cars = cars.stream().filter(e -> e.getManufactureYear() == Integer.parseInt(value)).collect(Collectors.toList());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (cheBMileage.isSelected()) {
            try {
                String value = tfMileage.getText();
                if (!value.isEmpty())
                    cars = cars.stream().filter(e -> e.getMileage() == Integer.parseInt(value)).collect(Collectors.toList());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }

        if (cheBPrice.isSelected()) {
            try {
                String value = tfPrice.getText();
                if (!value.isEmpty())
                    cars = cars.stream().filter(e -> e.getPrice() == Double.parseDouble(value)).collect(Collectors.toList());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (cheBGearbox.isSelected()) {
            String value = tfGearbox.getText();
            if (!value.isEmpty())
                cars = cars.stream().filter(e -> e.getGearboxType().equals(value)).collect(Collectors.toList());
        }

        if (cheBFuel.isSelected()) {
            FuelType value = cbFuel.getSelectionModel().getSelectedItem();
            if (value != null)
                cars = cars.stream().filter(e -> e.getFuelType().equals(value)).collect(Collectors.toList());
        }

        carList.getItems().setAll(cars);
    }

    /**
     * metoda pozwalajaca na posortowanie aktualnie wyszukanej listy samochodow po wybranych wartosciach
     */
    public void sort() {
        String type = cbSortType.getSelectionModel().getSelectedItem();
        String value = cbSortValue.getSelectionModel().getSelectedItem();

        List<Car> cars = new ArrayList<>(carList.getItems());
        carList.getItems().setAll(carService.sortedCars(cars, type, value));
    }

    /**
     * metoda pozwalajaca na zmiane widoku na addCar.fxml
     *
     * @param actionEvent event pozwalajacy na przechwycenie aktualnie otwartego okna
     * @throws IOException rzucany w momencie gdy scieżka do pliku fxml jest nie poprawna
     */
    public void newCar(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/addCar.fxml"));
        Scene scene = new Scene(parent);

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * metoda pozwalajaca na zmiane widoku na editCar.fxml
     *
     * @param actionEvent event pozwalajacy na przechwycenie aktualnie otwartego okna
     * @throws IOException rzucany w momencie gdy scieżka do pliku fxml jest nie poprawna
     */
    public void editCar(ActionEvent actionEvent) throws IOException {
        if (selectedCar != null) {
            Parent parent = FXMLLoader.load(getClass().getResource("/fxml/editCar.fxml"));
            Scene scene = new Scene(parent);

            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }

    /**
     * metoda pozwalajaca na zmiane widoku na sellCar.fxml
     *
     * @param actionEvent event pozwalajacy na przechwycenie aktualnie otwartego okna
     * @throws IOException rzucany w momencie gdy scieżka do pliku fxml jest nie poprawna
     */
    public void sell(ActionEvent actionEvent) throws IOException {
        if (selectedCar != null) {
            Parent parent = FXMLLoader.load(getClass().getResource("/fxml/sellCar.fxml"));
            Scene scene = new Scene(parent);

            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }

    /**
     * metoda pozwalajaca na zmiane widoku na soldCars.fxml
     *
     * @param actionEvent event pozwalajacy na przechwycenie aktualnie otwartego okna
     * @throws IOException rzucany w momencie gdy scieżka do pliku fxml jest nie poprawna
     */
    public void sold(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/soldCars.fxml"));
        Scene scene = new Scene(parent);

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
