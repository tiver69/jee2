package com.parking.beans;

import com.parking.dao.DaoFactory;
import com.parking.dao.UserDao;
import com.parking.entity.Role;
import com.parking.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@ManagedBean(name = "securityBean")
@SessionScoped
@Getter
@Setter
public class SecurityBean {

    private String login;
    private String password;
    private User user;

    public String signIn() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try (UserDao userDao = DaoFactory.getInstance().getUserDao()) {
            Optional<User> tempUser = userDao.getByLogin(login);
            if (tempUser.isPresent() && tempUser.get().getPassword().equals(DigestUtils.md5Hex(password)))
                this.user = tempUser.get();
            else
                return "signIn";
        }
        System.out.println("Authentication success!");

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSession(false);
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        user.setPassword("");
        sessionMap.put("User", user);

        if (user.getRoles().contains(Role.ADMIN))
            return "admin/carList?faces-redirect=true";
        else
            return "user/carList?faces-redirect=true";
    }

    public String logout() {
        this.user = null;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        ((HttpSession) context.getExternalContext().getSession(false)).invalidate();
        return "/signIn?faces-redirect=true";
    }

    public boolean isUserAdmin(){
        return user.getRoles().contains(Role.ADMIN);
    }
}
