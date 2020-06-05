package com.bigbeautifulchess.san;

import java.util.ArrayList;

import com.bigbeautifulchess.engine.Board;
import com.bigbeautifulchess.tools.Mov;

public class San {
	
	private ArrayList<Mov> historic;
	
	public San() {}
	
	public String printSan(Board b) {
		String s= new String();
		historic=b.getHistoric();
		for (int i = 0; i < this.historic.size(); i++)
		{
			
			char name1 = name(this.historic.get(i).getP1());
			char l1 = letter(this.historic.get(i).getFrom().getY());
			
			char name2 = name(this.historic.get(i).getP2());
			char l2 = letter(this.historic.get(i).getTo().getY());
			int nb2 = number(this.historic.get(i).getTo().getX());
			
			
			if(b.isBigCastlingOk()){
				s=s+"0-0-0";
			}else if(b.isSmallCastlingOk()){
				s=s+"0-0";
			}else{
				//King
				if(name1=='K' && name2!=' '){
					s=s+name1;
					s=s+'x';
					s=s+l2;
					s=s+nb2;
				}else if(name1=='K'){
					s=s+name1;
					s=s+l2;
					s=s+nb2;
				}
				
				//Queen
				if(name1=='Q' && name2!=' '){
					s=s+name1;
					s=s+'x';
					s=s+l2;
					s=s+nb2;
				}else if(name1=='Q'){
					s=s+name1;
					s=s+l2;
					s=s+nb2;
				}
				
				//Rook
				if(name1=='R' && name2!=' '){
					s=s+name1;
					s=s+'x';
					s=s+l2;
					s=s+nb2;
				}else if(name1=='R'){
				 	s=s+name1;
					s=s+l2;
					s=s+nb2;
				}
				
				//Bishop
				if(name1=='B' && name2!=' '){
					s=s+name1;
					s=s+'x';
					s=s+l2;
					s=s+nb2;
				}else if(name1=='B'){
					s=s+name1;
					s=s+l2;
					s=s+nb2;
				}
				
				//Knight
				if(name1=='N' && name2!=' '){
					s=s+name1;
					s=s+'x';
					s=s+l2;
					s=s+nb2;
				}else if(name1=='N'){
					s=s+name1;
					s=s+l2;
					s=s+nb2;
				}
				
				//Pawn
				if(name1=='P' && (nb2==8||nb2==1) && name2!=' '){
					s=s+l1;
					s=s+'x';
					s=s+l2;
					s=s+nb2;
					s=s+"=Q";
				}else if(name1=='P' && (nb2==8||nb2==1)){
					s=s+l2;
					s=s+nb2;
					s=s+"=Q";
				}else if(name1=='P' && name2!=' '){
					s=s+l1;
					s=s+'x';
					s=s+l2;
					s=s+nb2;
				}else if(name1=='P'){
					s=s+l2;
					s=s+nb2;
				}
				
			}
			
			if(b.isCheckMate()) {
				s=s+'#';
			}
			else if(b.isChecked()) {
				s=s+'+';
			}
			
			s=s+'\n';
			
			switch (b.getResult()) {
			case 0://draw
				s=s+"½-½";
				break;
			case 1://player 1 win -> white
				s=s+"1-0"+'\n';
				break;
			case 2://player 2 win -> black
				s=s+"0-1"+'\n';
				break;
			}
		}
		return s;
	}
	
	private char letter(int nb) {
		char x1=' ';
		switch (nb) {
		case 0:
			x1='a';
			break;
		case 1:
			x1='b';
			break;
		case 2:
			x1='c';
			break;
		case 3:
			x1='d';
			break;
		case 4:
			x1='e';
			break;
		case 5:
			x1='f';
			break;
		case 6:
			x1='g';
			break;
		case 7:
			x1='h';
			break;
		default:
			break;
		}
		return x1;
	}
	
	private int number(int nb) {
		int x1=0;
		switch (nb) {
		case 0:
			x1=8;
			break;
		case 1:
			x1=7;
			break;
		case 2:
			x1=6;
			break;
		case 3:
			x1=5;
			break;
		case 4:
			x1=4;
			break;
		case 5:
			x1=3;
			break;
		case 6:
			x1=2;
			break;
		case 7:
			x1=1;
			break;
		default:
			break;
		}
		return x1;
	}
	
	private char name(char letter) {
		char res=' ';// it's impossible to have nothing in return
		switch (letter) {
		case 'k':
			res='K';//King
			break;
		case 'q':
			res='Q';//Queen
			break;
		case 'r':
			res='R';//Rook
			break;
		case 'b':
			res='B';//Bishop
			break;
		case 'h':
			res='N';//Knight the horse
			break;
		case 'p':
			res='P';//Pawn
			break;
		case 'e':
			res=' ';//Empty
			break;
		default:
			break;
		}
		return res;
	}
	
}
