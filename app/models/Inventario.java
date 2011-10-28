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
	public Usuario usuario;
	
	@ManyToOne
	public Categoria categoria;

	@ManyToOne
	public Simatex simatex;
	
	@ManyToOne
	public Secao secao;

	public int contacontabil;

	public int fichageral;

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

	public String observacoes;

	public String NI;

	public String toString() {
		return material;
	}
}