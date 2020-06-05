package com.bigbeautifulchess.engine;

import java.util.List;

import com.bigbeautifulchess.domain.Game;
import com.bigbeautifulchess.domain.User;

public class CurrentBoard implements BoardInterface{
	
	private static Board b;
	private static Piece selectedPiece;
	private static int nbTours = 0;
	
	private static User myself;
	private static User opponent;
	@Override
	public User getWhitePlayer() {
		return myself;
	}
	@Override
	public User getBlackPlayer() {
		return opponent;
	}
	@Override
	public Board getBoard() {
		return b;
	}
	@Override
	public void resetBoard() {
		b = new Board();
		nbTours = 0;
		selectedPiece = null;
	}
	@Override
	public void loadBoard(Game g) {
		b = new Board(g.getBoard_info(), g.getNb_turn()%2, g.getFlag_winner(), g.getTime_user2(), g.getTime_user1(), g.getStorage(), g.getMouv());
		List<User> usrs = g.getUsers();
		if(usrs.size() == 2) {
			myself = usrs.get(0);
			opponent = usrs.get(1);
		}
	}
	
	@Override
	public void setSelectedPiece(Piece p) {
		selectedPiece = p;
	}
	@Override
	public void setSelectedPiece(int x, int y) {
		selectedPiece = b.getPieceOnCell(x, y);
	}
	@Override
	public void incrementTurns() {
		nbTours++;
	}
	@Override
	public void setWhitePlayer(User u) {
		myself = u;
	}
	@Override
	public void setBlackPlayer(User u) {
		opponent = u;
	}
	@Override
	public int getNbTurns() {
		return nbTours;
	}
	@Override
	public Piece getSelectedPiece() {
		return selectedPiece;
	}
	@Override
	public void emptySelectedPiece() {
		selectedPiece = null;
	}
	
}
