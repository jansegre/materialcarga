package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;
import play.mvc.Before;

@Entity
public class Inventario extends Model {
	
	@Required
	public Date criadoEm;
	
	@Required
	public Date modificadoEm;
	
	@Required
	public String material;

	@ManyToOne
	public Classe classe;

	@Required
	@ManyToOne
	public User usuario;

	@ManyToMany
	public List<Categoria> categorias;

	@ManyToOne
	public Simatex simatex;
	
	@OneToMany(mappedBy="inventario", cascade=CascadeType.ALL)
	public List<Codigo> codigos;

	public Integer contacontabil;

	public Integer fichageral;
	
	public Date incluidoEm;

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
	
	@Before
	void modificado() {
		this.modificadoEm = new Date();
	}
	
	public Inventario(User usuario, String material, Integer entrada, Double valunit) {
		this.criadoEm = new Date();
		this.modificadoEm = new Date();
		this.usuario = usuario;
		this.material = material;
		this.entrada = entrada;
		this.valunit = valunit;
	}
	
	public Secao getSecao() {
		return usuario.secao;
	}
	
	public Codigo addCodigo(String codigo) {
        Codigo newCodigo = new Codigo(this, codigo);
        this.codigos.add(newCodigo);
        this.save();
        return newCodigo;
    }

	public static List<Inventario> findByCategoria(String slug) {
		return Inventario
				.find("select distinct i from Inventario i join i.categoria as c where c.slug = ?",
						slug).fetch();
	}

	public String toString() {
		return material;
	}
}