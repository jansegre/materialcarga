package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Simatex extends Model {

	@Required
	public String nome;
	
	public String codom;

	public String nrref;

	public String pais;

	public String unidade;

	public String moeda;

	public String NEENSN;

	public String categoria;

	public String modalidade;

	public String duracao;

	public String dadoscomp;

	public String caractec;

	public String toString() {
		return nome;
	}
}