package sample.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import sample.model.Address;
import sample.model.Car;
import sample.model.Customer;
import sample.model.FuelType;

import java.util.ArrayList;
import java.util.List;

/**
 * klasa odpowiedzialna za komunikacje z bazą danych
 */
public class DataBaseService {

    /**
     * metoda pozwalajaca na nawiazanie polaczenia z baza danych
     * @return zracamy obiekt bazy o nazwie carService
     */
    private MongoDatabase connect() {
        MongoClient client = MongoClients.create();
        return client.getDatabase("carService");
    }

    /**
     * metoda pozwalająca na usuniecie bazy carService
     */
    public void dropDB() {
        MongoDatabase db = connect();
        db.drop();
    }

    /**
     * metoda pozwalajaca na wyszukanie wszystkich samochodow
     * @return zwraca liste samochodo znajdujacych sie w dazie danych
     */
    public List<Car> findAll() {
        MongoDatabase db = connect();
        MongoCollection<Document> cars = db.getCollection("cars");

        FindIterable<Document> result = cars.find();
        List<Car> carList = new ArrayList<>();
        for (Document document : result) {
            carList.add(convertToCar(document));
        }
        return carList;
    }

    /**
     * metoda pozwalajaca na znalezienie samochodu po jego id
     * @param id dane po ktorych szukamy
     * @return zwracamy obiekt typu Car
     */
    public Car findById(String id) {
        MongoDatabase db = connect();
        MongoCollection<Document> cars = db.getCollection("cars");

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document document = cars.find(query).first();
        return convertToCar(document);
    }

    /**
     * metoda pozwalajaca na zamiane danych z typu Document ( znajdujacgo sie w bazie danych ) na typ Car ( ktory funkcjonuje w aplikacji )
     * @param document obiekt posiadajacy dane samochodu
     * @return zwracamu obiekt typu Car
     */
    private Car convertToCar(Document document) {
        Car car = new Car();
        car.setId(document.get("_id").toString());
        car.setBrand(document.getString("brand"));
        car.setModel(document.getString("model"));
        car.setBodyType(document.getString("bodyType"));
        car.setEngineCapacity(document.getInteger("engineCapacity"));
        car.setManufactureYear(document.getInteger("manufactureYear"));
        car.setMileage(document.getInteger("mileage"));
        car.setPrice(document.getDouble("price"));
        car.setGearboxType(document.getString("gearboxType"));
        car.setVinNumber(document.getString("vinNumber"));
        car.setFuelType(convertFuelType(document.getInteger("fuelType")));
        car.setColor(document.getString("color"));
        car.setCountry(document.getString("country"));

        Document customer = (Document) document.get("customer");
        if (customer != null)
            car.setCustomer(convertToCustomer(customer));
        return car;
    }

    /**
     * metoda pozwalajaca na zamiane danych z typu Document ( znajdujacgo sie w bazie danych ) na typ Customer ( ktory funkcjonuje w aplikacji )
     * @param document obiekt posisdajacy dane klienta
     * @return zwracamy obiekt typu Customer
     */
    private Customer convertToCustomer(Document document) {
        Customer customer = new Customer();
        customer.setFirstName(document.getString("firstName"));
        customer.setLastName(document.getString("lastName"));
        customer.setPesel(document.getString("pesel"));
        customer.setCardNumber(document.getString("cardNumber"));

        Document address = (Document) document.get("address");
        customer.setAddress(convertToAddress(address));
        return customer;
    }

    /**
     * metoda pozwalajaca na zamiane danych z typu Document ( znajdujacgo sie w bazie danych ) na typ Address ( ktory funkcjonuje w aplikacji )
     * @param document obiekt posiadajacy dane adresu
     * @return zwracamy obiekt typu adres
     */
    private Address convertToAddress(Document document) {
        Address address = new Address();
        address.setCountry(document.getString("country"));
        address.setVoivodeship(document.getString("voivodeship"));
        address.setTown(document.getString("town"));
        address.setStreet(document.getString("street"));
        address.setHouseNumber(document.getString("houseNumber"));
        return address;
    }

