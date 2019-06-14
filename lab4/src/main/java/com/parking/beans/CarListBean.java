package com.parking.beans;

import com.parking.dao.CarDao;
import com.parking.dao.DaoFactory;
import com.parking.entity.Car;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.List;

@ManagedBean(name="carListBean")
@RequestScoped
public class CarListBean {

    @PostConstruct
    public void init() {
        System.out.println("CREATE NEW CARLIST BEAN");
    }

    public List<Car> getAllCars(){
        try (CarDao carDao = DaoFactory.getInstance().getCarDao()) {
            return carDao.getAll();
        }
    }
}
