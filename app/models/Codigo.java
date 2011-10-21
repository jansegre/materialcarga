package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Codigo extends Model {
	@Required
	public String codigo;

	@Required
	@ManyToOne
	public Inventario inventario;

	public String destino;

	public String observacoes;

	public String estado;

	public String toString() {
		return codigo;
	}
}