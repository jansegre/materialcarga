package controllers;

import play.*;
import play.modules.paginate.*;
import play.modules.search.*;
import play.mvc.*;
import play.data.validation.*;
import play.i18n.Messages;

import java.util.*;

import models.*;

@With(Secure.class)
public class Application extends Controller {

	static final int pageSize = 13;

	@Before(unless = { "password", "changePassword" })
	static void checkchpw() {
		User user = Security.getConnectedUser();
		if (user != null && user.changePassword) {
			params.flash();
			flash.put("warn", Messages.get("password.change"));
			password();
		}
	}

	public static void about() {
		render();
	}

	public static void index() {
		Date now = new Date();
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		ModelPaginator<Inventario> inventarios = new ModelPaginator(
				Inventario.class).orderBy("criadoEm desc");
		inventarios.setPageSize(pageSize);
		render(now, user, categorias, inventarios);
	}

	public static void cat(String slug) {
		Date now = new Date();
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		List<Inventario> invents = Inventario.findByCategoria(slug);
		ValuePaginator inventarios = new ValuePaginator(invents);
		inventarios.setPageSize(pageSize);
		Categoria categoria = Categoria.findBySlug(slug);
		render("@index", now, user, categorias, inventarios, categoria);
	}

	public static void search(String q) {
		User user = Security.getConnectedUser();
		List<Inventario> invents = Search.search(q, Inventario.class).reverse()
				.fetch();
		List<Categoria> categorias = Categoria.findAll();
		ValuePaginator inventarios = new ValuePaginator(invents);
		inventarios.setPageSize(pageSize);
		String search = q;
		render("@index", user, categorias, inventarios, search);
	}

	public static void delete(Long id) {
		// TODO
	}

	public static void transfer(Long id) {
		// TODO
	}

	public static void unload(Long id) {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		render(user, categorias);
	}

	public static void edit(Long id) {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		Inventario inventario = Inventario.findById(id);
		render(user, categorias, inventario);
	}

	public static void editInventario(@Valid Inventario inventario) {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		if (validation.hasErrors()) {
			if (request.isAjax())
				error(Messages.get("edit.error"));
			// params.flash(); // add http parameters to the flash scope
			// flash.error(Messages.get("add.error"));
			String error = Messages.get("add.error");
			render("@edit", user, categorias, inventario, error);
		} else {
			if (inventario.classe != null)
				inventario.classe.save();
			if (inventario.simatex != null) {
				if (inventario.simatex.empresa != null)
					inventario.simatex.empresa.save();
				inventario.simatex.save();
			}
			inventario.save();
			params.flash();
			flash.success(Messages.get("add.success"));
			if (inventario.categoria != null) {
				cat(inventario.categoria.slug);
			} else {
				index();
			}
		}
	}

	public static void add() {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		render("@edit", user, categorias);
	}

	public static void addLabel(Long id, String codigo) {
		Inventario inventario = Inventario.findById(id);
		Codigo label = inventario.addCodigo(codigo);
		renderJSON(label);
	}

	public static void password() {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		render(user, categorias);
	}

	public static void changePassword(@Required String current,
			@Required @MinSize(5) String password,
			@Equals("password") String password2) {
		if (validation.hasErrors()) {
			validation.keep();
			params.flash();
			flash.error(Messages.get("password.error"));
			password();
		} else {
			User user = Security.getConnectedUser();
			if (user.checkPassword(current)) {
				user.passwordHash = password;
				user.changePassword = false;
				user.save();
				params.flash();
				flash.success(Messages.get("password.success"));
				index();
			} else {
				params.flash();
				flash.error(Messages.get("password.error"));
				password();
			}
		}
	}
}