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
import com.bigbeautifulchess.engine.Piece;
import com.bigbeautifulchess.repository.GameRepository;
import com.bigbeautifulchess.repository.UserRepository;
import com.bigbeautifulchess.service.AjaxResponse;

@Controller
@RequestMapping("/game")
public class GameController {
	
	private Board b;
	private Piece selectedPiece;
	private int nbTours = 0;
	
	private User myself;
	private User opponent;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	GameRepository gameRepository;

	@PersistenceContext
	EntityManager em;
	
	
	@GetMapping("/new/{adversaire}")
	public String initGame(Model model, Authentication authentication, @PathVariable String adversaire){
		myself = userRepository.findByUsername(authentication.getName());
		opponent = userRepository.findByUsername(adversaire);
        System.out.println(myself.getUsername());
        System.out.println(opponent.getUsername());
		
		return "redirect:/game/reset";
	}
	
	@GetMapping("/load/{gameid}")
	public String loadGame(Model model, Authentication authentication, @PathVariable Long gameid){
		Optional<Game> probableGame = gameRepository.findById(gameid);
		if(probableGame.isPresent()) {
			//Partie identifiée
			Game g = probableGame.get();
			//Board(String bdd, int turn, int result, int time_black, int time_white, String storage, String historic)
			b = new Board(g.getBoard_info(), g.getNb_turn()%2, g.getFlag_winner(), g.getTime_user2(), g.getTime_user1(), g.getStorage(), g.getMouv());
			List<User> usrs = g.getUsers();
			if(usrs.size() == 2) {
				myself = usrs.get(0);
				opponent = usrs.get(1);
				return "redirect:/game/play";
			}
			
		}
		else {
			//Erreur : partie non identifiée
			return "redirect:/";
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

		users.add(myself);
		users.add(opponent);
		
		Game game = new Game();
		game.setId(null);
		game.setUsers(users);
		game.setFlag_winner(b.getResult());
		game.setBoard_info(b.cellsToString());
		game.setForfeit(false); //TODO : forfait
		game.setNb_turn(nbTours);
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
		return "game-history";
	}
	
	@GetMapping("/lost")
	public String lose(Model model) {
		return "lost";
	}
	
	@GetMapping("/play")
	public String play(Model model, Authentication authentication) {
		if(b == null) {
			return "redirect:/game/reset";
		}
		if(myself == null) {
			return "redirect:/";
		}
		System.out.println(b.getTurn());
		model.addAttribute("board", b);
		model.addAttribute("pieceInSelection", selectedPiece);
		if(selectedPiece != null) {
			model.addAttribute("moves", selectedPiece.getMoves());
		}
		
		User user = userRepository.findByUsername(authentication.getName());

		model.addAttribute("userInfo", user);
		model.addAttribute("friends", getFriends(user));
		if(myself != null )
			model.addAttribute("myself",myself);
		if(opponent != null)
			model.addAttribute("opponent",opponent);
		
		System.out.println("is small castling ok ? " + b.isSmallCastlingOk());
		System.out.println("is big castling ok ? " + b.isBigCastlingOk());
		System.out.println("Egalite ? " + b.draw());
		
		return "play";
	}
	
	@GetMapping("/reset")
	public String reset(Model model) {
		b = new Board();
		
		/* Uncomment to see game in console : */ 
		b.printBoardSimple();
		nbTours = 0;
		
		selectedPiece = null;
		
		return "redirect:/game/play";
	}
	
	@GetMapping("/play/select/{x},{y}")
	public String selectPiece( @PathVariable int x,@PathVariable int y,Model model, Authentication auth) {
		if(b == null) {
			return "redirect:/game/reset";
		}
		Piece clickedCell = b.getPieceOnCell(x,y);
		boolean isopponent = (auth.getName().equals(opponent.getUsername()))? true : false;
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
				selectedPiece = b.getPieceOnCell(x,y);
				selectedPiece.printPiece();
			}
			else{ //Un pion est deja selectionné, on deplace ou mange
				b.eat(selectedPiece, clickedCell);
				selectedPiece = null;
				nbTours++;
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
				selectedPiece = b.getPieceOnCell(x,y);
				selectedPiece.printPiece();
			}
			else{ //Un pion est deja selectionné, on deplace ou mange
				b.eat(selectedPiece, clickedCell);
				selectedPiece = null;
				nbTours++;
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
	
	
	@GetMapping(value = "/game/play/ajax")
	  public AjaxResponse getResource() {
		AjaxResponse response = new AjaxResponse("Done", b);
	    return response;
	  }
}
