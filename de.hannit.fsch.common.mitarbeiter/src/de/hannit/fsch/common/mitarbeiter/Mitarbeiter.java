/**
 * 
 */
package de.hannit.fsch.common.mitarbeiter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import de.hannit.fsch.common.csv.azv.Arbeitszeitanteil;

/**
 * @author fsch
 *
 */
public class Mitarbeiter extends Person 
{
private int personalNR = 0;
private String benutzerName;
private Date abrechnungsMonat = null;
private double brutto;
private double vollzeitAequivalent = 0;
private String tarifGruppe = null;
private String tarifStufe = null;
private double stellenAnteil = 0;

private ArrayList<Arbeitszeitanteil> arbeitszeitAnteile = null;
private TreeMap<String, Arbeitszeitanteil> azvMonat = null;
private boolean azvAktuell = true;

/**
 * Der Gesamtprozentanteil der gespeicherten Arbeitszeitanteile.
 * Dieser MUSS = 100 sein, sonst ist etwas schief gelaufen.
 * Wird vom NavPart und vom NavTreeContentProvider geprüft.
 */
private int azvProzentSumme = 0;

	/**
	 * 
	 */
	public Mitarbeiter() 
	{
	azvMonat = new TreeMap<String, Arbeitszeitanteil>();
	}
	
	public ArrayList<Arbeitszeitanteil> getArbeitszeitAnteile(){return arbeitszeitAnteile;}
	public void setArbeitszeitAnteile(ArrayList<Arbeitszeitanteil> arbeitszeitAnteile){this.arbeitszeitAnteile = arbeitszeitAnteile;
	}
	
	public TreeMap<String, Arbeitszeitanteil> getAzvMonat() {return azvMonat;}
	public void setAzvMonat(TreeMap<String, Arbeitszeitanteil> azvMonat) {this.azvMonat = azvMonat;}

	public int getPersonalNR() {return personalNR;}
	public void setPersonalNR(int personalNR) {this.personalNR = personalNR;}

	public String getBenutzerName() {return benutzerName;}
	public void setBenutzerName(String benutzerName) {this.benutzerName = benutzerName;}

	public Date getAbrechnungsMonat(){return abrechnungsMonat;}
	public void setAbrechnungsMonat(Date abrechnungsMonat)	{this.abrechnungsMonat = abrechnungsMonat;}

	public double getBrutto(){return brutto;}
	public void setBrutto(double brutto){this.brutto = brutto;}

	public double getVollzeitAequivalent(){return vollzeitAequivalent;}
	
	/**
	 * Speichert das Vollzeitäquivalent für den Mitarbeiter
	 * Das Vollzeitäquivalent wird aus den Tarifgruppen geliefert, wo der Mittelwert für die jeweilige Tarifgruppe bekannt ist.
	 * 
	 * Da das Vollzeitäquivalent für DIESEN Mitarbeiter gilt, wird mit dem Stellenanteil mutipliziert
	 * 
	 * Nachdem das Vollzeitäquivalent gespeichert wurde, wird in der Methode #updateBruttoanteil der Bruttoanteil
	 * für die gemeldeten Arbeitszeitanteile des Mitarbeiters gespeichert
	 * @param vollzeitAequivalent
	 */
	public double setVollzeitAequivalent(double vollzeitAequivalent)
	{
	this.vollzeitAequivalent = vollzeitAequivalent;
	updateBruttoanteil();
	return (this.vollzeitAequivalent * this.stellenAnteil);
	}
	
	/**
	 * 
	 * @return Die Summe aller AZV-Anteile. MUSS = 100% sein, sonst wird ein Fehler ausgegeben.
	 */
	public int getAzvProzentSumme()
	{
	Arbeitszeitanteil azv = null;
	azvProzentSumme = 0;
	
		for (String str : getAzvMonat().keySet())
		{
		azv = getAzvMonat().get(str);
		azvProzentSumme += azv.getProzentanteil();
		}
	return azvProzentSumme;	
	}

	private void updateBruttoanteil()
	{
	double brutto = 0;	
		for (Arbeitszeitanteil a : azvMonat.values())
		{
		brutto = ((this.brutto /100) * a.getProzentanteil());	
		// brutto = ((this.vollzeitAequivalent * this.stellenAnteil) / 100) * a.getProzentanteil();
		a.setBruttoAufwand(brutto);
		}
	}

	public String getTarifGruppe(){return tarifGruppe;}
	public void setTarifGruppe(String tarifGruppe){this.tarifGruppe = tarifGruppe;}

	public String getTarifStufe(){return tarifStufe;}
	public void setTarifStufe(String tarifStufe){this.tarifStufe = tarifStufe;}

	public double getStellenAnteil(){return stellenAnteil;}
	public void setStellenAnteil(double stellenAnteil){this.stellenAnteil = stellenAnteil;}

	public boolean isAzvAktuell(){return azvAktuell;}
	public void setAzvAktuell(boolean azvAktuell){this.azvAktuell = azvAktuell;}
	
}
