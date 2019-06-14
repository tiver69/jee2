public class Car {
    private int id;
    private String brand, model;

    public Car(){}

    public Car(int id, String brand, String model){
        this.brand = brand;
        this.model = model;
        this.id = id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass()!=obj.getClass())
            return false;
        Car compObj = (Car)obj;
        return ((compObj.getId() == id) && compObj.getModel().equals(model) &&
                compObj.getBrand().equals(brand));
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return brand + " " + model + " - " + id;
    }
}
