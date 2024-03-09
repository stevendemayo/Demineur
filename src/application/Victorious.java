package application;
//Classe utilisee pour creer des personnes du top 10, les 10 meilleurs temps
public class Victorious 
{
	private int rang;//le rang du joueur
	private String pseudo;//son pseudo
	private int time;//le temps qu'il lui a fallu pour gaganer la partie

	//Constructeur permettant de recuperer les informations 
	//d'une ligne du fichier de glorious.txt pour creer un objet Victorious
	public Victorious(String ligne) 
	{
		int i = 0;
		
		String ranger = "";
		while(ligne.charAt(i)!='e')
		{
			ranger += ligne.charAt(i);
			i++;
		}
		rang = Integer.parseInt(ranger);
		while(ligne.charAt(i)!='O')
			i++;
		i+=4;
		pseudo = "";
		while(ligne.charAt(i+1)!= '-')
		{
			pseudo += ligne.charAt(i);
			i++;
		}
		i+=10;
		String timer = "";
		while(ligne.charAt(i)!='s')
		{
			timer += ligne.charAt(i);
			i++;
		}
		time = Integer.parseInt(timer);
	}
	
	//constructeur classique
	public Victorious(int range,String pseud,int temps) 
	{
		rang = range;
		pseudo = pseud;
		time = temps;
	}
	public int getRang() {
		return rang;
	}
	public String getPseudo() {
		return pseudo;
	}
	public int getTime() {
		return time;
	}
	public void setRang(int rang) {
		this.rang = rang;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public void setTime(int time) {
		this.time = time;
	}
	@Override
	public String toString() {
		String ranger = "";
		if(rang == 1)
			ranger = "er(e)";
		else
			ranger = "eme";
		
		return rang +""+ ranger +" - PSEUDO : "+ pseudo +" - Time : "+ time +"secs";
	}
	
	

}
