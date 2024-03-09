package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class DemineurController 
{
	@FXML
	private GridPane grid;//lie au GridPane de la fenetre
	@FXML
	private ImageView ima;//lie a l'emogi sur la fenetre
	@FXML
	private ImageView flag;//lie a l'image du drapeau
	@FXML
	private HBox hb1;//lie au HBox du haut
	@FXML
	private HBox hb2;//lie au HBox du bas
	@FXML
	private ImageView classi;//lie a l'image du Top 10
	@FXML
	private Label sc;//lie a l'affichage du nombre de grapeau max a utlise pour trouver les bombes
	@FXML
	private ImageView mus;//lie a l'image sur le HBox du bas
	@FXML
	private Label time;//lie a l'affichage du temps de la partie
	
	private boolean fly = false;//etat du drapeau, actif ou non actif
	private boolean playa = true;//etat de la partie, en cours ou en arret
	private int Boom = 10;//nombre de drapeau a utilise
	private int cnt = 0;//le temps de jeu
	private int parcours = 0;//parcours des musiques
	private Timeline timer;//chrono
	private ArrayList<Case> cases = new ArrayList<>();//ensemsble des Cases
	private ArrayList<Victorious> glory = new ArrayList<>();//contient le top 10
	private ArrayList<String> musics = new ArrayList<>();//contient tt les musiques
	private MediaPlayer mediaPlayer;//permet de lire et de jouer une musique
    //matrice de jeu, noter que la matrice 2 lignes et 2 colonnes en plus que le GridPane, ceci pour faciliter certaines verifications
	private int[][] mat = {{-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},{-2,0,0,0,0,0,0,0,0,0,-2},{-2,0,0,0,0,0,0,0,0,0,-2},{-2,0,0,0,0,0,0,0,0,0,-2},{-2,0,0,0,0,0,0,0,0,0,-2},{-2,0,0,0,0,0,0,0,0,0,-2},{-2,0,0,0,0,0,0,0,0,0,-2},{-2,0,0,0,0,0,0,0,0,0,-2},{-2,0,0,0,0,0,0,0,0,0,-2},{-2,0,0,0,0,0,0,0,0,0,-2},{-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2}}; 
	
	
	@FXML
	public void initialize()
	{
		Copy();//recuperation des informations dans les divers fichiers
		//initialisation du chrono et de son gestionnaire d'evenement
		timer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				cnt++;//s'incremente chaque seconde jouant ainsi le role de chrono
				if(cnt<10)
					time.setText("00"+cnt);
				if(cnt<100 && cnt>9)
					time.setText("0"+cnt);
				if(cnt<1000 && cnt>99)
					time.setText(""+cnt);
			}
		}));
		timer.setCycleCount(Timeline.INDEFINITE);//permet de definir le cycle d'incrementation
		//mise des fonds d'ecrans des 2 HBox
		Image img =new Image("C://Users/dell/eclipse-workspace/Demineur//Images/fond1.bmp");
		BackgroundImage bckIm = new BackgroundImage(img,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background back = new Background(bckIm);
		hb1.setBackground(back);
		hb2.setBackground(back);
		//mise en place des gestionnaires d'evenemts sur les differents composants
		classi.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				classi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/class_click.bmp"));
			}
		});
		classi.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				classi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/class.bmp"));
				Alert alert = new Alert(AlertType.INFORMATION);//initialisation d'une boite de dialogue
		        alert.setTitle("THE UN-BOOM-TABLE 10");
		        alert.setHeaderText(null);
		        
		        String liste = "";
		        for(Victorious presV : glory)
		        	liste += presV.toString()+"\n";//recuperation de la liste du Top 10
		        
		        alert.setContentText(liste);//mise de la liste dans la boite de dialogue
		        ImageView ic = new ImageView();
		        ic.setFitHeight(60);
		        ic.setFitWidth(60);
		        ic.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/unboomtable.png"));
		        alert.setGraphic(ic);//definition d'une icone

		        alert.showAndWait();//affichage de la liste dans la boite de dialogue
			}
		});
		
		flag.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				fly = !fly;
				if(fly)
					flag.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/flag_click.bmp"));
				else
					flag.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/flag.bmp"));
			}
		});
		
		ima.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ima.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/pose_click.bmp"));
			}
		});
		ima.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//lancement d'une nouvelle partie
				flag.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/flag.bmp"));
				ima.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/pose.bmp"));
				//vidage de toutes les listes et initialisation des differents parametres de jeu
				cases.clear();
				glory.clear();
				musics.clear();
				playa = true;
				fly = false;
				Boom = 10;
				BoomReveal();
				cnt = 0;
				time.setText("000");
				timer.stop();//arret du chrono
				//vidage de la matrice de jeu
				for(int i=0;i<grid.getRowCount()+2;i++)
					for(int j=0;j<grid.getColumnCount()+2;j++)
						if(mat[i][j]!=-2)
							mat[i][j] = 0;
				mediaPlayer.stop();;//arret de la musiaue
				initialize();
			}
		});
		
		mus.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
            public void handle(MouseEvent event) {
				mus.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/mus_click.bmp"));
				mediaPlayer.stop();
			}
		});
		//gestionnaire d'evenement permettant de passer a la musique suivante apres le relache
		mus.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mus.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/mus.bmp"));
				parcours++;//passage au son suivant
				if(parcours == musics.size())//pour ne pas sortir de la liste
					parcours = 0;
				Media sound = new Media(musics.get(parcours));
				mediaPlayer = new MediaPlayer(sound);
				//gestionnaire d'evenement mit en place pour passer automatiquement a la musique suivante lorsque celle actuelle se termine
				MediaPlayer mp = new MediaPlayer(sound);
				mp.setOnEndOfMedia(() -> {
					parcours++;
					if(parcours == musics.size())
						parcours = 0;
					Media soundi = new Media(musics.get(parcours));
					mediaPlayer = new MediaPlayer(soundi);
					mediaPlayer.setOnEndOfMedia(mp.getOnEndOfMedia());
					mediaPlayer.play();	           
	            });
				mediaPlayer.setOnEndOfMedia(mp.getOnEndOfMedia());
				mediaPlayer.play();//lancement musique
			}
		});
		
		for(int i=0;i<grid.getRowCount();i++)
			for(int j=0;j<grid.getColumnCount();j++)
			{
				//initialisation de chaque case de notre GridPane avec une image de case
				ImageView imag = new ImageView();
				imag.setFitWidth(33);
				imag.setFitHeight(33);
				imag.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/init.bmp"));
				grid.add(imag, i, j);
				//initialisation de chaque de chaque image avec des gestionnaires d'evenements
				imag.setOnMousePressed(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                    	Node source = (Node) event.getSource();
                    	//recuperation de la ligne et de la colonne ou a eu lieu la pression
                        Integer colIndex = GridPane.getColumnIndex(source);
                        Integer rowIndex = GridPane.getRowIndex(source);
                        ImageView imagi = new ImageView();
                        imagi.setFitWidth(33);
        				imagi.setFitHeight(33);
                    	if(!fly && playa)
                    	{
                    		timer.play();//debut chrono
                    		for(Case cs: cases)
        						if(cs.getCol()==colIndex && cs.getRow()==rowIndex && !cs.getFlag())
        						{
        							//donner l'ilusion de la pression
        							cs.setReveal(true);
                    				ima.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/stress.bmp"));
                                    imagi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/click.bmp"));
                                    grid.add(imagi, colIndex, rowIndex);
        						}
                    	}
                    	if(fly && playa)
                    	{
                    		timer.play();
                    		for(Case cs: cases)
                    			if(cs.getCol()==colIndex && cs.getRow()==rowIndex && !cs.getReveal())
        						{//si la case en question n'a pas encore ete devoile
                    				if(!cs.getFlag())//si elle n'a pas de drapeau
                    				{
                    					Boom--;//reduire le nombre de drapeau a utiliser
                    					cs.setFlag(true);//un drapeau a ete mit
                                		imagi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/flag.bmp"));
                    				}
                    				else
                    				{
                    					Boom++;//augmenter le nombre de drapeau a utiliser
                    					cs.setFlag(false);//un drapeau a ete retire
                                		imagi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/init.bmp"));   
                    				}
                    				BoomReveal();
                    				grid.add(imagi, colIndex, rowIndex);
                    				//remise de gestionnaires d'evenements sur les cases cliquees, car les precedents ayant deja utilises
                    				imagi.setOnMousePressed(imag.getOnMousePressed());
                    				imagi.setOnMouseReleased(imag.getOnMouseReleased());
        						}	
                    	}
                    }
                });
				imag.setOnMouseReleased(new EventHandler<MouseEvent>() 
				{
					@Override
                    public void handle(MouseEvent event) {
						if(!fly && playa)
						{
							Node source = (Node) event.getSource();
	                        Integer colIndex = GridPane.getColumnIndex(source);
	                        Integer rowIndex = GridPane.getRowIndex(source);
	                        ImageView imagi = new ImageView();
	                        imagi.setFitWidth(33);
	        				imagi.setFitHeight(33);
	        				
	        				for(Case cs: cases)
	        					//si c'est la case en question et qu'elle n'a pas de drapeau
                    			if(cs.getCol()==colIndex && cs.getRow()==rowIndex && !cs.getFlag())
                    			{
                    				imagi = Reveal(rowIndex,colIndex);
        	        				Alert dial = new Alert(AlertType.CONFIRMATION);
        	        				ImageView ic = new ImageView();
    		                        ic.setFitWidth(40);
    		        				ic.setFitHeight(40);
        	        				if(mat[rowIndex+1][colIndex+1]==-1)//si elle contient une bombe
        	        				{
        	        					playa = false;//arret du jeu
        	        					ima.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/perte.bmp"));
        	        					grid.add(imagi, colIndex, rowIndex);
        	        					for(Case css: cases)
        	        					{	//devoiler toutes les bombes du terrain qui n'ont pas ete signale par un drapeau
        	        						ImageView image = new ImageView();
            		                        image.setFitWidth(33);
            		        				image.setFitHeight(33);
        	        						if(!css.getFlag() && css.getValue()==-1)
    	        							{
    	        		        				image.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/bomb.bmp"));
    	        								grid.add(image, css.getCol(), css.getRow());
    	        							}
        	        						if(css.getFlag() && css.getValue()!=-1)
        	        						{
        	        							image.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/nope.bmp"));
    	        								grid.add(image, css.getCol(), css.getRow());
        	        						}
        	        							
        	        					}
        	        					timer.stop();
        	        					//boite de dialogue indiquant la defaite
        	        					dial.setTitle("BAD BAD BAD");
        	    						dial.setHeaderText(null);
        	    						dial.setContentText("YOU KNOW A LOSER IS ALSO A WINNER\nA WINNER WHO FOUND A BOMB");
        	    						ic.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/ic_perte.png"));
        	    						dial.setGraphic(ic);
        	    						dial.showAndWait();
        	    						return;			
        	        				}
        	        				else	
        	        				{	//si ce n'est pas une bombe
        	        					ima.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/pose.bmp"));
        	        					grid.add(imagi, colIndex, rowIndex);
        	        					
        	        					if(mat[rowIndex+1][colIndex+1]==0)//si la case est vide
        	        					{//devoiler les cases alentour et garder les emplacements des cases vides alentours
        	        						ArrayList<Case> scases = new ArrayList<>();
        	        						for(Case css: cases)
        	        							if(css.getCol()==colIndex && css.getRow()==rowIndex)
        	        									scases.add(css);//garder la casse qu'on vient de cliquer pour reveler ses alentours
        	    	        				ReReveal(scases);//donner cette case a la fonction charger de reveler ses alentours
        	        					}
        	        					
        	        					boolean play = false;
        	        					for(Case css: cases)
        	        						//verifier si il y'a encore des cases ne comportant pas de bombe qui n'ont pas encore devoilees
        	        						if(css.getValue()!=-1 && css.getReveal()==false)
        	        							play = true;
        	        					if(play==false)
        	        					{	//boite de dialogue indiquant la victoire
        	        						timer.stop();
        	        						playa = false;
        	        						ima.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/gain.bmp"));
        	        						dial.setTitle("VICTORY");
        	        						dial.setHeaderText(null);
        	        						dial.setContentText("NICE GAME!!!!\nYOU ARE UN-BOOM-TABLE");
        	        						ic.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/ic_gain.png"));
            	    						dial.setGraphic(ic);//charge une image comme icone de la boite de dialogue
        	        						dial.showAndWait();
        	        						Verify();//verifie si le temps de jeu fait parti du Top 10
        	        						
        	        						return;//permet de sortir directement de la fonction
        	        					}
        	        				}
                    			}
						}
					}
				});
			}
		
		int col, row, bomb=0;
		while(bomb<Boom)
		{//mise des bombes aleatoirement sur le plateau
			col =(int)(Math.random()*(grid.getColumnCount()+1));
			row =(int)(Math.random()*(grid.getRowCount()+1));
			
			if(mat[row][col]==0)
			{
				mat[row][col] = -1;
				bomb++;
			}
		}
		for(int i=1;i<grid.getRowCount()+1;i++)
			for(int j=1;j<grid.getColumnCount()+1;j++)
			{
				if(mat[i][j] == 0)
				{//donner a une case la valeur du nombre de bombe qu'il y'a a ses alentours si celle xi n'est pas une bombe
					int cmpt = 0;
					for(int i1=i-1;i1<i+2;i1++)
						for(int j1=j-1;j1<j+2;j1++)
							if(mat[i1][j1]==-1)
								cmpt++;			
					mat[i][j] = cmpt;
				}
				//Initialisation de la liste de Case avec tt les cases representant notre zone de jeu
				cases.add(new Case(i-1,j-1,mat[i][j]));
			}
	}
	//donne l'image correspondante a la case en fonction de la valeur quelle contient
	public ImageView Reveal(int i,int j)
	{
		ImageView imagi = new ImageView();
        imagi.setFitWidth(33);
		imagi.setFitHeight(33);
		switch(mat[i+1][j+1])
		{
			case -1:
				imagi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/bomb.bmp"));					
				break;
			case 0:
				imagi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/blank.bmp"));
				break;
			case 1:
				imagi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/num1.bmp"));
				break;
			case 2:
				imagi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/num2.bmp"));
				break;
			case 3:
				imagi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/num3.bmp"));
				break;
			case 4:
				imagi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/num4.bmp"));
				break;
			case 5:
				imagi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/num5.bmp"));
				break;
			case 6:
				imagi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/num6.bmp"));
				break;
			case 7:
				imagi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/num7.bmp"));
				break;
			case 8:
				imagi.setImage(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/num8.bmp"));
				break;
		}
		return imagi;
	}
	//fonction recurssive permettant de reveler des cases tout au tour d'une case vide
	//et continue cette operation tant qu'il y'a des cases vides dans les cases reveler
	public void ReReveal(ArrayList<Case> caser)
	{
		if(caser.isEmpty())//condition de sortie de la recurssivite, si il n'ya plus de case vide a son voisinage
			return;
		ArrayList<Case> scases = new ArrayList<>();
		for(Case cs : caser)//pour toutes les cases de la liste donnee en parametre
		{
			int row = cs.getRow()+1;
			int col = cs.getCol()+1;
			for(int i=row-1;i<row+2;i++)
				for(int j=col-1;j<col+2;j++)//on parcours son voisinage
				{
					ImageView image = new ImageView();
	                image.setFitWidth(33);
					image.setFitHeight(33);
					if(mat[i][j]!=-1)//si ce n'est pas une bombe
					{
						for(Case css: cases)
							//si la case n'a pas encore ete revelee et ne possede pas de drapeau 
							if(css.getCol()==j-1 && css.getRow()==i-1 && !css.getFlag() && !css.getReveal())
							{
								if(css.getValue()==0)//si elle est vide on garde sa valeur dans la liste
									scases.add(css);
								
								css.setReveal(true);//signale qu'elle est revelee
								image = Reveal(i-1,j-1);
								grid.add(image, j-1, i-1);
							}
					}
				}
			ReReveal(scases);//repeter le processus avec la nouvelle liste obtenue apres le devoilement d'une case
		}
	}
	//affiche le nombre de drapeau max a utiliser
	public void BoomReveal()
	{
		if(Boom>=10)
			sc.setText("0"+Boom);
		if(Boom<10 && Boom>=0)
			sc.setText("00"+Boom);
		if(Boom<0)
		{
			int boomer = -Boom;
			if(boomer>=10)
				sc.setText("-0"+boomer);
			if(boomer<10 && boomer>=0)
				sc.setText("-00"+boomer);
		}
	}
	//permet de sauvegarder dans le fichier glorious.txt un joueur dont le temps est dans le Top 10
	public void Save(String pseudo)
	{//creer un Victorious ayant pour rang le dernier de la liste et pour temps celui du jeu
		Victorious player = new Victorious(glory.size()+1,pseudo,cnt);
		if(glory.size()!=0)//si il y'a deja au moins un victorieux dans la liste
		{
			if(glory.size()==10)//si il y'a exactement 10 victorieux dans la liste
			{
				for(Victorious pastV : glory)
				{
					if(player.getTime()<=pastV.getTime())//chercher ou placer le nouveau Victorious dans la liste
					{//changer son rang et celui de la personne qui avait ce rang avant lui
						glory.add(pastV.getRang()-1,player);
						player.setRang(pastV.getRang());
						pastV.setRang(pastV.getRang()+1);
						for(int i=pastV.getRang();i<glory.size();i++)
						{//baisser le rang de tous ceux apres de 1
							if(glory.get(i).getRang()!=10)
							glory.get(i).setRang(glory.get(i).getRang()+1);
						}
						break;
					}
				}//supprimer le dernier de la liste, car nous ne voulons que garder les 10 meilleurs temps
				glory.remove(glory.size()-1);
			}
			
			if(glory.size()<10)//s'il y'a moins de 10 victorieux dans la liste
			{
				glory.add(player);//ajouter d'abord le Victorious
					for(int i=0;i<glory.size()-1;i++)//ranger la liste des Victorious en fonction du temps de la partie
						if(glory.get(i+1).getTime()<glory.get(i).getTime())
						{
							glory.get(i+1).setRang(glory.get(i).getRang());
							glory.get(i).setRang(glory.get(i).getRang()+1);
							Victorious helper = glory.get(i);
							glory.set(i, glory.get(i+1));
							glory.set(i+1, helper);
							i = -1;
						}		
			}
		}
		else
			glory.add(player);//si la liste de Victorieux est vide, ajouter tout simplement le Victorieux
			
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File("glorious.txt")))){
			//conserver la liste des Victorious dans le ficher glorious.txt
			for(Victorious newV : glory)
			{
				writer.write(newV.toString());
			    writer.newLine();//aller a la ligne
			}		
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	//copie dans la liste des Victorious et la liste des musiques le contenu de glorious.txt et de music.txt respectivement
	public void Copy()
	{
		String ligne;
		try(BufferedReader reader = new BufferedReader(new FileReader(new File("glorious.txt")))){
			
			while( (ligne = reader.readLine()) != null )
				glory.add(new Victorious(ligne));
			
		}catch(Exception e) {
			//pour empecher la generation d'exception au cas ou le fichier ne soit vide
		}
        try(BufferedReader reader = new BufferedReader(new FileReader(new File("music.txt")))){
			
			while( (ligne = reader.readLine()) != null )
				musics.add(ligne);
			
			Media sound = new Media(musics.get(parcours));
			mediaPlayer = new MediaPlayer(sound);
			MediaPlayer mp = new MediaPlayer(sound);
			mp.setOnEndOfMedia(() -> {
				parcours++;
				if(parcours == musics.size())
					parcours = 0;
				Media soundi = new Media(musics.get(parcours));
				mediaPlayer = new MediaPlayer(soundi);
				mediaPlayer.setOnEndOfMedia(mp.getOnEndOfMedia());
				mediaPlayer.play();	           
            });
			mediaPlayer.setOnEndOfMedia(mp.getOnEndOfMedia());
			mediaPlayer.play();	
		}catch(IOException e) {
			e.printStackTrace();
		}
        
	}
	public void Verify()
	{
		try {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("TOP 10");
	        dialog.setHeaderText("PLEASE ENTER YOUR GLORIOUS NAME"); 
	        dialog.setGraphic(new ImageView(new Image("C://Users/dell/eclipse-workspace/Demineur/Images/ic_gain.png")));
	        String pseudo = " ";
			if(glory.size()<10)//s'il n'y a pa encore 10 personnes dans la liste de Victorious
			{//verifier qu'il saisie bien un pseudo pour la sauvegarde dans le fichier
				do
				{
					pseudo = dialog.showAndWait().get();
				}while(pseudo.compareTo("")==0);
				
				Save(pseudo);//sauvegarder
			}
			else
				for(Victorious pastV : glory)
					if(cnt<pastV.getTime())
					{//verifier qu'il saisie bien un pseudo pour la sauvegarde dans le fichier
						do
						{
							pseudo = dialog.showAndWait().get();
						}while(pseudo.compareTo("")==0);
						
						Save(pseudo);//sauvegarder
					}
		}catch(Exception e) {
			e.getStackTrace();
		}
	}
}
