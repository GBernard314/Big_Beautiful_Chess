package com.bigbeautifulchess.engine;

import com.bigbeautifulchess.domain.Game;
import com.bigbeautifulchess.domain.User;

public interface BoardInterface {
	public User getWhitePlayer();
	public User getBlackPlayer();
	
	public void setWhitePlayer(User u);
	public void setBlackPlayer(User u);
	
	public Board getBoard();
	
	public void resetBoard();
	public void loadBoard(Game g);
	public void setSelectedPiece(Piece p);
	public void setSelectedPiece(int x, int y);
	
	public Piece getSelectedPiece();
	
	public void incrementTurns();
	public void emptySelectedPiece();
	public int getNbTurns();
}
