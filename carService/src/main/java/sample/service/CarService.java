package sample.service;

import sample.controller.MainViewController;
import sample.model.Address;
import sample.model.Car;
import sample.model.Customer;
import sample.model.FuelType;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Klasa zawierająca główne operacje na typie Car
 */
public class CarService {

    private final DataBaseService db;

    public CarService() {
        this.db = new DataBaseService();
    }

    /**
     * metoda mająca na celu zwrócenie wszystkich niesprzedanych samochodów znajdujących się w bazie danych
     * @return zwraca liste obiektów typu Car
     */
    public List<Car> unsoldCars() {
        List<Car> carList = db.findAll();
        return carList.stream().filter(e -> e.getCustomer() == null).collect(Collectors.toList());
    }

    /**
     * metoda mająca na calu zwrócenie wszystkich sprzedanych samochodów znajdującaych się w bazie danych
     * @return zwraca liste obiektów typu Car
     */
    public List<Car> soldCars() {
        List<Car> carList = db.findAll();
        return carList.stream().filter(e -> e.getCustomer() != null).collect(Collectors.toList());
    }

    /**
     * metoda pozwalajaca na stwożenie nowego samochodu
     * @param carDataMap mapa posiadająca dane samochodu
     * @param fuelType rodzaj używanego paliwa
     * @return w przypadku gdy wszystkie niebędne wartości są poprawne zostaje nam zwrócony obiekt typu Car, w przypadku gdy dane moją jakiś błąd zwrócony zostaje null
     * @throws NumberFormatException w momencie gdy nie będzie możliwe sparsowanie stringa do typu numerycznego rzucony zostanie wyjątek
     */
    public Car add(HashMap<String, String> carDataMap, FuelType fuelType) throws NumberFormatException {
        if (isFieldsValid(carDataMap) && fuelType != null) {
            return new Car(null, carDataMap.get("brand"), carDataMap.get("model"), carDataMap.get("bodyType"),
                    Integer.parseInt(carDataMap.get("engineCapacity")),
                    Integer.parseInt(carDataMap.get("manufactureYear")),
                    Integer.parseInt(carDataMap.get("mileage")),
                    Double.parseDouble(carDataMap.get("price")),
                    carDataMap.get("gearboxType"), carDataMap.get("vINNumber"), fuelType, carDataMap.get("color"), carDataMap.get("country"), null);
        }
        return null;
    }

    /**
     * metoda pozwalajaca na edycje wskazanego samochodu
     * @param car samochód który ma zostać poddany edycji
     * @param carDataMap mapa posiadająca dane samochodu
     * @param fuelType rodzaj używanego paliwa
     * @return jesli parametry przejdą poprawnie proces walidacji zostanie zwrócony typ Car z aktualnymi danymi, w przeciwnym razie zwracamy null
     * @throws NumberFormatException w momencie gdy nie będzie możliwe sparsowanie stringa do typu numerycznego rzucony zostanie wyjątek
     */
    public Car edit(Car car, HashMap<String, String> carDataMap, FuelType fuelType) throws NumberFormatException {
        if (isFieldsValid(carDataMap) && fuelType != null && car != null) {
            car.setBrand(carDataMap.get("brand"));
            car.setModel(carDataMap.get("model"));
            car.setBodyType(carDataMap.get("bodyType"));
            car.setEngineCapacity(Integer.parseInt(carDataMap.get("engineCapacity")));
            car.setManufactureYear(Integer.parseInt(carDataMap.get("manufactureYear")));
            car.setMileage(Integer.parseInt(carDataMap.get("mileage")));
            car.setPrice(Double.parseDouble(carDataMap.get("price")));
            car.setGearboxType(carDataMap.get("gearboxType"));
            car.setVinNumber(carDataMap.get("vINNumber"));
            car.setFuelType(fuelType);
            car.setColor(carDataMap.get("color"));
            car.setCountry(carDataMap.get("country"));
            return car;
        }
        return null;
    }

    /**
     * metoda pozwalająca na usuwanie pozycji z bazy danych
     * @param car obiekt typu Car który ma zostać usunięty
     */
    public void remove(Car car) {
        db.remove(car);
    }

    /**
     * metoda mająca na celu stwożenie użytkownika oraz jego adresu oraz przypisanie go do wskazanego samochodu co przełoży się na oznaczenie samochodu jako sprzedanego
     * @param userDataMap mapa posiadająca dane klienta
     * @param car samochód który zostanie sprzedany
     * @return jeśli parametry przejdą poprawną walidację zwracamy obiekt sprzedanego samochodu, w przeciwnym razie zwracamy null
     */
    public Car sell(HashMap<String, String> userDataMap, Car car) {
        if (isFieldsValid(userDataMap) && car != null) {
            Address address = new Address(
                    userDataMap.get("country"),
                    userDataMap.get("voivodeship"),
                    userDataMap.get("town"),
                    userDataMap.get("street"),
                    userDataMap.get("houseNumber")
            );

            Customer customer = new Customer(
                    userDataMap.get("firstName"),
                    userDataMap.get("lastName"),
                    userDataMap.get("pesl"),
                    userDataMap.get("cardNumber"), address);

            car.setCustomer(customer);
            MainViewController.selectedCar = null;
            return car;
        }
        return null;
    }

