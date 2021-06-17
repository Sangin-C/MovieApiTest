package com.locky.moveapitest.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieInfoRepository extends JpaRepository<MovieInfo, Long> {
}
