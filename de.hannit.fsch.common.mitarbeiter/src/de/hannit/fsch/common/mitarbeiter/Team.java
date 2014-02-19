/**
 * 
 */
package de.hannit.fsch.common.mitarbeiter;

import java.util.TreeMap;

/**
 * @author fsch
 *
 */
public class Team
{
private int teamNummer = -1;
private TreeMap<Integer, Mitarbeiter> angestellte = new TreeMap<Integer, Mitarbeiter>();
private TreeMap<Integer, Mitarbeiter> beamte = new TreeMap<Integer, Mitarbeiter>();
private TreeMap<Integer, Mitarbeiter> mitarbeitAufVorkostenstellen = new TreeMap<Integer, Mitarbeiter>();
private TreeMap<Integer, Mitarbeiter> mitarbeitAltersteilzeit = new TreeMap<Integer, Mitarbeiter>();

	/**
	 * 
	 */
	public Team(int nummer)
	{
	setTeamNummer(nummer);	
	}

	public void addMitarbeiter(Mitarbeiter m)
	{
		switch (m.getStatus())
		{
		case Mitarbeiter.STATUS_BEAMTER:
		beamte.put(m.getPersonalNR(), m);	
		break;

		default:
		angestellte.put(m.getPersonalNR(), m);	
		break;
		}
		/*
		 * Für die Teamnummern 0 und 1 werden alle Mitarbeiter auf Vorkostenstellen gebucht.
		 * Sie werden daher zusätzlich in der Liste mitarbeitAufVorkostenstellen gesichert
		 * und später bei der Berechnung herausgenommen.
		 */
		switch (this.teamNummer)
		{
		case 0:
		mitarbeitAufVorkostenstellen.put(m.getPersonalNR(), m);	
		break;

		case 1:
		mitarbeitAufVorkostenstellen.put(m.getPersonalNR(), m);	
		break;

		default:
		break;
		}
	}
	
	public TreeMap<Integer, Mitarbeiter> getMitarbeitAufVorkostenstellen()
	{
	return mitarbeitAufVorkostenstellen;
	}

	public String getOE()
	{
	String strOE = null;
		switch (getTeamNummer())
		{
		case 0:
		strOE = "Geschäftsführung";	
		break;

		default:
		strOE = "81.01.0" + String.valueOf(getTeamNummer());	
		break;
		}
	return strOE;
	}
	
	public int getTeamNummer()
	{
		return teamNummer;
	}

	public void setTeamNummer(int teamNummer)
	{
		this.teamNummer = teamNummer;
	}

	public TreeMap<Integer, Mitarbeiter> getAngestellte()
	{
		return angestellte;
	}

	public void setAngestellte(TreeMap<Integer, Mitarbeiter> angestellte)
	{
		this.angestellte = angestellte;
	}

	public TreeMap<Integer, Mitarbeiter> getBeamte()
	{
		return beamte;
	}

	public void setBeamte(TreeMap<Integer, Mitarbeiter> beamte)
	{
		this.beamte = beamte;
	}

	
}
