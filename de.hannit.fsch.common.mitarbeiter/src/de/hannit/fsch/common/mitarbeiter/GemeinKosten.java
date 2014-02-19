/**
 * 
 */
package de.hannit.fsch.common.mitarbeiter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.TreeMap;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import de.hannit.fsch.common.csv.azv.Arbeitszeitanteil;

/**
 * Bei der Verteilung der Gemeinkosten werden die verbliebenen Reste der Endkostenstellen
 * aus Navision ausgelesen und für die Teams 1,2,3 und 4 auf die Kostenträger verteilt.
 * 
 * Wird die Mitarbeiterliste empfangen, werden zunächst Mitarbeiterlisten für die Teams 1,2,3 und 4 erstellt.
 * 
 * Für jedes Team wird dann die Gesamtsumme aller gemeldeten Prozentanteile für Kostenträger ermittelt.
 * Danach wird die Prozentsumme je Kostenträger ermittelt. Daraus ergibt sich folgender Dreisatz:
 * GesamtsummeProzentanteile = 100
 * ProzentsummeKTR = X  
 * 
 * Der Anteil des jeweiligen Kostenträgers an der Verteilungssumme auf der Endkostenstelle ist also:
 * 
 *   ProzentsummeKTR x 100
 * -------------------------
 * GesamtsummeProzentanteile
 * 
 * Anhand des Kostenträgeranteils an der GesamtsummeProzentanteile wird also wie folgt aufgeteilt:
 * 
 * Ergebnis =  (Verteilungssumme / 100) x ((ProzentsummeKTR x 100) / GesamtsummeProzentanteile)
 * 
 * Alles klar ? Viel Spaß bei der Einarbeitung ;-)
 * 
 * 
 * @author fsch
 * @since 11.02.2014
 *
 */
public class GemeinKosten implements ITableLabelProvider
{
public static final String ENDKOSTENSTELLE_TEAM1 = "1110";
public static final String ENDKOSTENSTELLE_TEAM2 = "2010";
public static final String ENDKOSTENSTELLE_TEAM3 = "3010";
public static final String ENDKOSTENSTELLE_TEAM4 = "4010";	

public static final String COLUMN1_KTR = "Kostenträger";
public static final String COLUMN2_KTR_BEZEICHNUNG = "Bezeichnung";
public static final String COLUMN3_PROZENTANTEIL = "Prozentanteil";
public static final String COLUMN4_VERTEILUNG = "Verteilung";

private TreeMap<Integer, Mitarbeiter> mitarbeiter;
private TreeMap<Integer, Mitarbeiter> team1 = new TreeMap<Integer, Mitarbeiter>();
private TreeMap<Integer, Mitarbeiter> team2 = new TreeMap<Integer, Mitarbeiter>();
private TreeMap<Integer, Mitarbeiter> team3 = new TreeMap<Integer, Mitarbeiter>();
private TreeMap<Integer, Mitarbeiter> team4 = new TreeMap<Integer, Mitarbeiter>();
private TreeMap<Integer, TreeMap<Integer, Mitarbeiter>> teams = new TreeMap<Integer, TreeMap<Integer, Mitarbeiter>>();

private DecimalFormat d = new DecimalFormat("#0.00"); 

/**
 * Die Summe der zu verteilenden Vorkostenstelle
 */
private double verteilungsSumme = 0;
/**
 * Vorkostenstelle, von der aus auf die Kostenträger verteilt wird.
 */
private String vorkostenStelle = null;

private TreeMap<Integer, Integer> gesamtsummenProzentanteile = new TreeMap<Integer, Integer>();
private TreeMap<String, Arbeitszeitanteil> aufteilungGemeinKosten = null;

private Date berichtsMonat = null;
private String label;
private boolean checked = false;
private boolean datenOK = false;

	/**
	 * 
	 */
	public GemeinKosten(Date selectedMonth)
	{
	this.berichtsMonat = selectedMonth;	
	}

