package application;
//Classe utilisee pour avoir divers informations liees a une case
public class Case 
{
	private int row;//ligne de la case
	private int col;//colonne de la case
	private int value;//valeur contenue dans la case
	private boolean reveal;//etat de la case, si elle est devoilee ou non
	private boolean flag;//etat de la case, si elle a un drapeau ou non

	public Case(int i,int j,int val) 
	{
		row = i;
		col = j;
		value = val;
		reveal = false;
		flag = false;
	}
	
	public boolean getReveal()
	{ return reveal; }
	
	public void setReveal(boolean bool)
	{ reveal = bool; }
	
	public boolean getFlag()
	{ return flag; }
	
	public void setFlag(boolean bool)
	{ flag = bool; }

	public int getRow() 
	{ return row; }

	public int getCol() 
	{ return col; }

	public int getValue() 
	{ return value; }
}
