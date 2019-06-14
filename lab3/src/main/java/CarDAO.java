import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class CarDAO {

    private static CarDAO carDAO;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    private CarDAO(){
        entityManagerFactory= Persistence.createEntityManagerFactory("NewPersistenceUnit");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    public static CarDAO getCarDAO() {
        if (carDAO == null)
            carDAO = new CarDAO();
        return carDAO;
    }

    public List<Car> getAll() {
        List<Car> listOfCar = entityManager.createQuery("SELECT a FROM Car a ").getResultList();
        return listOfCar;
    }

    public Car selectById(int id) {
        entityManager.getTransaction().begin();
        Car car = entityManager.find(Car.class, id);
        entityManager.getTransaction().commit();
        return car;
    }

    public void insert(Car car){
        entityManager.getTransaction().begin();
        entityManager.persist(car);
        entityManager.getTransaction().commit();
    }

    public void remove(int id ) {
        try {
            entityManager.getTransaction().begin();
            Car carRemove = entityManager.find(Car.class, id);
            entityManager.remove(carRemove);
            entityManager.getTransaction().commit();
        } catch (IllegalArgumentException e){
            entityManager.getTransaction().commit();
            throw e;
        }
//        Do not throw any exceptions if nothing to delete
//        entityManager.getTransaction().begin();
//        entityManager.createQuery("delete from Car p where  p.id=:id")
//                .setParameter( "id",id)
//                .executeUpdate();
//        entityManager.getTransaction().commit();
    }

    public void update(int id, Car newCar){
        Car previousCar = selectById(id);
        previousCar.setBrand(newCar.getBrand());
        previousCar.setModel(newCar.getModel());
        entityManager.getTransaction().begin();
        entityManager.merge(previousCar);
        entityManager.getTransaction().commit();
    }

    public void close(){
        entityManager.close();
        carDAO = null;
    }
}
