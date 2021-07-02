package com.locky.moveapitest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locky.moveapitest.domain.*;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
public class MovieApiController {

    private final DailyMovieRepository dailyMovieRepository;
    private final WeeklyMovieRepository weeklyMovieRepository;
    private final MovieListRepository movieListRepository;
    private final MovieInfoRepository movieInfoRepository;

    //발급키키
    String key = "f778bea14d8ca8349bc583598d1288e9";


    @GetMapping("/daily")
    public String dailyBoxOffice() {
        String dailyResponse = "";

        //일자 포맷 맞추기
        SimpleDateFormat todayFormat = new SimpleDateFormat("yyyyMMdd");

        //전날 박스오피스 조회 ( 오늘 날짜꺼는 안나옴.. )
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        String targetDt = time.format(DateTimeFormatter.ofPattern("yyyMMdd"));

        //ROW 개수
        String itemPerPage = "10";

        //다양성영화(Y)/상업영화(N)/전체(default)
        String multiMovieYn = "";

        //한국영화(K)/외국영화(F)/전체(default)
        String repNationCd = "";

        //상영지역별 코드/전체(default)
        String wideAreaCd = "";

        try {
            // KOBIS 오픈 API Rest Client를 통해 호출
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);

            // 일일 박스오피스 서비스 호출 (boolean isJson, String targetDt, String itemPerPage,String multiMovieYn, String repNationCd, String wideAreaCd)
            dailyResponse = service.getDailyBoxOffice(true, targetDt, itemPerPage, multiMovieYn, repNationCd, wideAreaCd);

            //JSON Parser 객체 생성
            JSONParser jsonParser = new JSONParser();

            //Parser로 문자열 데이터를 객체로 변환
            Object obj = jsonParser.parse(dailyResponse);

            //파싱한 obj를 JSONObject 객체로 변환
            JSONObject jsonObject = (JSONObject) obj;

            //차근차근 parsing하기
            JSONObject parse_boxOfficeResult = (JSONObject) jsonObject.get("boxOfficeResult");

            //박스오피스 종류
            String boxofficeType = (String) parse_boxOfficeResult.get("boxofficeType");

            //박스오피스 조회 일자
            String showRange = (String) parse_boxOfficeResult.get("showRange");

            ObjectMapper objectMapper = new ObjectMapper();
            JSONArray parse_dailyBoxOfficeList = (JSONArray) parse_boxOfficeResult.get("dailyBoxOfficeList");
            for (int i = 0; i < parse_dailyBoxOfficeList.size(); i++) {
                JSONObject dailyBoxOfficeObject = (JSONObject) parse_dailyBoxOfficeList.get(i);
                //JSON object -> Java Object(Entity) 변환
                DailyMovie dailyMovie = objectMapper.readValue(dailyBoxOfficeObject.toString(), DailyMovie.class);
                //DB저장
                dailyMovie.setTargetDt(targetDt);
                dailyMovie.setBoxofficeType(boxofficeType);
                dailyMovie.setShowRange(showRange);
                //dailyMovieRepository.save(dailyMovie);
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return dailyResponse;
    }

    @GetMapping("/weekly")
    public String weeklyBoxOffice() {
        String weeklyResponse = "";

        //일자 포맷 맞추기
        SimpleDateFormat todayFormat = new SimpleDateFormat("yyyyMMdd");

        //전주 박스오피스 조회 ( 해당주는 안나옴.. )
        //LocalDateTime time = LocalDateTime.now().minusDays(7);
        //String targetDt =  time.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String targetDt = "20210530";

        //주간/주말/주중 선택
        String weekGb = "0";

        //ROW 개수
        String itemPerPage = "10";

        //다양성영화(Y)/상업영화(N)/전체(default)
        String multiMovieYn = "";

        //한국영화(K)/외국영화(F)/전체(default)
        String repNationCd = "";

        //상영지역별 코드/전체(default)
        String wideAreaCd = "";

        try {
            // KOBIS 오픈 API Rest Client를 통해 호출
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);

            // 일일 박스오피스 서비스 호출 (boolean isJson, String targetDt, String itemPerPage, String weekGb, String multiMovieYn, String repNationCd, String wideAreaCd)
            weeklyResponse = service.getWeeklyBoxOffice(true, targetDt, itemPerPage, weekGb, multiMovieYn, repNationCd, wideAreaCd);

            //JSON Parser 객체 생성
            JSONParser jsonParser = new JSONParser();

            //Parser로 문자열 데이터를 객체로 변환
            Object obj = jsonParser.parse(weeklyResponse);

            //파싱한 obj를 JSONObject 객체로 변환
            JSONObject jsonObject = (JSONObject) obj;

            //차근차근 parsing하기
            JSONObject parse_boxOfficeResult = (JSONObject) jsonObject.get("boxOfficeResult");

            //박스오피스 종류
            String boxofficeType = (String) parse_boxOfficeResult.get("boxofficeType");

            //박스오피스 조회 일자
            String showRange = (String) parse_boxOfficeResult.get("showRange");

            //박스오피스 조회 일자
            String yearWeekTime = (String) parse_boxOfficeResult.get("yearWeekTime");

            ObjectMapper objectMapper = new ObjectMapper();
            JSONArray parse_weeklyBoxOfficeList = (JSONArray) parse_boxOfficeResult.get("weeklyBoxOfficeList");
            for (int i = 0; i < parse_weeklyBoxOfficeList.size(); i++) {
                JSONObject weeklyBoxOfficeObject = (JSONObject) parse_weeklyBoxOfficeList.get(i);
                //JSON object -> Java Object(Entity) 변환
                WeeklyMovie weeklyMovie = objectMapper.readValue(weeklyBoxOfficeObject.toString(), WeeklyMovie.class);
                //DB저장
                weeklyMovie.setTargetDt(targetDt);
                weeklyMovie.setBoxofficeType(boxofficeType);
                weeklyMovie.setShowRange(showRange);
                weeklyMovie.setYearWeekTime(yearWeekTime);
                //weeklyMovieRepository.save(weeklyMovie);
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return weeklyResponse;
    }

    @GetMapping("/movieList")
    public String movieList() {
        String movieListResponse = "";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("curPage", "1");
        paramMap.put("itemPerPage", "10");
        paramMap.put("movieNm", "");
        paramMap.put("directorNm", "");
        paramMap.put("openStartDt", "");
        paramMap.put("openEndDt", "");
        paramMap.put("prdtStartYear", "");
        paramMap.put("prdtEndYear", "");
        paramMap.put("repNationCd", "");
        String[] movieTypeCdArr = {};
        paramMap.put("movieTypeCdArr", movieTypeCdArr);

        try {
            // KOBIS 오픈 API Rest Client를 통해 호출
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);
            movieListResponse = service.getMovieList(true, paramMap);

            //JSON Parser 객체 생성
            JSONParser jsonParser = new JSONParser();

            //Parser로 문자열 데이터를 객체로 변환
            Object obj = jsonParser.parse(movieListResponse);

            //파싱한 obj를 JSONObject 객체로 변환
            JSONObject jsonObject = (JSONObject) obj;

            //차근차근 parsing하기
            JSONObject parse_movieListResult = (JSONObject) jsonObject.get("movieListResult");

            //JSON object -> Java Object(Entity) 변환하기위한 Mapper 선언
            ObjectMapper objectMapper = new ObjectMapper();

            //새로운 JSONObject 선언
            JSONObject movieListResultObject = new JSONObject();

            JSONArray parse_movieList = (JSONArray) parse_movieListResult.get("movieList");
            for (int i = 0; i < parse_movieList.size(); i++) {
                JSONObject movieListObject = (JSONObject) parse_movieList.get(i);

                String repNationNm = (String) movieListObject.get("repNationNm");
                movieListResultObject.put("repNationNm", repNationNm);

                String nationAlt = (String) movieListObject.get("nationAlt");
                movieListResultObject.put("nationAlt", nationAlt);

                String repGenreNm = (String) movieListObject.get("repGenreNm");
                movieListResultObject.put("repGenreNm", repGenreNm);

                String movieNm = (String) movieListObject.get("movieNm");
                movieListResultObject.put("movieNm", movieNm);

                String movieCd = (String) movieListObject.get("movieCd");
                movieListResultObject.put("movieCd", movieCd);

                String prdtStatNm = (String) movieListObject.get("prdtStatNm");
                movieListResultObject.put("prdtStatNm", prdtStatNm);

                String prdtYear = (String) movieListObject.get("prdtYear");
                movieListResultObject.put("prdtYear", prdtYear);

                String typeNm = (String) movieListObject.get("typeNm");
                movieListResultObject.put("typeNm", typeNm);

                String openDt = (String) movieListObject.get("openDt");
                movieListResultObject.put("openDt", openDt);

                String movieNmEn = (String) movieListObject.get("movieNmEn");
                movieListResultObject.put("movieNmEn", movieNmEn);

                String genreAlt = (String) movieListObject.get("genreAlt");
                movieListResultObject.put("genreAlt", genreAlt);

                //영화감독(directors) Array 추출
                JSONArray parse_directorsList = (JSONArray) movieListObject.get("directors");
                movieListResultObject.put("directors", parse_directorsList.toString());
                //제작사(companys) Array 추출
                JSONArray parse_companysList = (JSONArray) movieListObject.get("companys");
                movieListResultObject.put("companys", parse_companysList.toString());

/*
                //영화감독(directors) Array 추출
                StringBuilder directorsList = new StringBuilder();
                JSONArray parse_directorsList = (JSONArray) movieListObject.get("directors");

                for (int j = 0; j < parse_directorsList.size(); j++) {
                    JSONObject directorsListObject = (JSONObject) parse_directorsList.get(j);
                    String directors = (String) directorsListObject.get("peopleNm");
                    if(j>0)directorsList.append(", ");
                    directorsList.append(directors);
                    movieListResultObject.put("directors", directorsList.toString());
                }
/*

/*
                //제작사(companys) Array 추출
                //제작사 코드 빼고 제작사명만 넣는다.
                StringBuilder companysList = new StringBuilder();
                JSONArray parse_companysList = (JSONArray) movieListObject.get("companys");

                for (int k = 0; k < parse_companysList.size(); k++) {
                    JSONObject companysListObject = (JSONObject) parse_companysList.get(k);
                    String companyNm = (String) companysListObject.get("companyNm");
                    if(k>0) companysList.append(",");
                    companysList.append(companyNm);
                    movieListResultObject.put("companys", companysList.toString());
                }
*/

                //JSON object -> Java Object(Entity) 변환
                MovieList movieList = objectMapper.readValue(movieListResultObject.toString(), MovieList.class);
                movieListRepository.save(movieList);
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return movieListResponse;
    }


    @RequestMapping("/movieInfo")
    public String movieInfo(String MovieCd) {

        String movieInfoResponse = "";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("movieCd", "20204261");

        try {
            // KOBIS 오픈 API Rest Client를 통해 호출
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);

            movieInfoResponse = service.getMovieInfo(true, paramMap);

            //JSON Parser 객체 생성
            JSONParser jsonParser = new JSONParser();

            //Parser로 문자열 데이터를 객체로 변환
            Object obj = jsonParser.parse(movieInfoResponse);

            //파싱한 obj를 JSONObject 객체로 변환
            JSONObject jsonObject = (JSONObject) obj;

            //JSON object -> Java Object(Entity) 변환하기위한 Mapper 선언
            ObjectMapper objectMapper = new ObjectMapper();

            //새로운 JSONObject 선언
            JSONObject movieInfoResultObject = new JSONObject();

            //차근차근 parsing하기
            JSONObject parse_movieInfoResult = (JSONObject) jsonObject.get("movieInfoResult");

            //영화 정보
            JSONObject parse_movieInfo = (JSONObject) parse_movieInfoResult.get("movieInfo");

            //영화 코드
            String movieCd = (String) parse_movieInfo.get("movieCd");
            movieInfoResultObject.put("movieCd", movieCd);

            //영화 한글명
            String movieNm = (String) parse_movieInfo.get("movieNm");
            movieInfoResultObject.put("movieNm", movieNm);

            //영화 영문명
            String movieNmEn = (String) parse_movieInfo.get("movieNmEn");
            movieInfoResultObject.put("movieNmEn", movieNmEn);

            //영화 원문명
            String movieNmOg = (String) parse_movieInfo.get("movieNmOg");
            movieInfoResultObject.put("movieNmOg", movieNmOg);

            //제작연도
            String prdtYear = (String) parse_movieInfo.get("prdtYear");
            movieInfoResultObject.put("prdtYear", prdtYear);

            //오픈일자
            String openDt = (String) parse_movieInfo.get("openDt");
            movieInfoResultObject.put("openDt", openDt);

            //제작상태
            String prdtStatNm = (String) parse_movieInfo.get("prdtStatNm");
            movieInfoResultObject.put("prdtStatNm", prdtStatNm);

            //영화유형
            String typeNm = (String) parse_movieInfo.get("typeNm");
            movieInfoResultObject.put("typeNm", typeNm);

            //국가(nations) Array 추출
            JSONArray parse_nationsList = (JSONArray) parse_movieInfo.get("nations");
            movieInfoResultObject.put("nations", parse_nationsList.toString());

            //장르(genres) Array 추출
            JSONArray parse_genresList = (JSONArray) parse_movieInfo.get("genres");
            movieInfoResultObject.put("genres", parse_genresList.toString());

            //영화감독(directors) Array 추출
            JSONArray parse_directorsList = (JSONArray) parse_movieInfo.get("directors");
            movieInfoResultObject.put("directors", parse_directorsList.toString());

            //배우(actors) Array 추출
            JSONArray parse_actorsList = (JSONArray) parse_movieInfo.get("actors");
            movieInfoResultObject.put("actors", parse_actorsList.toString());

            //영화사(companys) Array 추출
            JSONArray parse_companysList = (JSONArray) parse_movieInfo.get("companys");
            movieInfoResultObject.put("companys", parse_companysList.toString());

            //심의정보(audits) Array 추출
            JSONArray parse_auditsList = (JSONArray) parse_movieInfo.get("audits");
            movieInfoResultObject.put("audits", parse_auditsList.toString());

            //JSON object -> Java Object(Entity) 변환
            MovieInfo movieInfo = objectMapper.readValue(movieInfoResultObject.toString(), MovieInfo.class);
            movieInfoRepository.save(movieInfo);

        } catch (Exception e) {
            e.getMessage();
        }
        return movieInfoResponse;
    }

    @GetMapping(value = "/aa")
    public List<MovieList> aa() {
        return movieListRepository.findAll();
    }

    @GetMapping(value = "/bb")
    public void bb() {
        try {
            String bb = "[{\"peopleNm\": \"이삭 아이시탄\"}, {\"peopleNm\": \"캐롤 폴리퀸\"}]";

            //JSON Parser 객체 생성
            JSONParser jsonParser = new JSONParser();

            Object obj = jsonParser.parse(bb);

            //파싱한 obj를 JSONObject 객체로 변환
            JSONArray jsonObject = (JSONArray) obj;

            System.out.println(jsonObject.get(0));

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
