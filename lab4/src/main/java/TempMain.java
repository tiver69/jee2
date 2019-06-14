import com.parking.beans.UserBean;
import com.parking.dao.DaoFactory;
import com.parking.dao.CarDao;
import com.parking.dao.UserDao;
import com.parking.entity.Car;
import com.parking.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TempMain {

    public static void main(String[] args) {

//        try (CarDao carDao = DaoFactory.getInstance().getCarDao()) {
//            System.out.println(carDao.getAll());
//        }
//
//        try (UserDao userDao = DaoFactory.getInstance().getUserDao()) {
//            System.out.println(userDao.getById(1L));
//        }

        User user = new User();
        try (UserDao userDao = DaoFactory.getInstance().getUserDao()) {
            if (userDao.getByLogin("tiver69").isPresent()) {
                user = userDao.getByLogin("tiver69").get();
            }
        }
        System.out.println(user);


//        try(CarDao carDao = DaoFactory.getInstance().getCarDao()) {
//            Car insertCar = new Car();
//            insertCar.setId(1234L);
//            insertCar.setBrand("lala");
//            insertCar.setModel("tralala");
//            insertCar.setUser(user);
//            System.out.println(insertCar);
//            carDao.create(insertCar);
//        }

    }
}
