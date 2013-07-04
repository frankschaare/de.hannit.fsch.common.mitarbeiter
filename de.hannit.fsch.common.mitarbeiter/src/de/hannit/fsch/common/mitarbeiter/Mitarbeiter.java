/**
 * 
 */
package de.hannit.fsch.common.mitarbeiter;

/**
 * @author fsch
 *
 */
public class Mitarbeiter extends Person 
{
private int personalNR;
private String benutzerName;

	/**
	 * 
	 */
	public Mitarbeiter() 
	{
		// TODO Auto-generated constructor stub
	}

	public int getPersonalNR() {
		return personalNR;
	}

	public void setPersonalNR(int personalNR) {
		this.personalNR = personalNR;
	}

	public String getBenutzerName() {
		return benutzerName;
	}

	public void setBenutzerName(String benutzerName) {
		this.benutzerName = benutzerName;
	}

	
}
