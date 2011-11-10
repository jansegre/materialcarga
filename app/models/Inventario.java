package models;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;
import play.modules.search.*;
import play.mvc.Before;

@Indexed
@Entity
public class Inventario extends Model {

	public Date criadoEm;

	public Date modificadoEm;

	@PrePersist
	protected void onCreate() {
		criadoEm = new Date();
		modificadoEm = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		modificadoEm = new Date();
	}

	@Field
	@Required
	public String material;

	@ManyToOne
	public Classe classe;

	@Required
	@ManyToOne
	public User usuario;

	@ManyToOne
	public Categoria categoria;

	@ManyToOne
	public Simatex simatex;

	@OneToMany(mappedBy = "inventario", cascade = CascadeType.ALL)
	public List<Codigo> codigos;

	public Integer contacontabil;

	public Integer fichageral;

	public Date incluidoEm;

	public Integer NBa;

	@Field
	@Required
	public Integer entrada;

	public Integer saida;

	public Integer existencia;

	@Field
	@Required
	public Double valunit;

	public void setValunit(String valunit) {
		this.valunit = currencyToDouble(valunit);
	}

	public String getValunit() {
		return doubleToCurrency(valunit);
	}

	@Field
	public Double valtotal;

	public void setValtotal(String valtotal) {
		this.valtotal = currencyToDouble(valtotal);
	}

	public String getValtotal() {
		return doubleToCurrency(valtotal);
	}

	static Double currencyToDouble(String currency) {
		if (currency == null)
			return null;
		try {
			return Double.parseDouble(currencyToBigDecimalFormat(currency));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	static String doubleToCurrency(Double number) {
		if (number == null)
			return null;
		DecimalFormat real = new DecimalFormat("###,###,###,###.00");
		String currency = real.format(number);
		currency = currency.replace(',', ';');
		currency = currency.replace('.', ',');
		currency = currency.replace(';', '.');
		return currency;
	}

	static String currencyToBigDecimalFormat(String currency) throws Exception {
		if (!doesMatch(
				currency,
				"^[+-]?[0-9]{1,3}(?:[0-9]*(?:[.,][0-9]{0,2})?|(?:,[0-9]{3})*(?:\\.[0-9]{0,2})?|(?:\\.[0-9]{3})*(?:,[0-9]{0,2})?)$"))
			throw new Exception("Currency in wrong format " + currency);

		// Replace all dots with commas
		currency = currency.replaceAll("\\.", ",");

		// If fractions exist, the separator must be a .
		if (currency.length() >= 3) {
			char[] chars = currency.toCharArray();
			if (chars[chars.length - 2] == ',') {
				chars[chars.length - 2] = '.';
			} else if (chars[chars.length - 3] == ',') {
				chars[chars.length - 3] = '.';
			}
			currency = new String(chars);
		}

		// Remove all commas
		return currency.replaceAll(",", "");
	}

	static boolean doesMatch(String s, String pattern) {
		try {
			Pattern patt = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
			Matcher matcher = patt.matcher(s);
			return matcher.matches();
		} catch (RuntimeException e) {
			return false;
		}
	}

	@Field
	@MaxSize(2000)
	public String observacoes;

	public String NI;

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
				.find("select distinct i from Inventario i join i.categoria as c where c.slug = ? order by i.criadoEm desc",
						slug).fetch();
	}

	public String toString() {
		return material;
	}
}