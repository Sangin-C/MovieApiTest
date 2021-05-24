package com.locky.moveapitest.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyMovieRepository extends JpaRepository<WeeklyMovie, Long> {
}
