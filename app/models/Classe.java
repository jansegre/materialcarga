package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Classe extends Model {

	public String nome;

	@Required
	public Integer codigo;

	public String toString() {
		return nome;
	}

}