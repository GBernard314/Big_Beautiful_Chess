package com.bigbeautifulchess.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Game {


	@Id @Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long Id;
	
	@OneToMany(cascade=CascadeType.ALL)
	protected List<User> users = new ArrayList<>();
	
	@Column
	protected String board_info;
	
	@Column
	protected int flag_winner;
	
	@Column
	protected boolean forfeit;
	
	@Column
	protected int nb_turn;
	
	@Column
	protected int time_user1;
	
	@Column
	protected int time_user2;
	
	@Column
	private String storage;
	
	@Column
	private String mouv;
	
	
	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getMouv() {
		return mouv;
	}

	public void setMouv(String mouv) {
		this.mouv = mouv;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getBoard_info() {
		return board_info;
	}

	public void setBoard_info(String board_info) {
		this.board_info = board_info;
	}

	public int getFlag_winner() {
		return flag_winner;
	}

	public void setFlag_winner(int flag_winner) {
		this.flag_winner = flag_winner;
	}

	public boolean isForfeit() {
		return forfeit;
	}

	public void setForfeit(boolean forfeit) {
		this.forfeit = forfeit;
	}

	public int getNb_turn() {
		return nb_turn;
	}

	public void setNb_turn(int nb_turn) {
		this.nb_turn = nb_turn;
	}

	public int getTime_user1() {
		return time_user1;
	}

	public void setTime_user1(int time_user1) {
		this.time_user1 = time_user1;
	}

	public int getTime_user2() {
		return time_user2;
	}

	public void setTime_user2(int time_user2) {
		this.time_user2 = time_user2;
	}
}