	public TreeMap<Integer, Mitarbeiter> getMitarbeiter()
	{
	return mitarbeiter;
	}

	
	public TreeMap<Integer, SummenZeile> getSummentabelle()
	{
	TreeMap<Integer, SummenZeile> summenTabelle = new TreeMap<Integer, SummenZeile>();
	
	return summenTabelle;
	}
	/*
	 * Die Liste der gesamten Mitarbeiter wird durchlaufen.
	 * Die Mitarbeiter werden nach Teams gegliedert.
	 * 
	 */
	public void splitTeams()
	{
		for (Mitarbeiter m : mitarbeiter.values())
		{
			int test = m.getAzvMonat().firstEntry().getValue().getITeam();
			
			switch (test)
			{
			case 1:
			team1.put(m.getPersonalNR(), m);	
			break;

			case 2:
			team2.put(m.getPersonalNR(), m);	
			break;
			
			case 3:
			team3.put(m.getPersonalNR(), m);	
			break;
			
			default:
			team4.put(m.getPersonalNR(), m);
			break;
			}
		}
	// Für den einfacheren späteren Zugriff werden die Teams in eine seperate Liste gepackt:	
	teams.put(1, team1);
	teams.put(2, team2);
	teams.put(3, team3);
	teams.put(4, team4);
	
	setGesamtsummeProzentanteile();
		
	}
	
	/*
	 * Wird die Mitarbeiterliste gespeichert, werden alle Mitarbeiter in ihre Teamlisten sortiert
	 */
	public void setMitarbeiter(TreeMap<Integer, Mitarbeiter> mitarbeiter)
	{
	this.mitarbeiter = mitarbeiter;
	}

	/*
	 * Durchläuft die Teamlisten und ermittelt die Gesamtsummen aller KTR-Prozentanteile
	 */
	private void setGesamtsummeProzentanteile()
	{
		for (int i = 1; i <= teams.size(); i++)
		{
		TreeMap<Integer, Mitarbeiter> team = teams.get(i); 	
		int gesamtsummeProzentanteile = 0;

			for (Mitarbeiter m : team.values())
			{
				for (Arbeitszeitanteil azv : m.getAzvMonat().values())
				{
					if (azv.isKostentraeger())
					{
					gesamtsummeProzentanteile += azv.getProzentanteil();	
					}
				}
			}
		gesamtsummenProzentanteile.put(i, gesamtsummeProzentanteile);	
		}
		
		for (int i = 1; i <= gesamtsummenProzentanteile.size(); i++)
		{
		System.out.println("Team " + i + ": " + gesamtsummenProzentanteile.get(i));	
		}
	}

	
	public TreeMap<Integer, Integer> getGesamtsummenProzentanteile()
	{
	return gesamtsummenProzentanteile;
	}

	public Date getBerichtsMonat()
	{
	return berichtsMonat;
	}

	public TreeMap<String, Arbeitszeitanteil> getAufteilungGemeinKosten()
	{
	return aufteilungGemeinKosten;
	}

