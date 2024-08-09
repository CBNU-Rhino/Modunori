package app.app.openApi.Controller;

import app.app.openApi.AreaCode;
import app.app.openApi.Repository.AreaCodeRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@RestController
@RequiredArgsConstructor
public class AreaCodeController {

    private final AreaCodeRepository areaCode1Repository;

    @RequestMapping("/api")
    public String save() {
        String result = "";

        try {
            String urlStr = "https://apis.data.go.kr/B551011/KorWithService1/areaCode1?serviceKey=fHhnNwA7fGBGdq%2FTX99FNNLQJh6pa3CQTHUPpKpk%2FyNHVqEzIDueYm2EKXOq7%2BfjY4fS4KpjCEQBoG3oQ0tTaQ%3D%3D&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&areaCode=1&_type=json";
            URL url = new URL(urlStr);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = br.readLine();

            if (result == null || result.isEmpty()) {
                throw new RuntimeException("API에서 데이터를 받아오지 못했습니다.");
            }

            // JSON 파싱
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject parseResponse = (JSONObject) jsonObject.get("response");
            if (parseResponse == null) {
                throw new RuntimeException("Response가 null입니다.");
            }
            JSONObject parseBody = (JSONObject) parseResponse.get("body");
            JSONObject parseItems = (JSONObject) parseBody.get("items");
            JSONArray array = (JSONArray) parseItems.get("item");

            for (int i = 0; i < array.size(); i++) {
                JSONObject tmp = (JSONObject) array.get(i);
                AreaCode areaCode1 = new AreaCode(
                        i + 1L,
                        ((Number) tmp.get("rnum")).longValue(),  // 수정된 부분
                        (String) tmp.get("code"),
                        (String) tmp.get("name"));
                areaCode1Repository.save(areaCode1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage(); // 오류 메시지 반환
        }
        return "Data saved successfully!"; // 성공 메시지
    }
}
