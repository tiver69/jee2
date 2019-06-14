package com.parking.beans;

import com.parking.dao.DaoFactory;
import com.parking.dao.UserDao;
import com.parking.entity.Role;
import com.parking.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import java.util.ArrayList;
import java.util.HashSet;

@ManagedBean(name = "registerBean")
@SessionScoped
@Getter
@Setter
public class RegisterBean {
	private String name;
	private String login;
	private String password;
	private String confirmPassword;

	public void validatePassword(ComponentSystemEvent event) {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIComponent components = event.getComponent();

		// get password
		UIInput uiInputPassword = (UIInput) components.findComponent("password");
		String password = uiInputPassword.getLocalValue() == null ? "" : uiInputPassword.getLocalValue().toString();
		String passwordId = uiInputPassword.getClientId();

		// get confirm password
		UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmpassword");
		String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? ""
				: uiInputConfirmPassword.getLocalValue().toString();

		// Let required="true" do its job.
		if (password.isEmpty() || confirmPassword.isEmpty()) {
			return;
		}

		if (!password.equals(confirmPassword)) {
			FacesMessage msg = new FacesMessage("Confirm password does not match password");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			facesContext.addMessage(passwordId, msg);
			facesContext.renderResponse();
		}

		try(UserDao userDao = DaoFactory.getInstance().getUserDao()) {
			if (userDao.getByLogin(login).isPresent()) {
				FacesMessage msg = new FacesMessage("User with this e-mail already exists");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				facesContext.addMessage(passwordId, msg);
				facesContext.renderResponse();
			}
		}
	}

	public String register() {
		User user = new User(0L, name, login, DigestUtils.md5Hex(password), new ArrayList<>(), new HashSet<>());
		user.getRoles().add(Role.USER);
		try(UserDao userDao = DaoFactory.getInstance().getUserDao()) {
			if (userDao.create(user))
				return "/signIn?faces-redirect=true";
		}
		return "registration";
	}
}
