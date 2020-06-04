package com.bigbeautifulchess.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.bigbeautifulchess.engine.Board;
import com.bigbeautifulchess.tools.Coord;

@Controller

public class PlayController {
	private Board b;
	Long cur_mode;
	
	@GetMapping("/lost")
	public String lose(Model model) {
		return "lost";
	}
	
	@GetMapping("/play")
	public String play(Model model) {
		if(b == null) {
			return "redirect:/reset";
		}
		
		model.addAttribute("board", b);
		return "play";
	}
	
	@GetMapping("/reset")
	public String reset(Model model) {
		b = new Board();
		
		/* Uncomment to see game in console : */ 
		b.printBoardSimple();
		
		return "redirect:/play";
	}
	
	@GetMapping({"/play/move-{x},{y}"})
	public String move( Model model ,@PathVariable(required=true) int x, @PathVariable(required=true) int y) {
		if(b == null) {
			return "redirect:/reset";
		}
		Coord c = new Coord(x,y);
		b.getCells()[0][0].setC(c);
		
		model.addAttribute("board", b);
		return "play";
	}
	
	/*
	@GetMapping({"/{id}/play"})
	public String add(@PathVariable(required=false) Long id, Model model) {
		GameMode mode = modes.findById(id).get();
		if(mode != null) {
			cur_mode = id;
			width = mode.getWidth();
			height = mode.getHeight();
			nbmines = mode.getNbMine();
		}
		return "redirect:/reset";
	}
	*/
	
	//Chargement de partie
	@GetMapping({"/load/{id}"})
	public String load(Model model, @PathVariable(required=true) Long id) {
		/*
		GameLoaderService gs = new GameLoaderService();
		
		//Recupération des données
		InProgressGame gamecur = progressRep.findById(id).get();
		String data = gamecur.getGridCode();
		this.cur_mode = gamecur.getGameModeID();
		
		//Suppression de la sauvegarde
		progressRep.deleteById(id);
		
		//Initialisation du jeu
		g = gs.decodeGameParameters(data);
		if(g != null) {
			this.width = g.getWidth();
			this.height = g.getHeight();
			this.nbmines = g.getNbMine();
			return "redirect:/play";
		}
		*/
		return "redirect:/";
	}
	
	@GetMapping("/play/{id}")
	public String selectCase(@PathVariable int id, Model model) {
		//No game started case : start a new one
		/*
		if(g == null) {
			return "redirect:/reset";
		}
		if(g.lost == false) {
			if(g.getCase(id).isVisible() == false) {
				g.clickOnCase(id);
			}
			if(g.getCase(id).getType() == Type.BOMB) {
				g.lose();
			}
		}
		model.addAttribute("gridGame", g);
		model.addAttribute("score", new ScoreForm());
		*/
		return "play";
	}
	
	@GetMapping("/saveGame")
	public String saveGame(Model model) {
		//Sauve le jeu dans la base de données
		/*
		if(g != null) {
			InProgressGame progress = new InProgressGame();
			progress.setElapsedTime((int)g.getElapsedTime());
			progress.setGridCode(g.exportToString());
			if(cur_mode != 0 && cur_mode != null) {
				progress.setGameModeID(cur_mode);
			
				progressRep.save(progress);
			}
		}else {
			System.out.println("ERROR: NO GAME TO SAVE");
		}
		*/
		return "redirect:/progress/list";
	}
	
	/*
	@PostMapping("/saveScore")
	public String addScore(@Valid @ModelAttribute("score") ScoreForm form, BindingResult result, Model model) {
		//VICTOIRE ! - on enregistre le score
		if(result.hasErrors()) {
			model.addAttribute("score",form);
			return "play";
		}
		
		if(g.retourneChance() == 0) {
			int time = (int)g.getElapsedTime();
			String name = form.getOwner();
			
			Score s = new Score();
			s.setOwner(name);
			s.setValue(time);
			if(cur_mode != 0 && cur_mode != null)
				s.setGameMode(modes.findById(cur_mode).get());
			scores.save(s);
		}else {
			return "play";
		}
		
		return "redirect:/score/list";
	}
	*/
	
	
	
}
