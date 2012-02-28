package controllers;

import play.*;
import play.modules.paginate.*;
import play.modules.search.*;
import play.mvc.*;
import play.data.binding.Binder;
import play.data.validation.*;
import play.i18n.Messages;

import java.util.*;

import models.*;

@With(Secure.class)
public class Home extends Controller {

	static final int pageSize = 13;

	@Before(unless = { "password", "changePassword" })
	static void checkchpw() {
		User user = Security.getConnectedUser();
		if (user != null ? user.changePassword : false) {
			params.flash();
			flash.put("warn", Messages.get("password.change"));
			password();
		}
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

	public static void showCategoria(String slug) {
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
		Date now = new Date();
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		List<Inventario> invents;
		if (q == null || q.equals("")) {
			invents = Inventario.findAll();
			ValuePaginator inventarios = new ValuePaginator(invents);
			inventarios.setPageSize(pageSize);
			String search = "";
			render("@index", now, user, categorias, inventarios, search);
		} else {
			invents = Search.search(q, Inventario.class).reverse().fetch();
			ValuePaginator inventarios = new ValuePaginator(invents);
			inventarios.setPageSize(pageSize);
			String search = q;
			render("@index", now, user, categorias, inventarios, search);
		}
	}

	public static void delInventario(Long id) {
		Inventario inventario = Inventario.findById(id);
		Categoria cat = inventario.categoria;
		inventario.delete();
		params.flash();
		flash.success(Messages.get("del.success"));
		if (cat != null) {
			showCategoria(cat.slug);
		} else {
			index();
		}
	}

	public static void transfer(Long id) {
		// TODO
	}

	public static void unload(Long id) {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		render(user, categorias);
	}

	public static void show(Long id) {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		Inventario inventario = Inventario.findById(id);
		render(user, categorias, inventario);
	}

	static void saveAndValidate(Inventario inventario) {
		validation.valid(inventario);
		if (inventario.simatex != null) {
			if (inventario.simatex.nome == null
					|| inventario.simatex.nome.isEmpty())
				inventario.simatex = null;
			else
				validation.valid(inventario.simatex);
		}
		if (inventario.empresa != null) {
			if (inventario.empresa.nome == null
					|| inventario.empresa.nome.isEmpty())
				inventario.empresa = null;
			else
				validation.valid(inventario.empresa);
		}
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		if (validation.hasErrors()) {
			if (request.isAjax())
				error(Messages.get("save.error"));
			String error = Messages.get("save.error");
			render("@show", user, categorias, inventario, error);
		} else {
			if (inventario.simatex != null)
				inventario.simatex.save();
			if (inventario.empresa != null)
				inventario.empresa.save();
			if (inventario.classe != null) {
				if (inventario.classe.id != null) {
					inventario.classe = Classe.findById(inventario.classe.id);
				} else if (inventario.classe.codigo != null) {
					Classe classe = Classe.find("codigo",
							inventario.classe.codigo).first();
					if (classe != null)
						inventario.classe = classe;
					else
						inventario.classe.save();
				} else {
					inventario.classe = null;
				}
			}
			inventario.autoSetSaida();
			inventario.autoSetExistencia();
			inventario.autoSetValtotal();
			inventario.save();
			params.flash();
			flash.success(Messages.get("save.success"));
			if (inventario.categoria != null) {
				showCategoria(inventario.categoria.slug);
			} else {
				index();
			}
		}
	}

	public static void saveInventario(Long id) {
		Inventario inventario = Inventario.findById(id);
		notFoundIfNull(inventario);
		Binder.bind(inventario, "inventario", params.all());
		saveAndValidate(inventario);
	}

	public static void addInventario(Inventario inventario) {
		saveAndValidate(inventario);
	}

	public static void newCategoria(String cat) {
		Categoria ncat = new Categoria(cat);
		Categoria ccat = Categoria.findBySlug(ncat.slug);
		if (ccat != null || ncat.slug == null || ncat.slug.isEmpty()
				|| cat == null || cat.isEmpty()) {
			params.flash();
			flash.error(Messages.get("save.cat.error"));
			index();
		} else {
			ncat.save();
			showCategoria(ncat.slug);
		}
	}

	public static void add() {
		User user = Security.getConnectedUser();
		List<Categoria> categorias = Categoria.findAll();
		render("@show", user, categorias);
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
		User user = Security.getConnectedUser();
		if (validation.hasErrors()) {
			validation.keep();
			params.flash();
			flash.error(Messages.get("password.error"));
			password();
		} else {

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