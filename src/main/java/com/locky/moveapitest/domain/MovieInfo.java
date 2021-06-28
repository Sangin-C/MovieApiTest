package com.locky.moveapitest.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String movieCd;
    private String movieNm;
    private String movieNmEn;
    private String movieNmOg;
    private String prdtYear;
    private String openDt;
    private String prdtStatNm;
    private String typeNm;

}
