package com.bigbeautifulchess.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bigbeautifulchess.domain.Game;
import com.bigbeautifulchess.domain.User;
import com.bigbeautifulchess.engine.Board;
import com.bigbeautifulchess.engine.BoardInterface;
import com.bigbeautifulchess.engine.CurrentBoard;
import com.bigbeautifulchess.engine.Piece;
import com.bigbeautifulchess.repository.GameRepository;
import com.bigbeautifulchess.repository.UserRepository;
import com.bigbeautifulchess.san.San;
import com.bigbeautifulchess.service.AjaxResponse;

@Controller
@RequestMapping("/game")
public class GameController {
	
	private BoardInterface boardInterface;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	GameRepository gameRepository;

	@PersistenceContext
	EntityManager em;
	
	
	@GetMapping("/new/{adversaire}")
	public String initGame(Model model, Authentication authentication, @PathVariable String adversaire){
		boardInterface = new CurrentBoard();
		User white = userRepository.findByUsername(authentication.getName());
		User black = userRepository.findByUsername(adversaire);
		boardInterface.setWhitePlayer(white);
		boardInterface.setBlackPlayer(black);
		
		return "redirect:/game/reset";
	}
	
	@GetMapping("/load/{gameid}")
	public String loadGame(Model model, Authentication authentication, @PathVariable Long gameid){
		boardInterface = new CurrentBoard();
		Optional<Game> probableGame = gameRepository.findById(gameid);
		if(probableGame.isPresent()) {
			//Partie identifiée
			boardInterface.loadBoard(probableGame.get());
			return "redirect:/game/play";
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/visualize/{gameid}")
	public String visualizeGame(Model model, Authentication authentication, @PathVariable Long gameid){
		boardInterface = new CurrentBoard();
		Optional<Game> probableGame = gameRepository.findById(gameid);
		if(probableGame.isPresent()) {
			boardInterface.loadBoard(probableGame.get());
			
			if(boardInterface.getBoard() == null) {
				return "redirect:/game/reset";
			}
			if(boardInterface.getWhitePlayer() == null) {
				return "redirect:/";
			}
			model.addAttribute("board", boardInterface.getBoard());
			
			San san = new San();
			model.addAttribute("boardSan", san.printSan(boardInterface.getBoard()));
			
			User user = userRepository.findByUsername(authentication.getName());
			model.addAttribute("userInfo", user);
			model.addAttribute("friends", getFriends(user));
			
			if(boardInterface.getWhitePlayer() != null )
				model.addAttribute("myself",boardInterface.getWhitePlayer());
			if(boardInterface.getBlackPlayer() != null)
				model.addAttribute("opponent",boardInterface.getBlackPlayer());
			
			return "visualise";
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/drop/{gameid}")
	public String dropGame(Model model, Authentication authentication, @PathVariable Long gameid){
		Optional<Game> probableGame = gameRepository.findById(gameid);
		if(probableGame.isPresent()) {
			gameRepository.deleteById(gameid);
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/save")
	public String saveGameRepo(){
		List <User> users = new ArrayList<>();

		users.add(boardInterface.getWhitePlayer());
		users.add(boardInterface.getBlackPlayer());
		
		Board b = boardInterface.getBoard();
		Game game = new Game();
		game.setId(null);
		game.setFlag_winner(b.getResult());
		game.setBoard_info(b.cellsToString());
		game.setForfeit(false); //TODO : forfait
		game.setNb_turn(boardInterface.getNbTurns());
		game.setTime_user1(b.getTime_white());
		game.setTime_user2(b.getTime_black());
		game.setUsers(users);
		game.setStorage(b.storageToString());
		game.setMouv(b.historicToString());
		
		gameRepository.save(game);
		
		if(b.getResult()>0) {
			return "redirect:/game/history";
		}
		return "redirect:/game/ongoing";
	}

	/*Renvoie une liste de parties - le boooléen ongoing indique si l'on désire les parties en cours(true) ou terminées(false) */
	private List<Game> getGames(User user, Boolean ongoing) {
		List<Game> games = new ArrayList<>();
		if(user != null) {
			int finishedParam;
			if(ongoing == true) finishedParam = -1;
			else finishedParam = 1;
			games = em.createQuery("select g from Game g JOIN g.users us where g.flag_winner !=:value and us.id = :valuename ",Game.class)
					.setParameter("value", finishedParam)
					.setParameter("valuename", user.getId())
					.getResultList();
		}
		return games;
	}
	
	/*Renvoie une liste d'User représentant les amis de l'user passé en paramètre */
	private List<User> getFriends(User user){
		List<User> userlist = new ArrayList<>();
		if(user != null) {
		  String[] friend_list;
		  if(user.getFriends_list_id() != null) {
			  friend_list = user.splitFriendsList();
			  for(String a : friend_list) {
				  List <User> u = em.createQuery("select u from User u where u.id =:value",User.class)
						  			.setParameter("value", Long.parseLong(a))
						  			.getResultList();
				  userlist.add(u.get(0));
			  }
		  }
		}
		return userlist;
	}
	
	@GetMapping("/history")
	public String history(Model model, Authentication authentication) {
		User user = userRepository.findByUsername(authentication.getName());
		
		model.addAttribute("label", "Historique des parties");
		model.addAttribute("userInfo", user);
		model.addAttribute("friends", getFriends(user));
		model.addAttribute("ongoing",false);
		model.addAttribute("gameList", getGames(user, true));
		
		return "game-history";
	}
	
	@GetMapping("/ongoing")
	public String ongoing(Model model, Authentication authentication) {
		User user = userRepository.findByUsername(authentication.getName());
		
		model.addAttribute("label", "Parties en cours");
		model.addAttribute("userInfo", user);
		model.addAttribute("friends", getFriends(user));
		model.addAttribute("gameList", getGames(user, false));
		model.addAttribute("ongoing",true);
		return "game-history";
	}
	
	@GetMapping("/play")
	public String play(Model model, Authentication authentication) {
		boardInterface = new CurrentBoard();
		if(boardInterface.getBoard() == null) {
			return "redirect:/game/reset";
		}
		if(boardInterface.getWhitePlayer() == null) {
			return "redirect:/";
		}
		model.addAttribute("board", boardInterface.getBoard());
		
		Piece selectedPiece = boardInterface.getSelectedPiece();
		model.addAttribute("pieceInSelection", selectedPiece);
		if(selectedPiece != null) {
			model.addAttribute("moves", selectedPiece.getMoves());
		}
		
		User user = userRepository.findByUsername(authentication.getName());

		model.addAttribute("userInfo", user);
		model.addAttribute("friends", getFriends(user));
		if(boardInterface.getWhitePlayer() != null )
			model.addAttribute("myself",boardInterface.getWhitePlayer());
		if(boardInterface.getBlackPlayer() != null)
			model.addAttribute("opponent",boardInterface.getBlackPlayer());
		
		System.out.println("is small castling ok ? " + boardInterface.getBoard().isSmallCastlingOk());
		System.out.println("is big castling ok ? " +  boardInterface.getBoard().isBigCastlingOk());
		System.out.println("Egalite ? " +  boardInterface.getBoard().draw());
		
		return "play";
	}
	
	@GetMapping("/reset")
	public String reset(Model model) {
		boardInterface.resetBoard();
		return "redirect:/game/play";
	}
	
	@GetMapping("/play/select/{x},{y}")
	public String selectPiece( @PathVariable int x,@PathVariable int y,Model model, Authentication auth) {
		Board b = boardInterface.getBoard();
		Piece selectedPiece = boardInterface.getSelectedPiece();
		if(b == null) {
			return "redirect:/game/reset";
		}
		Piece clickedCell = b.getPieceOnCell(x,y);
		boolean isopponent = (auth.getName().equals(boardInterface.getBlackPlayer().getUsername()))? true : false;
		int cellColor = clickedCell.getColor();
		
		if(isopponent) { //Joueur noir
			if(selectedPiece == null){ //Cas selection d'un pion
				System.out.println("Joueur noir détécté");
				if(b.getTurn() == 0) {
					//Pas son tour
					return "redirect:/game/play";
				}
				if(cellColor == 0) {
					//Joueur noir clique sur blanc, on redirige
					return "redirect:/game/play";
				}
				boardInterface.setSelectedPiece(x,y);
				boardInterface.getSelectedPiece().printPiece();
			}
			else{ //Un pion est deja selectionné, on deplace ou mange
				boardInterface.getBoard().eat(boardInterface.getSelectedPiece(), clickedCell);
				boardInterface.emptySelectedPiece();
				boardInterface.incrementTurns();
			}
		}else { //Joueur blanc
			System.out.println("Joueur blanc détécté");
			if(b.getTurn() == 1) {
				//Pas son tour
				return "redirect:/game/play";
			}
			if(selectedPiece == null){ //Cas selection d'un pion
				if(cellColor == 1) {
					//Joueur blanc clique sur noir
					return "redirect:/game/play";
				}
				boardInterface.setSelectedPiece(x,y);
				boardInterface.getSelectedPiece().printPiece();
			}
			else{ //Un pion est deja selectionné, on deplace ou mange
				boardInterface.getBoard().eat(boardInterface.getSelectedPiece(), clickedCell);
				boardInterface.emptySelectedPiece();
				boardInterface.incrementTurns();
			}
		}
		
		//Verification supplémentaire pour victoire
		if(b.isCheckMate()) {
			if(isopponent) {
				b.setResult(2);
			}else {
				b.setResult(1);
			}
			return "redirect:/game/save";
		}
		
		return "redirect:/game/play";
	}
	
}