    /**
     * metoda pozwalająca na sortowanie samochodów rosnąco/malejąco z podziałem na typ
     * @param carList lista samochodów na której odbędzie się sortowanie
     * @param type typ sortowania (brand, price, manufacture year, mileage)
     * @param value wartość sortowania ( rosnąco / malejąco )
     * @return
     * w przypadku gdy type lub value jest null lub isEmpty zwracamy pustą listę
     * jeśli type ma wartość "brand" a value "ascending" zostanie zwrócona lista posortowania rosnąco po marce samochodu
     * itd.
     */
    public List<Car> sortedCars(List<Car> carList, String type, String value) {
        if (type == null || type.isEmpty() || value == null || value.isEmpty()) return Collections.emptyList();

        Comparator<Car> carComparator;
        switch (type.toLowerCase(Locale.ROOT)) {
            case "marka":
                carComparator = new Comparator<Car>() {
                    @Override
                    public int compare(Car o1, Car o2) {
                        return o1.getBrand().compareTo(o2.getBrand());
                    }
                };

                carList.sort(carComparator);
                if (value.toLowerCase(Locale.ROOT).equals("malejąco")) Collections.reverse(carList);
                break;
            case "cena":
                carComparator = new Comparator<Car>() {
                    @Override
                    public int compare(Car o1, Car o2) {
                        return Double.compare(o1.getPrice(), o2.getPrice());
                    }
                };

                carList.sort(carComparator);
                if (value.toLowerCase(Locale.ROOT).equals("malejąco")) Collections.reverse(carList);
                break;
            case "rok produkcji":
                carComparator = new Comparator<Car>() {
                    @Override
                    public int compare(Car o1, Car o2) {
                        return Integer.compare(o1.getManufactureYear(), o2.getManufactureYear());
                    }
                };

                carList.sort(carComparator);
                if (value.toLowerCase(Locale.ROOT).equals("malejąco")) Collections.reverse(carList);
                break;
            case "przebieg":
                carComparator = new Comparator<Car>() {
                    @Override
                    public int compare(Car o1, Car o2) {
                        return Integer.compare(o1.getMileage(), o2.getMileage());
                    }
                };

                carList.sort(carComparator);
                if (value.toLowerCase(Locale.ROOT).equals("malejąco")) Collections.reverse(carList);
                break;
        }
        return carList;
    }

    /**
     * metoda pozwalajaca na walidacje mapy
     * @param dataMap mapa zawierajaca dane wejsciowe
     * @return w przypadku gdy pole jest null lub isEmpty zwracamy false i konczymy sprawdzania. Wszystkie pola muszą być != null oraz nie moga byc puste
     */
    public boolean isFieldsValid(HashMap<String, String> dataMap) {
        for (Map.Entry<String, String> e : dataMap.entrySet()) {
            if (e.getValue() == null || e.getValue().isEmpty()) return false;
        }
        return true;
    }

    /**
     * metoda sprawdajaca poprawnosc roku produkcji ktory powinien byc z przedzialu 1870 - 2021
     * @param manufactureYear dane podlegajaca sprawdzeniu
     * @return w przypadku gdy manufactureYear jest pomiedzy 1870 a 2021 zwracamy true, w przeciwnym razie false
     */
    public boolean isManufactureYearValid(int manufactureYear) {
        return manufactureYear >= 1870 && manufactureYear <= 2021;
    }

    /**
     * metoda pozwalająca na sprawdzenie czy mileage jest większe od 0
     * @param mileAge dane podlegające sprawdzeniu
     * @return w przypadku gdy mileage jest wieksze od 0 zwracamy true, w przeciwnym razie false
     */
    public boolean isMileageValid(int mileAge) {
        return mileAge > 0;
    }

    /**
     * metoda pozwalajaca na sprawdzenie czy price jest wiesze od 0
     * @param price dane podlegajace sprawdzeniu
     * @return w przypadku gdy price jest wieksze od 0 zwracamy true, w przeciwnym razie false
     */
    public boolean isPriceValid(double price) {
        return price > 0;
    }

    /**
     * metoda powalajaca na sprawdzenie poprawnosci numeru vin
     * @param vin dane podlegajace sprawdzeniu
     * @return dostajemy true w przypadku gdy vin posiada znaki lub cyfry o dlugości 17 znaków z zakresu a-Z 0-9 z pominieciem i,I,o,O,q,Q
     */
    public boolean isVinValid(String vin) {
        Pattern pattern = Pattern.compile("[a-hj-npr-zA-HJ-NPR-Z0-9]{17}");
        return pattern.matcher(vin).matches();
    }
}
