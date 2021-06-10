package sample.service;

import org.junit.jupiter.api.Test;
import sample.model.Car;
import sample.model.FuelType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarServiceTest {

    private final CarService carService = new CarService();

    private HashMap<String, String> carDataMap() {
        HashMap<String, String> carDataMap = new HashMap<>();
        carDataMap.put("brand", "brandTest");
        carDataMap.put("model", "modelTest");
        carDataMap.put("bodyType", "bodyTypeTest");
        carDataMap.put("engineCapacity", "1800");
        carDataMap.put("manufactureYear", "2000");
        carDataMap.put("mileage", "150");
        carDataMap.put("price", "20000");
        carDataMap.put("gearboxType", "gearboxTest");
        carDataMap.put("vINNumber", "vinNumberTest");
        carDataMap.put("color", "colorTest");
        carDataMap.put("country", "countryTest");
        return carDataMap;
    }

    private HashMap<String, String> customerDateMap() {
        HashMap<String, String> customerDataMap = new HashMap<>();
        customerDataMap.put("firstName", "firstNameTest");
        customerDataMap.put("lastName", "lastNameTest");
        customerDataMap.put("pesel", "peselTest");
        customerDataMap.put("cardNumber", "cardNumberTest");
        customerDataMap.put("country", "countryTest");
        customerDataMap.put("voivodeship", "voivodeshipTest");
        customerDataMap.put("town", "townTest");
        customerDataMap.put("street", "streetTest");
        customerDataMap.put("houseNumber", "houseNumberTest");
        return customerDataMap;
    }

    private List<Car> carList() {
        List<Car> carList = new ArrayList<>();
        carList.add(new Car("1", "a", "carModel", "body type", 2000, 2000, 100, 100.50, "gearboxType", "vin", FuelType.PETROL, "color", "country", null));
        carList.add(new Car("2", "c", "carModel", "body type", 2000, 1998, 150, 20, "gearboxType", "vin", FuelType.PETROL, "blue", "country", null));
        carList.add(new Car("3", "b", "carModel", "body type", 2000, 2005, 70, 30.60, "gearboxType", "vin", FuelType.PETROL, "color", "country", null));
        carList.add(new Car("4", "e", "carModel", "body type", 2000, 2010, 50, 200, "gearboxType", "vin", FuelType.PETROL, "blue", "country", null));
        carList.add(new Car("5", "d", "carModel", "body type", 2000, 2021, 5, 15.15, "gearboxType", "vin", FuelType.PETROL, "color", "country", null));
        return carList;
    }

    //add
    @Test
    void add_fuelTypeNull_assertNull() {
        HashMap<String, String> carDataMap = carDataMap();
        assertNull(carService.add(carDataMap, null));
    }

    @Test
    void add_brandEmpty_assertNull() {
        HashMap<String, String> carDataMap = carDataMap();
        carDataMap.put("brand", "");
        FuelType fuelType = FuelType.PETROL;

        assertNull(carService.add(carDataMap, fuelType));
    }

    @Test
    void add_throwNumberFormatException_assertionTrue() {
        HashMap<String, String> carDataMap = carDataMap();
        carDataMap.put("mileage", "aaa");
        FuelType fuelType = FuelType.PETROL;

        boolean flag = false;
        try {
            carService.add(carDataMap, fuelType);
        } catch (NumberFormatException e) {
            flag = true;
        }

        assertTrue(flag);
    }

    @Test
    void add_addNewCar_assertNotNull() {
        HashMap<String, String> carDataMap = carDataMap();
        FuelType fuelType = FuelType.PETROL;

        assertNotNull(carService.add(carDataMap, fuelType));
    }

    //edit
    @Test
    void edit_fuelTypeNull_assertNull() {
        HashMap<String, String> carDataMap = carDataMap();
        Car car = new Car();
        assertNull(carService.edit(car, carDataMap, null));
    }

    @Test
    void edit_carNull_assertNull() {
        HashMap<String, String> carDataMap = carDataMap();
        assertNull(carService.edit(null, carDataMap, FuelType.PETROL));
    }

    @Test
    void edit_throwNumberFormatException_assertionTrue() {
        HashMap<String, String> carDataMap = carDataMap();
        carDataMap.put("mileage", "aaa");
        FuelType fuelType = FuelType.PETROL;

        boolean flag = false;
        try {
            carService.edit(new Car(), carDataMap, fuelType);
        } catch (NumberFormatException e) {
            flag = true;
        }

        assertTrue(flag);
    }

    @Test
    void edit_editCar_assertEquals() {
        HashMap<String, String> carDataMap = carDataMap();
        Car car1 = carService.add(carDataMap, FuelType.PETROL_GAS);
        Car car2 = carService.edit(car1, carDataMap, FuelType.PETROL);
        assertEquals(car1, car2);
    }

    //sell
    @Test
    void sell_firstNameEmpty_assertNull() {
        HashMap<String, String> customerDataMap = customerDateMap();
        customerDataMap.put("firstName", "");

        assertNull(carService.sell(customerDataMap, new Car()));
    }

    @Test
    void sell_carNull_assertNull() {
        HashMap<String, String> customerDataMap = customerDateMap();
        assertNull(carService.sell(customerDataMap, null));
    }

    @Test
    void sell_assertNotNull() {
        HashMap<String, String> customerDataMap = customerDateMap();
        assertNotNull(carService.sell(customerDataMap, new Car()));
    }

    //sortedCars
    @Test
    void sortedCars_brandAsc_assertEquals() {
        Car car = carList().get(3);
        List<Car> sortedCarLitByBrandAsc = carService.sortedCars(carList(), "marka", "rosnąco");
        assertEquals(car, sortedCarLitByBrandAsc.get(4));
    }

    @Test
    void sortedCars_brandDesc_assertEquals() {
        Car car = carList().get(3);
        List<Car> sortedCarLitByBrandAsc = carService.sortedCars(carList(), "marka", "malejąco");
        assertEquals(car, sortedCarLitByBrandAsc.get(0));
    }

    @Test
    void sortedCars_null_assertEquals() {
        List<Car> sortedCarLitByBrandAsc = carService.sortedCars(carList(), null, null);
        assertEquals(0, sortedCarLitByBrandAsc.size());
    }

    //validation
    @Test
    void isManufactureYearValid_1900_assertTrue(){
        assertTrue(carService.isManufactureYearValid(1900));
    }

    @Test
    void isManufactureYearValid_1700_assertFalse(){
        assertFalse(carService.isManufactureYearValid(1700));
    }

    @Test
    void isMileageValid_50_assertTrue(){
        assertTrue(carService.isMileageValid(50));
    }

    @Test
    void isMileageValid_0_assertFalse(){
        assertFalse(carService.isMileageValid(0));
    }

    @Test
    void isPriceValid_50_assertTrue(){
        assertTrue(carService.isPriceValid(50));
    }

    @Test
    void isPriceValid_0_assertFalse(){
        assertFalse(carService.isPriceValid(0));
    }

    @Test
    void isVinValid_assertTrue(){
        assertTrue(carService.isVinValid("abAbabAbcdCdcdCD1"));
    }

    @Test
    void isVinValid_stringSize16_assertFalse(){
        assertFalse(carService.isVinValid("aaaaaaaaaaaaaaaa"));
    }

    @Test
    void isVinValid_illegalChars_assertFalse(){
        assertFalse(carService.isVinValid("aaaaaaaaaiaOaaaa"));
    }
}