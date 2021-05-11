package com.locky.moveapitest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locky.moveapitest.domain.Movie;
import com.locky.moveapitest.domain.MovieRepository;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;


@Slf4j
@RequiredArgsConstructor
@RestController
public class MovieApiController {

    private final MovieRepository movieRepository;

    //발급키키
    String key = "f778bea14d8ca8349bc583598d1288e9";
    String dailyResponse = "";

    @GetMapping("/")
    public String show(){
        
        //일자 포맷 맞추기
        SimpleDateFormat todayFormat = new SimpleDateFormat("yyyyMMdd");

        //전날 박스오피스 조회 ( 오늘 날짜꺼는 안나옴.. )
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DATE , -1);
        //조회 날짜
        String targetDt = todayFormat.format(day.getTime());
        //ROW 개수
        String itemPerPage = "10";
        //다양성영화(Y)/상업영화(N)/전체(default)
        String multiMovieYn = "Y";
        //한국영화(K)/외국영화(F)/전체(default)
        String repNationCd = "F";
        //상영지역별 코드/전체(default)
        String wideAreaCd = "";

        try {
            // KOBIS 오픈 API Rest Client를 통해 호출
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);

            // 일일 박스오피스 서비스 호출 (boolean isJson, String targetDt, String itemPerPage,String multiMovieYn, String repNationCd, String wideAreaCd)
            dailyResponse = service.getDailyBoxOffice(true, targetDt, itemPerPage, multiMovieYn, repNationCd, wideAreaCd);
            log.info(dailyResponse);

            //JSON Parser 객체 생성
            JSONParser jsonParser = new JSONParser();

            //Parser로 문자열 데이터를 객체로 변환
            Object obj = jsonParser.parse(dailyResponse);
            log.info("obj.toString() : "+obj.toString());
            //파싱한 obj를 JSONObject 객체로 변환
            JSONObject jsonObject = (JSONObject) obj;

            //차근차근 parsing하기
            JSONObject parse_boxOfficeResult = (JSONObject) jsonObject.get("boxOfficeResult");

            //박스오피스 종류
            String boxofficeType = (String) parse_boxOfficeResult.get("boxofficeType");
            log.info("boxofficeType : "+boxofficeType);

            //박스오피스 조회 일자
            String showRange = (String) parse_boxOfficeResult.get("showRange");
            log.info("showRange : "+showRange);

            ObjectMapper objectMapper = new ObjectMapper();
            JSONArray parse_dailyBoxOfficeList = (JSONArray) parse_boxOfficeResult.get("dailyBoxOfficeList");
            for(int i=0; i<parse_dailyBoxOfficeList.size(); i++){
                JSONObject dailyBoxOffice = (JSONObject) parse_dailyBoxOfficeList.get(i);
                //JSON object -> Java Object(Entity) 변환
                Movie movie = objectMapper.readValue(dailyBoxOffice.toString(), Movie.class);
                //DB저장
                movieRepository.save(movie);
            }
        }catch(Exception e){
            e.getMessage();
        }

        return dailyResponse;
    }
}
