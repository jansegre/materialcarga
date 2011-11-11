package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Empresa extends Model {

	@Required
	public String nome;

	public String razaosocial;

	public String cnpj;

	public String endereco;

	public String telefone;

	public String atividades;

	public String cage;

	public String toString() {
		return nome;
	}
}