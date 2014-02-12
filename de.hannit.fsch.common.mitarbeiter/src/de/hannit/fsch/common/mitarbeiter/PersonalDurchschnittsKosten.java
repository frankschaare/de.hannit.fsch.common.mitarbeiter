/**
 * 
 */
package de.hannit.fsch.common.mitarbeiter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import de.hannit.fsch.common.csv.azv.Arbeitszeitanteil;

/**
 * Bei den Personaldurchschnittskosten werden die Vollzeitäquivalente
 * unterteilt nach den Teams und dort wiederum unterteilt nach Beamten / Angestellten dargestellt.
 * 
 * Diese Werte werden in Navision bei der Verteilung der Umlage 002 und 003 benötigt.
 * 
 * @author fsch
 * @since 11.02.2014
 *
 */
public class PersonalDurchschnittsKosten implements ITableLabelProvider
{
public static final String COLUMN1_OE = "Organisationseinheit";
public static final String COLUMN2_ANGESTELLTE = "Angestellte";
public static final String COLUMN3_ANGESTELLTE_VZAE = "VZÄ";
public static final String COLUMN4_BEAMTE = "Beamte";
public static final String COLUMN5_BEAMTE_VZAE = "VZÄ";
public static final String COLUMN6_SUMME_BRUTTO = "Gesamtkosten";
public static final String COLUMN7_SUMME_VZAE = "Gesamt VZÄ";
public static final String COLUMN8_ABZUG_VORKOSSTENSTELLEN = "./. Vorkostenstellen";
public static final String COLUMN9_VZAE_ENDKOSTENSTELLEN = "VZÄ Endkostenstellen";

private TreeMap<Integer, Mitarbeiter> mitarbeiter;
private TreeMap<Integer, Team> teams = new TreeMap<Integer, Team>();	
private Date berichtsMonat = null;
private String label;
private DecimalFormat kommaZahl= new DecimalFormat("0,00");

	/**
	 * 
	 */
	public PersonalDurchschnittsKosten(Date selectedMonth)
	{
	this.berichtsMonat = selectedMonth;	
	}

	public TreeMap<Integer, Mitarbeiter> getMitarbeiter()
	{
	return mitarbeiter;
	}

	/**
	 * Gesamtsumme aller Bruttoaufwendungen 
	 */
	public double getSummeBruttoGesamt()
	{
	double sumGesamt = 0;
	
		for (Team t : teams.values())
		{
			if (t.getAngestellte().size() > 0)
			{
				for (Mitarbeiter m : t.getAngestellte().values())
				{
				sumGesamt += m.getBrutto();	
				}			
			}
			if (t.getBeamte().size() > 0)
			{
				for (Mitarbeiter m : t.getBeamte().values())
				{
				sumGesamt += m.getBrutto();	
				}			
			}			
		}
	return sumGesamt;	
	}
	
	/**
	 * Gesamtsumme aller Vollzeiäquivalente 
	 */
	public double getSummeVZAEGesamt()
	{
	double sumGesamt = 0;
	
		for (Team t : teams.values())
		{
			if (t.getAngestellte().size() > 0)
			{
				for (Mitarbeiter m : t.getAngestellte().values())
				{
				sumGesamt += m.getStellenAnteil();	
				}			
			}
			if (t.getBeamte().size() > 0)
			{
				for (Mitarbeiter m : t.getBeamte().values())
				{
				sumGesamt += m.getStellenAnteil();	
				}			
			}			
		}
	return sumGesamt;	
	}
	
	/**
	 * Gesamt Anzahl Mitarbeiter 
	 */
	public int getAnzahlMitarbeiter()
	{
	int anzGesamt = 0;
	
		for (Team t : teams.values())
		{
		anzGesamt += t.getAngestellte().size();
		anzGesamt += t.getBeamte().size();
		}
	return anzGesamt;	
	}	
	
	/**
	 * Summe der Bruttoaufwendungen für alle Angestellten des Teams
	 */
	public double getSummeBruttoAngestellte(int teamNR)
	{
	double sumBruttoAngestellte = 0;
	Team t = teams.get(teamNR);
	
		if (t.getAngestellte().size() > 0)
		{
			for (Mitarbeiter m : t.getAngestellte().values())
			{
			sumBruttoAngestellte += m.getBrutto();	
			}			
		}
	
	return sumBruttoAngestellte;	
	}
	
	/**
	 * Summe der Vollzeitäquivalente für alle Angestellten des Teams
	 * @param teamNR
	 * @return
	 */
	public double getSummeVZAEAngestellte(int teamNR)
	{
	double sumVZAEAngestellte = 0;
	Team t = teams.get(teamNR);
	
		for (Mitarbeiter m : t.getAngestellte().values())
		{
		sumVZAEAngestellte += m.getStellenAnteil();	
		}
	
	return sumVZAEAngestellte;	
	}	
	
	/**
	 * Summe der Bruttoaufwendungen für alle Beamten des Teams
	 */
	public double getSummeBruttoBeamte(int teamNR)
	{
	double sumBruttoBeamte = 0;
	Team t = teams.get(teamNR);
	
		for (Mitarbeiter m : t.getBeamte().values())
		{
			sumBruttoBeamte += m.getBrutto();	
		}
	
	return sumBruttoBeamte;	
	}
	
