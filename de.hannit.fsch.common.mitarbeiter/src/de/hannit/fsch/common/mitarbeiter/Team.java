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
