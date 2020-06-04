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
import org.springframework.web.bind.annotation.RequestParam;

import com.bigbeautifulchess.engine.Board;
import com.bigbeautifulchess.engine.Piece;
import com.bigbeautifulchess.tools.Coord;

@Controller

public class PlayController {
	private Board b;
	private Piece selectedPiece;
	
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
		model.addAttribute("pieceInSelection", selectedPiece);
		return "play";
	}
	
	@GetMapping("/reset")
	public String reset(Model model) {
		b = new Board();
		
		/* Uncomment to see game in console : */ 
		b.printBoardSimple();
		
		selectedPiece = null;
		
		return "redirect:/play";
	}
	
	@GetMapping("/play/select/{x},{y}")
	public String selectPiece( @PathVariable int x,@PathVariable int y,Model model) {
		if(b == null) {
			return "redirect:/reset";
		}
		selectedPiece = b.getPieceOnCell(x,y);
		selectedPiece.printPiece();
		
		return "redirect:/play";
	}
	
	@GetMapping("/play/move/{x},{y}")
	public String movePiece( @PathVariable int x,@PathVariable int y,Model model) {
		if(b == null) {
			return "redirect:/reset";
		}
		if(selectedPiece == null) {
			return "redirect:/play";
		}
		
		selectedPiece.printPiece();
		selectedPiece.setCoord(new Coord(x,y));
		
		return "redirect:/play";
	}
	
	
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
