package com.locky.moveapitest.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyMovieRepository extends JpaRepository<DailyMovie, Long> {
}
