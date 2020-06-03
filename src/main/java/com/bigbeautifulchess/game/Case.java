package com.bigbeautifulchess.game;

public class Case {
	private int type;
	private int id;
	//EMPTY / VALUE OR NUMBER
	private boolean visible;
	private boolean flagged;
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void setValue(int value) {
		this.type = value;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}
	
	public Case(int type, int id) {
		this.type = type;
		this.flagged = false;
		this.visible = false;
		this.id = id;
	}

	@Override
	public String toString() {
		String ret = "";
		if(flagged) {
			ret = "🚩";
		}
		else if(visible){
			switch(type) {
				case Type.EMPTY:
					ret = "";
					break;
				
				case Type.BOMB:
					ret = "💣";
					break;
				
				default:
					ret = Integer.toString(type);
					break;
			}
		}
		
		return ret;
	}

	public int getId() {
		return id;
	}
	
	public boolean isEmpty() {
		return (type == Type.EMPTY) ? true : false;
	}
	
	public boolean isBomb() {
		return (type == Type.BOMB) ? true : false;
	}
	
	public boolean isFlagged() {
		return flagged;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	//Renvoie la classe CSS associée à la valeur de case
	public String getColorClass() {
		String col ="unvisibleCase";
		if(isVisible()) {
			if(isEmpty()) col = "emptyCase";
			else if(isBomb()) col = "bombCase";
			else {
				switch(type) {
				case 1:
					col = "lowDangerCase";
					break;
					
				case 2:
					col = "moderateDangerCase";
					break;
					
				case 3:
					col = "highDangerCase";
					break;
					
				default:
					col = "maxDangerCase";
					break;
				}
			}
		}
		return col;
	}
	
	
}
