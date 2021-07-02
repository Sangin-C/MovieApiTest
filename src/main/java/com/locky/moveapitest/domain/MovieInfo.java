package com.locky.moveapitest.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
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

    @Column(columnDefinition = "json")
    private String nations;

    @Column(columnDefinition = "json")
    private String genres;

    @Column(columnDefinition = "json")
    private String directors;

    @Column(columnDefinition = "json")
    private String actors;

    @Column(columnDefinition = "json")
    private String companys;

    @Column(columnDefinition = "json")
    private String audits;

}
