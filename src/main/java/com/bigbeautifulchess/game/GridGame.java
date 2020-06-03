package com.bigbeautifulchess.game;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GridGame {
	private int width;
	private int height;
	private int nbMine;
	private long startTime;
	
	public boolean lost = false;
	private long totalTime = 0;
	
	private Case[] cases; 
	
	//Constructeur :: initialisation (utile pour les chargements et nouveaux jeux)
	public GridGame(int large, int haut, int nombreMines, boolean init) {
		this.width = large;
		this.height = haut;
		int nbCases = getNombreCases();

		if(nombreMines >= nbCases) { 
			System.out.println("ERREUR :: MineSweeper :: Plus de bombes que de cases ! ");
			System.out.println("RESOLVE :: MineSweeper :: Nombre de bombes saturé au nombreCases-1 ");
			this.nbMine = nbCases-1;
		}else {
			this.nbMine = nombreMines;
		}
		
		//Instancie la grille (initialisation objet)
		cases = new Case[nbCases];
		
		if(init == true) {
			//Initialise la grille
			for(int i=0; i< nbCases; i++) {
				cases[i] = new Case(Type.EMPTY, i);
			}
		}
		//FIN
	}
	
	//Constructeur :: creation classique : nouveau jeu
	public GridGame(int large, int haut, int nombreMines) {
		this(large, haut, nombreMines, true);
		
		//Place les mines aléatoirement
		placeMines(this.nbMine);
		
		//Estime le danger pour chaque case
		placeNumeros();
		
		this.startTime = (long)(System.currentTimeMillis()/1000);
		
	}
	
	//Constructeur :: tableau déja existant
	public GridGame(int large, int haut, int nombreMines, Case[] tabCases) {
		this(large, haut, nombreMines, false);
		this.setCases(tabCases);
	}
	
	//Constructeur :: initialisation + tableaux de coordonnées mines et de cases visibles
		public GridGame(int large, int haut, List<Integer> tabIdMines, List<Integer> tabIdVisibles, List<Integer> flaggedIds, long timeElapsed) {
			this(large, haut, tabIdMines.size(), true); //Initialisation des cases à vide.
			int nbCases = getNombreCases();
			
			//Loop : bombes
			for(int i=0; i<tabIdMines.size(); i++) {
				int id = tabIdMines.get(i).intValue();
				//Si l'id precisé est valable
				if(id < nbCases ) {
					this.cases[id].setType(Type.BOMB);
				}
			}
			//Evaluation du danger:
			placeNumeros();
			
			//Plantation des drapeaux
			for(int i=0; i< flaggedIds.size(); i++) {
				//Si l'id precisé est valable
				int id = flaggedIds.get(i).intValue();
				if(id < nbCases ) {
					cases[id].setFlagged(true);
				}
			}
			
			//Affichage des cases visibles
			for(int i=0; i< tabIdVisibles.size(); i++) {
				//Si l'id precisé est valable
				int id = tabIdVisibles.get(i).intValue();
				if(id < nbCases ) {
					if(!cases[id].isFlagged()) cases[id].setVisible(true);
				}
			}
			
			//Affectation du temps écoulé: tempsActuel - tempsEcoule
			long tNow = (long)(System.currentTimeMillis()/1000);
			if(tNow - timeElapsed > 0)
				tNow -= timeElapsed;
			startTime = tNow;
		}
	
	public int getNombreCases() {
		//Retourne le nombre total de cases de notre grille
		return width*height;
	}
	
	public int getNombreCasesDecouvertes() {
		int nb = 0;
		for(int i=0; i< getNombreCases(); i++) {
			if(cases[i].isVisible()) nb++;
		}
		return nb;
	}
	
	public int retourneChance() {
		//Retourne la chance de victoire en pourcents
		int chance =  (int)((1 - ((float)nbMine/(float)(getNombreCases() - getNombreCasesDecouvertes()) )) * 100);
		if(chance == 0) {
			//victoire ->
			totalTime = getElapsedTime();
		}
		return chance;
	}
	
	/*Place les bombes sur la grille, de maniere aleatoire */
	private void placeMines(int nb) {
		int nbcase = getNombreCases();
		
		//tant qu'il reste des bombes à planter, on genere un numero de case aleatoire
		for(int i=0; i<nb; i++) {
			int numeroCase;
			do {
				numeroCase = new Random().nextInt(nbcase);
			}
			while( cases[numeroCase].getType() != Type.EMPTY);
			
			cases[numeroCase].setType(Type.BOMB);
		}
	}
	
	/*Place les indicateurs de dangers (1,2,3...) */
	private void placeNumeros() {
		for(int i=0; i< getNombreCases(); i++) {
			if(cases[i].getType() == Type.EMPTY)
				cases[i].setValue(calculeAdjacent(i));
		}
	}
	
	private Case[] mapAdjacents(int id) {
		Case[] adjacentes = new Case[8];
		//On mappe la case courante:
				// 0: #id-1 - width		1: #id - width		2: #id+1 - width
				// 3: #id-1 			X: #id				4: #id+1
				// 5: #id-1 + width		6: #id + width		7: #id+1 + width
		//Cas de bords:
		boolean notTop = 	(id >= width)? true : false;
		boolean notBottom = (id < (height-1)*width)? true : false;
		boolean notLeft = 	(id % width == 0) ? false : true;
		boolean notRight =  ( (id+1) % width == 0)? false : true;
		
		if(notTop) {
			if(notLeft) { adjacentes[0] = cases[id-1 - width]; }
			adjacentes[1] = cases[id - width];
			if(notRight) { adjacentes[2] = cases[id+1 - width];	}
		}
		
		if(notLeft){ adjacentes[3] = cases[id-1];} 
		if(notRight) { adjacentes[4] = cases[id+1];	}
		
		if(notBottom) {
			if(notLeft){ adjacentes[5] = cases[id-1+ width];	}
			adjacentes[6] = cases[id+   width];
			if(notRight && (id+1+ width) < getNombreCases() ){ adjacentes[7] = cases[id+1+ width];	}
		}
		
		return adjacentes;
	}
	
	public int calculeAdjacent(int id) {
		int danger = 0;
		
		for (Case case_adjacente : mapAdjacents(id)) 
		{ 
			if(case_adjacente != null) {
				if(case_adjacente.getType() == Type.BOMB) {
			    	danger++;
			    }
			}
		}
		
		return danger;
	}
	
	public void print() {
		 System.out.println('\n');
		for(int i=0; i < getNombreCases(); i++) {
			System.out.print(cases[i]);
			if(i%width == 0) System.out.print('\n');
		}
	}

	//Recupere toutes les cases
	public Case[] getCases() {
		return cases;
	}
	
	//Recupere une liste de cases
	private Case[] linearGetCases(int start, int end) {
		return Arrays.copyOfRange(cases,  start, end);
	}
	
	//Recupere une ligne
	public Case[] getLine(int rowNum) {
		 return linearGetCases(rowNum, rowNum+width);
	}
	
	//Recupere une liste de cases avec offset 
	private Case[] getCaseListByOffset(int start, int number, int offset) {
		Case[] arr = new Case[number];
		for(int i=0; i < number; i++) {
			
			arr[i] = cases[start + offset*i];
		}
		return arr;
	}

	public Case[] getFirstCasePerRow() {
		return getCaseListByOffset(0, height, width);
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getNbMine() {
		return nbMine;
	}
	
	public void clickOnCase(int id) {
		Case cur = cases[id];
		if(!cur.isFlagged()) {
			cur.setVisible(true);
			if(cur.getType() == Type.EMPTY) {
				for(Case adj : mapAdjacents(id)) {
					if(adj != null) {
						if(adj.getType() == Type.BOMB) {
							return;
						}else if(adj.getType() == Type.EMPTY && adj.isVisible()==false){
							clickOnCase(adj.getId());
						}
						else {
							adj.setVisible(true);
						}
					}
				}
			}
		}
	}
	
	public void flagCase(int id) {
		Case cur = cases[id];
		if(cur.isFlagged()) {
			cur.setFlagged(false);
		}
		else {
			cur.setFlagged(true);
		}
	}
	
	public Case getCase(int id) {
		return cases[id];
	}
	
	public void revealCases() {
		for(Case c : cases) {
			c.setVisible(true);
		}
	}
	
	public void lose() {
		totalTime = getElapsedTime();
		revealCases();
		this.lost = true;
	}
	
	public long getElapsedTime() {
		//Retourne le temps écoulé depuis le debut de la partie
		if(lost || totalTime>0) {
			//En cas de partie terminée, le temps retourné est statique
			return totalTime;
		}
		//Retour du temps écoulé, en secondes
		return ((long) (System.currentTimeMillis()/1000) - startTime);
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public void setCases(Case[] cases) {
		this.cases = cases;
	}
	
	/**
	 * Exporte les paramètres de jeu en une chaine exploitable
	 * @return exploitableStringCode
	 */
	public String exportToString() {
		StringBuffer sb = new StringBuffer();
		sb.append(Integer.toString(width)+":");
		sb.append(Integer.toString(height)+":");
		
		StringBuffer bombBuff = new StringBuffer();
		int nbBomb=0;
		bombBuff.append('(');
		StringBuffer visBuff = new StringBuffer();
		visBuff.append('(');
		int nbVis=0;
		StringBuffer flagBuff = new StringBuffer();
		flagBuff.append('(');
		int nbFlag = 0;
		for(int i=0; i< getNombreCases(); i++) {
			if(getCase(i).isBomb()) {
				if(nbBomb == 0) {
					nbBomb++;
				}else {
					bombBuff.append(',');
				}
				bombBuff.append(Integer.toString(i));
			}
			if(getCase(i).isVisible()) {
				if(nbVis == 0) {
					nbVis++;
				}else {
					visBuff.append(',');
				}
				visBuff.append(Integer.toString(i));
			}
			if(getCase(i).isFlagged()) {
				if(nbFlag == 0) {
					nbFlag++;
				}else {
					flagBuff.append(',');
				}
				flagBuff.append(Integer.toString(i));
			}
		}
		bombBuff.append(')');
		visBuff.append(')');
		flagBuff.append(')');
		
		
		sb.append(bombBuff);
		sb.append(':');
		sb.append(visBuff);
		sb.append(':');
		sb.append(flagBuff);
		sb.append(':');
		sb.append(Long.toString(getElapsedTime()));
		
		return sb.toString();
	}
	
	
}