	/*
	 * Wenn die Verteilungssumme und die Vorkostenstelle bekannt ist,
	 * wird die Verteilungssumme auf die Kostenträger verteilt
	 */
	public void setAufteilungGemeinKosten()
	{
	this.aufteilungGemeinKosten = null;
	Arbeitszeitanteil vorhanden = null;
	TreeMap<Integer, Mitarbeiter> team = null;
	int teamNR = 0;
	int gesamtSummeProzentAnteile = 0;
	int summeProzentanteilKTR = 0;
	
		switch (this.vorkostenStelle)
		{
		case ENDKOSTENSTELLE_TEAM1:
		teamNR = 1;	
		team = teams.get(teamNR);
		gesamtSummeProzentAnteile = gesamtsummenProzentanteile.get(teamNR);
		break;
		
		case ENDKOSTENSTELLE_TEAM2:
		teamNR = 2;
		team = teams.get(teamNR);
		gesamtSummeProzentAnteile = gesamtsummenProzentanteile.get(teamNR);
		break;
		
		case ENDKOSTENSTELLE_TEAM3:
		teamNR = 3;	
		team = teams.get(teamNR);
		gesamtSummeProzentAnteile = gesamtsummenProzentanteile.get(teamNR);
		break;		
		
		default:
		teamNR = 4;	
		team = teams.get(teamNR);
		gesamtSummeProzentAnteile = gesamtsummenProzentanteile.get(teamNR);
		break;
		}
		
		/*
		 * Für alle Mitarbeiter des Teams werden die Prozentanteile je Kostenträger aufsummiert.
		 */
		aufteilungGemeinKosten = new TreeMap<String, Arbeitszeitanteil>();
		for (Mitarbeiter m : team.values())
		{
			for (Arbeitszeitanteil azv : m.getAzvMonat().values())
			{
				if (azv.isKostentraeger())
				{
					if (aufteilungGemeinKosten.containsKey(azv.getKostentraeger()))
					{
					vorhanden = aufteilungGemeinKosten.get(azv.getKostentraeger());
					summeProzentanteilKTR = (vorhanden.getProzentanteil() + azv.getProzentanteil());
					vorhanden.setProzentanteil(summeProzentanteilKTR);
					aufteilungGemeinKosten.put(vorhanden.getKostentraeger(), vorhanden);
					}
					else
					{
					aufteilungGemeinKosten.put(azv.getKostentraeger(), azv);	
					}
				}
			}
		}
		for (Arbeitszeitanteil azv : aufteilungGemeinKosten.values())
		{
		System.out.println(azv.getKostentraeger() + " = " + azv.getProzentanteil());	
		}
		/*
		 * Danach wird der Anteil des Kostenträgers an der Verteilungssumme festgelegt:
		 * (ProzentanteilAZV x 100) / GesamtsummeProzentanteile
		 */
		for (Arbeitszeitanteil azv : aufteilungGemeinKosten.values())
		{
		double test = (azv.getProzentanteil() * 100);
		test = test  / gesamtSummeProzentAnteile;
		azv.setProzentanteilGemeinkosten(test);	
		azv.setAnteilGemeinkosten((verteilungsSumme / 100) * azv.getProzentanteilGemeinkosten());
		}
		
		for (Arbeitszeitanteil azv : aufteilungGemeinKosten.values())
		{
		System.out.println(azv.getKostentraeger()+";"+azv.getProzentanteilGemeinkosten()+";"+ NumberFormat.getCurrencyInstance().format(azv.getAnteilGemeinkosten()));	
		}		
		
	}

	public void setVerteilungsSumme(double verteilungsSumme)
	{
	this.verteilungsSumme = verteilungsSumme;
		if (this.vorkostenStelle != null)
		{
		setAufteilungGemeinKosten();	
		}
	}

	public void setVorkostenStelle(String vorkostenStelle)
	{
	this.vorkostenStelle = vorkostenStelle;
		if (this.verteilungsSumme > 0)
		{
		setAufteilungGemeinKosten();	
		}
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
	Arbeitszeitanteil azv = (Arbeitszeitanteil) element;
	
		switch (columnIndex) 
		{
		case 0:
		label = azv.getKostentraeger();
		break;
		
		case 1:
		label = azv.getKostenTraegerBezeichnung();
		break;
		
		case 2:
		label = d.format(azv.getProzentanteilGemeinkosten());
		break;
		
		case 3:
		label = NumberFormat.getCurrencyInstance().format(azv.getAnteilGemeinkosten());
		break;
				
		default:
		label = " ";
		break;
			
		}
	return label;
	}
public boolean isChecked(){return checked;}
public void setChecked(boolean checked){this.checked = checked;}	

public boolean isDatenOK(){return datenOK;}
public void setDatenOK(boolean ok){this.datenOK = ok;}	

}
