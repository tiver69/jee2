import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="carBean")
@RequestScoped
public class CarBean {
    private CarDAO dataBaseSource = CarDAO.getCarDAO();
    private int id;
    private String brand;
    private String model;

    private boolean saveFlag = false;

    @PostConstruct
    public void init() {
        System.out.println("CREATE NEW CAR BEAN");
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public boolean getSaveFlag() {
        return saveFlag;
    }
public void setSaveFlag(boolean saveFlag) {
        this.saveFlag = saveFlag;
    }

    public String saveCar() {
        System.out.println("ADD CAR " + id + " " + brand + " " + model);
        Car insertCar = new Car(id,brand,model);
        if ((saveFlag = dataBaseSource.selectById(id) != null) || validation()) {
            return "400?faces-redirect=true&includeViewParams=true";
        }
        dataBaseSource.insert(insertCar);
        return "carList";
    }

    public String toUpdatePage(int id){
        this.id = id;
        Car newCar = dataBaseSource.selectById(id);
        this.model = newCar.getModel();
        this.brand = newCar.getBrand();

        System.out.println("FORWARD TO UPDATE PAGE");
        return "updateCar?faces-redirect=true&includeViewParams=true";
    }

    public String updateCar(int id){
        if (validation() || dataBaseSource.selectById(id) == null ) {
            return "400?faces-redirect=true&includeViewParams=true";
        }
        System.out.println("UPDATE CAR " + id + " "+ brand + " " + model);
        Car updateCar = new Car(id,brand,model);
        dataBaseSource.update(id, updateCar);
        return "carList?faces-redirect=true";
    }

    public void deleteCar(int id){
        System.out.println("REMOVE CAR " + id + " "+ dataBaseSource.selectById(id).getBrand()
            + " " + dataBaseSource.selectById(id).getModel());
        dataBaseSource.remove(id);
    }

    private boolean validation(){
        boolean result = (id == 0 || model.isEmpty()|| brand.isEmpty());
        System.out.println("VALIDATION FAILS: " + result);
        return result;
    }
}

