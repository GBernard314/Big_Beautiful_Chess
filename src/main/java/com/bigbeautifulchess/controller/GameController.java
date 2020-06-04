package com.bigbeautifulchess.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

@Controller
@RequestMapping("/game")
public class GameController {
	
	private Board b;
	private Piece selectedPiece;
	private int myColor = 0;
	
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
		List <User> users = new ArrayList<>();
		myself = userRepository.findByUsername("test");
		opponent = userRepository.findByUsername(adversaire);
        System.out.println(myself.getUsername());
        System.out.println(opponent.getUsername());

		
		return "redirect:/game/reset";
	}
	
	@GetMapping("/save")
	public String saveGameRepo(){
		List <User> users = new ArrayList<>();

		users.add(myself);
		users.add(opponent);
		
		Game game = new Game();
		game.setFlag_winner(b.getResult());
		game.setBoard_info(b.cellsToString());
		game.setForfeit(false); //TODO : forfait
		game.setNb_turn(5); //TODO : nbTurn
		game.setTime_user1(b.getTime_white());
		game.setTime_user2(b.getTime_black());
		game.setUsers(users);
		game.setStorage(b.storageToString());
		game.setMouv("feziufhezf"); //TODO : c'est quoi ?
		
		gameRepository.save(game);
		
		myself = null;
		opponent = null;
		
		return "redirect:/";
	}

	private List<Game> getGames(String username, Boolean ongoing) {
		int finishedParam;
		if(ongoing == true) finishedParam = -1;
		else finishedParam = 1;
		User user = userRepository.findByUsername(username);
		List<Game> games = em.createQuery("select g from Game g JOIN g.users us where g.flag_winner !=:value and us.username = :valuename ",Game.class)
				.setParameter("value", finishedParam)
				.setParameter("valuename", username)
				.getResultList();
		return games;
	}
	
	@GetMapping("/history")
	public String history(Model model, Authentication authentication) {
		model.addAttribute("label", "Historique des parties");
		model.addAttribute("gameList", getGames(authentication.getName(), true));
		User user = userRepository.findByUsername(authentication.getName());
		List<User> userlist = new ArrayList<>();
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

		model.addAttribute("userInfo", user);
		model.addAttribute("friends", userlist);
		
		return "game-history";
	}
	
	@GetMapping("/ongoing")
	public String ongoing(Model model, Authentication authentication) {
		model.addAttribute("label", "Parties en cours");
		model.addAttribute("gameList", getGames(authentication.getName(), false));
		User user = userRepository.findByUsername(authentication.getName());
		List<User> userlist = new ArrayList<>();
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

		model.addAttribute("userInfo", user);
		model.addAttribute("friends", userlist);
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
		System.out.println(b.getTurn());
		model.addAttribute("board", b);
		model.addAttribute("pieceInSelection", selectedPiece);
		if(selectedPiece != null) {
			model.addAttribute("moves", selectedPiece.getMoves());
		}
		
		User user = userRepository.findByUsername(authentication.getName());
		List<User> userlist = new ArrayList<>();
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

		model.addAttribute("userInfo", user);
		model.addAttribute("friends", userlist);
		if(myself != null )
			model.addAttribute("myself",myself);
		if(opponent != null)
			model.addAttribute("opponent",opponent);
		
		return "play";
	}
	
	@GetMapping("/reset")
	public String reset(Model model) {
		b = new Board();
		
		/* Uncomment to see game in console : */ 
		b.printBoardSimple();
		
		selectedPiece = null;
		
		return "redirect:/game/play";
	}
	
	@GetMapping("/play/select/{x},{y}")
	public String selectPiece( @PathVariable int x,@PathVariable int y,Model model) {
		if(b == null) {
			return "redirect:/game/reset";
		}
		Piece clickedCell = b.getPieceOnCell(x,y);
		if(selectedPiece == null){ //Cas selection d'un pion
			if(true || clickedCell.getColor() == myColor) { //Debug : les deux couleurs sont jouables
				selectedPiece = b.getPieceOnCell(x,y);
				selectedPiece.printPiece();
			}
		}
		else{ //Un pion est deja selectionné, on deplace ou mange
			b.eat(selectedPiece, clickedCell);
			selectedPiece = null;
		}
		
		return "redirect:/game/play";
	}
	
}