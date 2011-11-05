package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.i18n.Messages;

import java.util.*;

import models.*;

@With(Secure.class)
public class Application extends Controller {
	
	@Before(unless={"changePassword"})
	static void checkchpw() {
		User user = Security.getConnectedUser(); 
		if(user != null && user.changePassword) {
			params.flash();
			flash.error(Messages.get("password.change"));
			index();
		}
	}
	
	public static void about() {
		render();
	}

	public static void index() {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		String action = user.changePassword? "password" : "home";
		render(user, categorias, action);
	}
	
	public static void addLabel(String codigo) {
		Inventario inventario = Inventario.find("pronto", false).first();
		Codigo label = inventario.addCodigo(codigo);
		renderJSON(label);
	}
	
	public static void changePassword(@Required String current, @Required @MinSize(5) String password, @Equals("password") String password2) {
		if (validation.hasErrors()) {
            validation.keep();
            params.flash();
            flash.error(Messages.get("password.error"));
            index();
        } else {
        	//TODO: verificação da senha atual
        	User user = Security.getConnectedUser();
        	user.passwordHash = password;
        	user.changePassword = false;
        	user.save();
        	params.flash();
            flash.success(Messages.get("password.success"));
        	index();
        }
	}
	
	//Página
	public static void cat(String slug) {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		List<Inventario> inventarios = Inventario.findByCategoria(slug); 
		render(user, categorias, inventarios);
	}

}