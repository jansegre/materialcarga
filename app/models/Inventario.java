package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Inventario extends Model {

	public int contacontabil;

	public int fichageral;

	@ManyToOne
	public Classe classe;

	@Required
	public String material;

	public int nba;

	@Required
	public int entrada;

	public int saida;

	public int existencia;

	@Required
	public float valunit;

	public float valtotal;

	@ManyToMany
	public Secao secao;

	public String observacoes;

	public String NI;

	@Required
	@ManyToOne
	public Usuario usuario;

	@ManyToOne
	public Categoria categoria;

	@ManyToOne
	public Simatex simatex;

	public String toString() {
		return observacoes;
	}
}