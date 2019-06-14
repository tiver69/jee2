package com.parking.beans;

import com.parking.dao.DaoFactory;
import com.parking.dao.UserDao;
import com.parking.entity.Car;
import com.parking.entity.User;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@ManagedBean(name = "userBean")
@RequestScoped
public class UserBean {

    @PostConstruct
    public void init() {
        System.out.println("CREATE NEW USER BEAN");
    }

    public List<Car> getAllUserCars() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        User currentUser = (User)
                ((HttpSession) externalContext.getSession(false))
                        .getAttribute("User");
        long userId = currentUser.getId();


        try (UserDao userDao = DaoFactory.getInstance().getUserDao()) {
            if (userDao.getById(userId).isPresent()) {
                User user = userDao.getById(userId).get();
                return user.getCars();
            }
        }
        return new ArrayList<>();
    }

    public List<String> getAllUserLogins() {
        List<User> users;
        List <String> userLogins = new ArrayList<>();
        try (UserDao userDao = DaoFactory.getInstance().getUserDao()) {
            users = userDao.getAll();
        }
        users.forEach(user -> userLogins.add(user.getLogin()));
        return userLogins;
    }
}

