package com.parking.beans;

import com.parking.dao.CarDao;
import com.parking.dao.DaoFactory;
import com.parking.dao.UserDao;
import com.parking.entity.Car;
import com.parking.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.Map;

@ManagedBean(name="carBean")
@RequestScoped
@Getter
@Setter
public class CarBean {
    private long id;
    private String brand;
    private String model;
    private String userLogin;

    private boolean saveFlag = false;

    @PostConstruct
    public void init() {
        System.out.println("CREATE NEW CAR BEAN");
    }

    public String saveCar() {
        System.out.println("ADD CAR " + id + " " + brand + " " + model);

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        User currentUser = (User)
                ((HttpSession) externalContext.getSession(false))
                        .getAttribute("User");
        long userId = currentUser.getId();
        Car insertCar = new Car(id,brand,model,currentUser);

        try(CarDao carDao = DaoFactory.getInstance().getCarDao()) {
//            if ( validation() || (saveFlag = carDao.getById(id).isPresent())) {
//                return "/error/400?faces-redirect=true&includeViewParams=true";
//            }
            System.out.println(insertCar);
            if (carDao.create(insertCar))
                return "/user/carList";
            else return "/error/400?faces-redirect=true&includeViewParams=true";
        }
    }

    public String saveCarAdmin() {
        System.out.println("ADD CAR " + id + " " + brand + " " + model+ " "+ userLogin);

        User forUser;
        try (UserDao userDao = DaoFactory.getInstance().getUserDao()){
            forUser = userDao.getByLogin(userLogin).get();
        }
        Car insertCar = new Car(id,brand,model,forUser);

        try(CarDao carDao = DaoFactory.getInstance().getCarDao()) {
//            if ( validation() || (saveFlag = carDao.getById(id).isPresent())) {
//                return "/error/400?faces-redirect=true&includeViewParams=true";
//            }
            System.out.println(insertCar);
            if (carDao.create(insertCar))
                return "/admin/carList";
            else return "/error/400?faces-redirect=true&includeViewParams=true";
        }
    }

    public String toUpdatePage(int id){
        this.id = id;
        try(CarDao carDao = DaoFactory.getInstance().getCarDao()) {
            Car newCar = carDao.getById(id).get();
            this.model = newCar.getModel();
            this.brand = newCar.getBrand();
        }
        System.out.println("FORWARD TO UPDATE PAGE");
        return "/user/updateCar?faces-redirect=true&includeViewParams=true";
    }

    public String updateCar(int id){
        try(CarDao carDao = DaoFactory.getInstance().getCarDao()) {
            if (validation() || !carDao.getById(id).isPresent()) {
                return "/error/400?faces-redirect=true&includeViewParams=true";
            }
            Car updateCar = carDao.getById(id).get();
            updateCar.setModel(model);
            updateCar.setBrand(brand);
            carDao.update(updateCar);
        }
        System.out.println("UPDATE CAR " + id + " " + brand + " " + model);
        return "/user/carList?faces-redirect=true";
    }

    public void deleteCar(long id){
        System.out.println("REMOVE CAR " + id);
        try(CarDao carDao = DaoFactory.getInstance().getCarDao()) {
            carDao.remove(id);
        }
    }

    private boolean validation(){
        boolean result = (id == 0 || model.isEmpty()|| brand.isEmpty());
        System.out.println("VALIDATION FAILS: " + result);
        return result;
    }
}

