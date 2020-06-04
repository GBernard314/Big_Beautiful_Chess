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
import org.springframework.web.bind.annotation.RequestMapping;

import com.bigbeautifulchess.domain.Game;
import com.bigbeautifulchess.domain.User;
import com.bigbeautifulchess.repository.GameRepository;
import com.bigbeautifulchess.repository.UserRepository;

@Controller
@RequestMapping("/game")
public class GameController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	GameRepository gameRepository;

	@PersistenceContext
	EntityManager em;

	
	@GetMapping("/new")
	public String initGameRepo(){
		List <User> users = new ArrayList<>();
		User user1 = userRepository.findByUsername("test");
		User user2 = userRepository.findByUsername("coco");
        System.out.println(user1.getUsername());
        System.out.println(user2.getUsername());

		users.add(user1);
		users.add(user2);
		
		Game game = new Game();
		game.setFlag_winner(-1);
		game.setBoard_info("1,4;5,6/3,2;6,2");
		game.setForfeit(false);
		game.setNb_turn(5);
		game.setTime_user1(200);
		game.setTime_user2(600);
		game.setUsers(users);
		
		gameRepository.save(game);
		
		return "index.html";
	}
	
	@GetMapping("/viewPlayers")
	public String returnPlayersOfGame(Authentication authentication, Model model) {
		Game g = new Game();
		
		List<Game> finishedGame = em.createQuery("select g from Game g JOIN g.users us where g.flag_winner !=:value and us.username = :valuename ",Game.class)
							.setParameter("value", -1)
							.setParameter("valuename", "test")
							.getResultList();
		
		List <Game> onGoingGame = em.createQuery("select g from Game g JOIN g.users us where g.flag_winner =:value and us.username = :valuename ",Game.class)
				.setParameter("value", -1)
				.setParameter("valuename", "test")
				.getResultList();
									
		model.addAttribute("finishedGames", finishedGame);
		model.addAttribute("onGoingGame", onGoingGame);
		for(int i=0; i<finishedGame.size(); i++) {
			System.out.print("Finished");
			System.out.println(finishedGame.get(i).getId());
		}
		
		for(int i=0; i<onGoingGame.size(); i++) {
			System.out.print("On going");
			System.out.println(onGoingGame.get(i).getId());
		}
		 
		return "index.html";
	}
}