	/**
	 * Summe der Vollzeitäquivalente für alle Beamten des Teams
	 * @param teamNR
	 * @return
	 */
	public double getSummeVZAEBeamte(int teamNR)
	{
	double sumVZAEBeamte = 0;
	Team t = teams.get(teamNR);
	
		for (Mitarbeiter m : t.getBeamte().values())
		{
		sumVZAEBeamte += m.getStellenAnteil();	
		}
	
	return sumVZAEBeamte;	
	}		
	
	public ArrayList<String> getSummentabelle()
	{
	ArrayList<String> summenTabelle = new ArrayList<String>();
	int teamNR = -1;
	String strSumme = null;
	double sumBruttoAngestellte = 0;
	double sumBruttoAngestellteGesamt = 0;
	double sumVZAEAngestellte = 0;
	double sumVZAEAngestellteGesamt = 0;
	double sumBruttoBeamte = 0;
	double sumBruttoBeamteGesamt = 0;
	double sumVZAEBeamte = 0;
	double sumVZAEBeamteGesamt = 0;
	
		for (Team t : teams.values())
		{
		teamNR = t.getTeamNummer();	
		
		sumBruttoAngestellte = getSummeBruttoAngestellte(teamNR);
		sumBruttoAngestellteGesamt += sumBruttoAngestellte;
		
		sumVZAEAngestellte = getSummeVZAEAngestellte(teamNR);
		sumVZAEAngestellteGesamt += sumVZAEAngestellte;
		
		sumBruttoBeamte = getSummeBruttoBeamte(teamNR);
		sumBruttoBeamteGesamt += sumBruttoBeamte;
		
		sumVZAEBeamte = getSummeVZAEBeamte(teamNR);
		sumVZAEBeamteGesamt += sumVZAEBeamte;
		
		strSumme = t.getOE() + ";" + NumberFormat.getCurrencyInstance().format(sumBruttoAngestellte) + ";" + sumVZAEAngestellte + ";" + NumberFormat.getCurrencyInstance().format(sumBruttoBeamte) + ";" + sumVZAEBeamte + ";" + NumberFormat.getCurrencyInstance().format((sumBruttoAngestellte + sumBruttoBeamte)) + ";" + (sumVZAEAngestellte + sumVZAEBeamte); 
		
		summenTabelle.add(strSumme);
		}
	strSumme = " ; ; ; ; ; ; ; ;";
	summenTabelle.add(strSumme);
		
	strSumme = "Summen:;" + NumberFormat.getCurrencyInstance().format(sumBruttoAngestellteGesamt) + ";" + sumVZAEAngestellteGesamt + ";" + NumberFormat.getCurrencyInstance().format(sumBruttoBeamteGesamt) + ";" + sumVZAEBeamteGesamt + ";" + NumberFormat.getCurrencyInstance().format((sumBruttoAngestellteGesamt + sumBruttoBeamteGesamt)) + ";" + (sumVZAEAngestellteGesamt + sumVZAEBeamteGesamt);
	summenTabelle.add(strSumme);
	
	return summenTabelle;
	}
	
	public void printSummenTabelle()
	{
	ArrayList<String> summenTabelle = getSummentabelle();
		for (String string : summenTabelle)
		{
		System.out.println(string);	
		}
	}
	
	public void setMitarbeiter(TreeMap<Integer, Mitarbeiter> mitarbeiter)
	{
	this.mitarbeiter = mitarbeiter;
	TreeMap<String, Arbeitszeitanteil> azvs = null;
	/*
	 * Wiewiel Prozentanteil hat der Mitarbeiter pro Team aufgewendet ?
	 */
	TreeMap<Integer, Integer> prozentanteileTeams = null;
	int teamNR = -1;
	int prozentanteil;
		/*
		 * Die Liste der gesamten Mitarbeiter wird durchlaufen.
		 * Die Mitarbeiter werden nach Teams gegliedert.
		 * 
		 * Gleichzeitig wird eine Liste mit allen Teams gebildet, für die AZV-Anteile gemeldet wurden
		 */
		for (Mitarbeiter m : mitarbeiter.values())
		{
		prozentanteileTeams = new TreeMap<Integer, Integer>();	
		azvs = m.getAzvMonat();	
			for (Arbeitszeitanteil azv : azvs.values())
			{
			teamNR = azv.getITeam();	
				if (prozentanteileTeams.containsKey(teamNR))
				{
				prozentanteil = prozentanteileTeams.get(teamNR);	
				prozentanteileTeams.put(teamNR, (prozentanteil + azv.getProzentanteil()));	
				}
				else
				{
				prozentanteileTeams.put(teamNR, azv.getProzentanteil());
				}
				// Teamliste prüfen
				if (!teams.containsKey(teamNR))
				{
				teams.put(teamNR, new Team(teamNR));	
				}			
			}
		teams.get(teamNR).addMitarbeiter(m);	
		}
	}

	public Date getBerichtsMonat()
	{
	return berichtsMonat;
	}

	@Override
	public void addListener(ILabelProviderListener listener)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex)
	{
	String strValues = (String) element;
	String[] parts	= strValues.split(";");
		switch (columnIndex) 
		{
		case 0:
		label = parts[0];
		break;
		
		case 1:
		label = parts[1];
		break;
		
		case 2:
		label = parts[2];
		break;
		
		case 3:
		label = parts[3];
		break;
		
		case 4:
		label = parts[4];
		break;		
		
		case 5:
		label = parts[5];
		break;		
		
		case 6:
		label = parts[6];
		break;		
		
		default:
		label = " ";
		break;
			
		}
	return label;
	}
	
	

}
