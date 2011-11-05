package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;
import play.i18n.Messages;

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
	
	public Codigo(Inventario inventario, String codigo) {
		this.inventario = inventario;
		this.codigo = codigo;
		this.destino = "";
		this.observacoes = "";
		this.estado = Messages.get("codigo.novo");
	}

	public String toString() {
		return codigo;
	}
}