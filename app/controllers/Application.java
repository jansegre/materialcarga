package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.i18n.Messages;

import java.util.*;

import models.*;

@With(Secure.class)
public class Application extends Controller {
	
	@Before(unless={"password", "changePassword"})
	static void checkchpw() {
		User user = Security.getConnectedUser(); 
		if(user != null && user.changePassword) {
			params.flash();
			flash.error(Messages.get("password.change"));
			password();
		}
	}
	
	public static void about() {
		render();
	}

	public static void index() {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		render(user, categorias);
	}
	
	public static void add() {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		render(user, categorias);
	}
	
	public static void password() {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		render(user, categorias);
	}
	
	public static void changePassword(@Required @MinSize(5) String password, @Equals("password") String password2) {
		if (validation.hasErrors()) {
            validation.keep();
            params.flash();
            flash.error(Messages.get("password.error"));
            password();
        } else {
        	User user = Security.getConnectedUser();
        	user.passwordHash = password;
        	user.changePassword = false;
        	user.save();
        	password();
        }
	}
	
	public static void cat(String slug) {
		List<Inventario> inventarios = Inventario.findByCategoria(slug); 
		render(inventarios);
	}

}