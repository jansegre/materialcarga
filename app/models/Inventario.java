package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Inventario extends Model {

	@ManyToOne
	public Classe classe;

	@Required
	@ManyToOne
	public User usuario;

	@ManyToMany
	public List<Categoria> categorias;

	@ManyToOne
	public Simatex simatex;

	@ManyToOne
	public Secao secao;
	
	@OneToMany(mappedBy="inventario", cascade=CascadeType.ALL)
	public List<Codigo> codigos;

	public Integer contacontabil;

	public Integer fichageral;

	@Required
	public String material;

	public Integer NBa;

	@Required
	public Integer entrada;

	public Integer saida;

	public Integer existencia;

	@Required
	public Double valunit;

	public Double valtotal;

	@MaxSize(2000)
	public String observacoes;

	public String NI;

	public static List<Inventario> findByCategoria(String slug) {
		return Inventario
				.find("select distinct i from Inventario i join i.categoria as c where c.slug = ?",
						slug).fetch();
	}

	public String toString() {
		return material;
	}
}