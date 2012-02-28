package models;

import java.util.*;
import java.util.regex.*;
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
	
	@ManyToOne
	public Empresa empresa;

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

	public void autoSetSaida() {
		if (this.saida == null)
			this.saida = 0;
	}

	public Integer existencia;

	public void autoSetExistencia() {
		if (this.existencia == null)
			this.existencia = this.entrada - this.saida;
	}

	@Field
	@Required
	public Long valunit;

	public void setValunit(String valunit) {
		this.valunit = currencyToLong(valunit);
	}

	public String getValunit() {
		return valunit == null ? null : longToCurrency(valunit);
	}

	@Field
	public Long valtotal;

	public void setValtotal(String valtotal) {
		this.valtotal = currencyToLong(valtotal);
	}

	public void autoSetValtotal() {
		if (getValtotal() == null || getValtotal().equals(""))
			setValtotal(longToCurrency(this.entrada
					* currencyToLong(getValunit())));
		else
			System.out.println("FAIL![" + getValtotal() + "]");
	}

	public String getValtotal() {
		return longToCurrency(valtotal);
	}

	static Long currencyToLong(String currency) {
		if(currency == null || currency.equals(""))
			return null;
		try {
			return Long.parseLong(currencyToDecimalFormat(currency));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	static String longToCurrency(Long number) {
		if (number == null)
			return null;
		Locale.setDefault(Locale.GERMAN);
		DecimalFormat real = new DecimalFormat("###,###,###,##0.00");
		String currency = real.format(number / 100.0);
		return currency;
	}

	static String currencyToDecimalFormat(String currency) {
		currency = currency.replace(".", ",");
		int l = currency.length();
		if (l >= 3) {
			if (currency.charAt(l - 3) == ',')
				return currency.replace(",", "");
			else if (currency.charAt(l - 2) == ',')
				return currency.replace(",", "") + "0";
			else
				return currency.replace(",", "") + "00";
		} else {
			return currency.replace(",", "") + "00";
		}
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

		// Remove all commas and dots
		return currency.replaceAll(",", "").replace(".", "");
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

	public String destino;

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