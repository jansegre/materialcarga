package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import java.util.*;

import models.*;

@With(Secure.class)
public class Application extends Controller {
	
	@Before(unless={"conf", "chpw"})
	static void checkchpw() {
		User user = Security.connectedUser(); 
		if(user != null && user.changePassword) {
			conf();
		}
	}

	public static void index() {
		User user = Security.connectedUser();
		List<Categoria> categorias = Categoria.findAll();
		render(user, categorias);
	}
	
	public static void conf() {
		User user = Security.connectedUser();
		render(user);
	}
	
	public static void chpw(@Required @MinSize(5) String password, @Equals("password") String password2) {
		
	}
	
	public static void cat(String slug) {
		List<Inventario> inventarios = Inventario.findByCategoria(slug); 
		render(inventarios);
	}

}