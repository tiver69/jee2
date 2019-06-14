import org.junit.*;

import static org.hamcrest.CoreMatchers.*;
import javax.persistence.RollbackException;
import java.util.List;

@Ignore
public class CarDAOTest {

    private static CarDAO carDAO;
    private static Car carOne, carTwo, carThree;
    private static int bdSize;

    @BeforeClass
    public static void setUpClass(){
        carDAO = CarDAO.getCarDAO();
        carOne = new Car(1,"TestCar", "First");
        carTwo = new Car(2,"TestCar", "Second");
        carThree = new Car(3,"TestCar", "Third");
        bdSize = carDAO.getAll().size();
        carDAO.insert(carOne);
        carDAO.insert(carTwo);
        carDAO.insert(carThree);
        carDAO = CarDAO.getCarDAO();
    }

    @AfterClass
    public static void tearDownClass(){
        carDAO.remove(1);
        carDAO.remove(2);
        carDAO.remove(3);
        carDAO.close();
    }


    @Test
    public void getAll() {
        List<Car> carList = carDAO.getAll();
        Assert.assertThat("Current size must be 3 more", carList.size(),equalTo(bdSize + 3));
    }

    @Test
    public void selectById() {
        Car selectCar = carDAO.selectById(carOne.getId());
        Assert.assertThat("Selected must be equal", selectCar, equalTo(carOne));
    }

    @Test
    public void insert() {
        Car insertCar = new Car(5, "TestCarInsert", "Forth");
        carDAO.insert(insertCar);
        Car getInsertCar = carDAO.selectById(insertCar.getId());
        Assert.assertThat("Get&Insert must be equals", insertCar, equalTo(getInsertCar));
        carDAO.remove(insertCar.getId());
    }

    @Test(expected = RollbackException.class)
    public void insertShouldThrowException() {
        carDAO.insert(carTwo);
    }

    @Test
    public void remove() {
        Car removeCar = new Car(6, "TestCarRemove", "Sixth");
        carDAO.insert(removeCar);
        carDAO.remove(removeCar.getId());
        int currDBSize = carDAO.getAll().size();
        Assert.assertEquals(bdSize+3, currDBSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeShouldThrowException() {
        carDAO.remove(4);
    }
}