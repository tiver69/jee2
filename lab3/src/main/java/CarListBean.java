import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean(name="carListBean")
@RequestScoped
public class CarListBean {
    private CarDAO dataBaseSource = CarDAO.getCarDAO();
    private List<Car> carList;

    @PostConstruct
    public void init() {
        System.out.println("CREATE NEW CARLIST BEAN");
    }

    public List<Car> carList(){
        carList = dataBaseSource.getAll();
        return carList;
    }
}