    /**
     * metoda pozwalajaca na zamiane danych z typu int ( znajdujacgo sie w bazie danych ) na typ FuelType ( ktory funkcjonuje w aplikacji )
     * @param fuelType obiekt posiadajacy dane adresu
     * @return zwracamy obiekt typu FuelType
     */
    private FuelType convertFuelType(Integer fuelType) {
        switch (fuelType) {
            case 0:
                return FuelType.DIESEL;
            case 1:
                return FuelType.PETROL;
            case 2:
                return FuelType.PETROL_GAS;
            default:
                return null;
        }
    }

    /**
     * metoda pozwalajaca na dodanie Car do bazy danych
     * @param car obiekt ktory ma zostac zapisany w bazie
     */
    public void insertCar(Car car) {
        MongoDatabase db = connect();
        MongoCollection<Document> cars = db.getCollection("cars");

        Document carToSave = convertToCarData(car);
        cars.insertOne(carToSave);
    }

    /**
     * metoda pozwalajaca na aktualizacje istniejacego juz obiektu Car
     * @param car obiekt ktory ma zostac zaktualizowany
     */
    public void update(Car car) {
        MongoDatabase db = connect();
        MongoCollection<Document> cars = db.getCollection("cars");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", new ObjectId(car.getId()));

        Document updateData = convertToCarData(car);

        Document updateQuery = new Document();
        updateQuery.append("$set", updateData);

        cars.updateOne(searchQuery, updateQuery);
    }

    /**
     * metoda pozwalajaca na usuniecie typu Car z bazy danych
     * @param car obiekt ktory zostanie usuniety
     */
    public void remove(Car car) {
        MongoDatabase db = connect();
        MongoCollection<Document> cars = db.getCollection("cars");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", new ObjectId(car.getId()));

        cars.deleteOne(searchQuery);
    }

    /**
     * metoda pozwalajaca zaminic typ Car na typ Document ktory mozemy zapisac w bazie danych, jesli customer jest != null zostaje doklejony do dokumentu
     * @param car obiekt ktory zostanie zamieniony na typ Document
     * @return Document - typ ktory mozemy zapisac w bazie Mongo
     */
    private Document convertToCarData(Car car) {
        Document carData = new Document("brand", car.getBrand())
                .append("model", car.getModel())
                .append("bodyType", car.getBodyType())
                .append("engineCapacity", car.getEngineCapacity())
                .append("manufactureYear", car.getManufactureYear())
                .append("mileage", car.getMileage())
                .append("price", car.getPrice())
                .append("gearboxType", car.getGearboxType())
                .append("vinNumber", car.getVinNumber())
                .append("fuelType", car.getFuelType().ordinal())
                .append("color", car.getColor())
                .append("country", car.getCountry());

        if (car.getCustomer() != null) {
            Document customerData = convertToCustomerData(car.getCustomer());
            carData.append("customer", customerData);
        }
        return carData;
    }

    /**
     * metoda pozwalajaca zaminic typ Customer na typ Document ktory mozemy zapisac w bazie danych
     * @param customer obiekt ktory zostanie zamieniony na typ Document
     * @return Document - typ ktory mozemy zapisac w bazie Mongo
     */
    private Document convertToCustomerData(Customer customer) {
        Document customerData = new Document("firstName", customer.getFirstName())
                .append("lastName", customer.getLastName())
                .append("pesel", customer.getPesel())
                .append("cardNumber", customer.getCardNumber());

        Document addressData = convertToAddressData(customer.getAddress());
        customerData.append("address", addressData);
        return customerData;
    }

    /**
     * metoda pozwalajaca zaminic typ Address na typ Document ktory mozemy zapisac w bazie danych
     * @param address obiekt ktory zostanie zamieniony na typ Document
     * @return Document - typ ktory mozemy zapisac w bazie Mongo
     */
    private Document convertToAddressData(Address address) {
        return new Document("country", address.getCountry())
                .append("voivodeship", address.getVoivodeship())
                .append("town", address.getTown())
                .append("street", address.getStreet())
                .append("houseNumber", address.getHouseNumber());
    }
}
