package com.bigbeautifulchess.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigbeautifulchess.domain.Game;

public interface GameRepository extends JpaRepository<Game,Long>{

}
