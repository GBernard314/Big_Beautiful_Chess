package com.bigbeautifulchess.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.bigbeautifulchess.domain.Game;
import com.bigbeautifulchess.domain.User;
import com.bigbeautifulchess.form.RegisterForm;
import com.bigbeautifulchess.repository.GameRepository;
import com.bigbeautifulchess.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	GameRepository gameRepository;

	@PersistenceContext
	EntityManager em;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public String welcome(Model model, Authentication authentication) {
		User user = userRepository.findByUsername(authentication.getName());
		List <Game> onGoingGame = em.createQuery("select g from Game g JOIN g.users us where g.flag_winner =:value and us.username = :valuename order by nb_turn DESC",Game.class)
			.setParameter("value", -1)
			.setParameter("valuename", authentication.getName())
			.setMaxResults(5)
			.getResultList();
		
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
		int gameCount = (int) gameRepository.count();
		
		model.addAttribute("onGoingGamesCount", gameCount);
		model.addAttribute("userInfo", user);
		model.addAttribute("friends", userlist);
		model.addAttribute("onGoingGames", onGoingGame);
		
		return "index";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/register")
	public String showRegisterForm(WebRequest request, Model model) {
		model.addAttribute("register", new User());
		return "register";
	}
	
	@Transactional
	@GetMapping("/addFriend/{name}")
	public String addFriend(@PathVariable String name, Authentication auth) {
		User myself = userRepository.findByUsername(auth.getName());
		User friend = userRepository.findByUsername(name);
		if(friend != null) {
			System.out.println("Ajout de "+friend.getUsername());
			//attribuer à user en base (friend.id)
			String friendlist=  myself.getFriends_list_id();
			friendlist += ";"+friend.getId();
			myself.setFriends_list_id(friendlist);
			em.merge(myself);
		}
		return "redirect:/";
	}
	
	@PostMapping("/register")
	public String addForm(@ModelAttribute RegisterForm form) {
		User user = new User();
		user.setUsername(form.getUsername());
		user.setEmail(form.getEmail());
		user.setPassword(passwordEncoder.encode(form.getPassword()));
		user.setFriends_list_id("2;3;4");
		userRepository.save(user);
		
		return "redirect:/login";
	}
	

	  @RequestMapping(value = "/username", method = RequestMethod.GET)
	  @ResponseBody
	  public String currentUserName(Authentication authentication, Model model) {
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
		 model.addAttribute("friends", userlist);
		 for(int i=0; i<userlist.size(); i++) {
			 System.out.println(userlist.get(i).getUsername());
			 System.out.println(userlist.get(i).getEmail());
		 }
		 if(userlist.size()==0) {
			 System.out.println("Aucun amis !");
		 }
		 
	     return authentication.getName();

	  }
	
}