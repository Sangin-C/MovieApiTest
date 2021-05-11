package com.locky.moveapitest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovieApiRequestDto {

    private String targetDt;
    private String itemPerPage;
    private String multiMovieYn;
    private String repNationCd;
    private String wideAreaCd;

    @Builder
    public MovieApiRequestDto(String targetDt, String itemPerPage, String multiMovieYn, String repNationCd, String wideAreaCd) {
        this.targetDt = targetDt;
        this.itemPerPage = itemPerPage;
        this.multiMovieYn = multiMovieYn;
        this.repNationCd = repNationCd;
        this.wideAreaCd = wideAreaCd;
    }
}
