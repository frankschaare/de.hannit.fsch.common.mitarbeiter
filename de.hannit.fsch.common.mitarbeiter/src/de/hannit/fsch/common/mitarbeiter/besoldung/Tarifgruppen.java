package de.hannit.fsch.common.mitarbeiter.besoldung;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.TreeMap;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class Tarifgruppen implements ITableLabelProvider
{
private TreeMap<String, Tarifgruppe> tarifGruppen = new TreeMap<String, Tarifgruppe>();
private Tarifgruppe tarifgruppe;
private double summeTarifgruppen = 0;
private double summeStellen = 0;
private double summeVollzeitAequivalent = 0;
private int anzahlMitarbeiter = 0;
private Date berichtsMonat = null;
private String label;
private DecimalFormat f = new DecimalFormat("#0,00"); 

	public Tarifgruppen()
	{
	}
	public TreeMap<String, Tarifgruppe> getTarifGruppen(){return tarifGruppen;}
	public void setTarifGruppen(TreeMap<String, Tarifgruppe> tarifGruppen){this.tarifGruppen = tarifGruppen;}
	
	
	public double getSummeTarifgruppen()
	{
		for (Tarifgruppe t : getTarifGruppen().values())
		{
		summeTarifgruppen = summeTarifgruppen + t.getSummeTarifgruppe();	
		}
	return summeTarifgruppen;
	}
	public double getSummeStellen()
	{
		for (Tarifgruppe t : getTarifGruppen().values())
		{
		summeStellen = summeStellen + t.getSummeStellen();	
		}
	return summeStellen;
	}
	public double getSummeVollzeitAequivalent()
	{
		for (Tarifgruppe t : getTarifGruppen().values())
		{
		summeVollzeitAequivalent = summeVollzeitAequivalent + t.getVollzeitAequivalent();	
		}
	return (summeVollzeitAequivalent * summeStellen);
	}
	
	public int getAnzahlMitarbeiter(){return anzahlMitarbeiter;}
	public void setAnzahlMitarbeiter(int anzahlMitarbeiter)	{this.anzahlMitarbeiter = anzahlMitarbeiter;}
	public Date getBerichtsMonat(){	return berichtsMonat;}
	public void setBerichtsMonat(Date berichtsMonat){this.berichtsMonat = berichtsMonat;}
	
	@Override
	public void addListener(ILabelProviderListener listener)	{}
	
	@Override
	public void dispose()	{}
	
	@Override
	public boolean isLabelProperty(Object element, String property)	{return false;}
	
	@Override
	public void removeListener(ILabelProviderListener listener)	{}
	
	@Override
	public Image getColumnImage(Object element, int columnIndex)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getColumnText(Object element, int columnIndex)
	{
	tarifgruppe =  (Tarifgruppe) element;

		switch (columnIndex) 
		{
		case 0:
		label = String.valueOf(tarifgruppe.getTarifGruppe());
		break;
		
		case 1:
		label = NumberFormat.getCurrencyInstance().format((tarifgruppe.getSummeTarifgruppe()));
		break;
		
		case 2:
		label = String.valueOf(tarifgruppe.getSummeStellen());
		break;
		
		case 3:
		label = NumberFormat.getCurrencyInstance().format((tarifgruppe.getVollzeitAequivalent()));
		break;
		
		default:
		label = "ERROR";
		break;
			
		}
	return label;
	}
}
