package com.bigbeautifulchess.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigbeautifulchess.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
