package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Secao extends Model {
	
	@Required
	public String nome;

	public String descricao;

	public String toString() {
		return nome;
	}
}
