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

	@Before(unless = { "changePassword" })
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
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		ModelPaginator<Inventario> inventarios = new ModelPaginator(
				Inventario.class).orderBy("modificadoEm desc");
		inventarios.setPageSize(15);
		render(user, categorias, inventarios);
	}

	public static void cat(String slug) {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		List<Inventario> invents = Inventario.findByCategoria(slug);
		ValuePaginator inventarios = new ValuePaginator(invents);
		inventarios.setPageSize(15);
		render("@index", user, categorias, inventarios);
	}

	public static void search(String q) {
		User user = Security.getConnectedUser();
		List<Inventario> invents = Search.search(q, Inventario.class).reverse().fetch();
		List<Categoria> categorias = Categoria.findAll();
		ValuePaginator inventarios = new ValuePaginator(invents);
		inventarios.setPageSize(15);
		String search=q;
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
		Simatex simatex = inventario.simatex;
		Empresa empresa = simatex == null ? null : simatex.empresa;
		List<Codigo> codigos = inventario.codigos;
		render(user, categorias, inventario, simatex, codigos, empresa);
	}

	public static void editInventario(@Valid Inventario inventario) {
		if (validation.hasErrors()) {
			if(request.isAjax()) error(Messages.get("edit.error"));
			params.flash(); // add http parameters to the flash scope
			validation.keep(); // keep the errors for the next request
			params.flash();
			flash.error(Messages.get("add.error"));
			edit(inventario.id);
		} else {
			inventario.save();
			edit(inventario.id);
		}
	}

	public static void add() {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		render(user, categorias);
	}

	public static void addLabel(Long id, String codigo) {
		Inventario inventario = Inventario.findById(id);
		Codigo label = inventario.addCodigo(codigo);
		renderJSON(label);
	}

	public static void addInventario(@Required String material,
			@Required Integer entrada, @Required Double valunit,
			Integer contacontabil, Integer fichageral, Date incluidoem,
			Integer nba, Integer saida, Integer existencia, Double valtotal,
			String observacoes, String ni) {
		if (validation.hasErrors()) {
			validation.keep();
			params.flash();
			flash.error(Messages.get("add.error"));
			add();
		} else {
			User user = Security.getConnectedUser();
			Inventario inventario = new Inventario(user, material, entrada,
					valunit);
			inventario.contacontabil = contacontabil;
			inventario.fichageral = fichageral;
			inventario.incluidoEm = incluidoem;
			inventario.NBa = nba;
			inventario.saida = saida;
			inventario.existencia = existencia;
			inventario.valtotal = valtotal;
			inventario.observacoes = observacoes;
			inventario.NI = ni;
			inventario.save();
			params.flash();
			flash.success(Messages.get("add.success"));
			index();
		}
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