package app.app.openApi.Controller;

import app.app.openApi.AreaCode;
import app.app.openApi.AreaResponse;
import app.app.openApi.Repository.AreaCodeRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AreaCodeController {

    private final AreaCodeRepository areaCodeRepository;
    private final HttpServletResponse httpServletResponse

    @GetMapping("/api/area-info")
    public ResponseEntity<String> getAreaInfo(@RequestParam String areaCode) {
        String urlStr = "https://apis.data.go.kr/B551011/KorWithService1/areaCode1?serviceKey=fHhnNwA7fGBGdq%2FTX99FNNLQJh6pa3CQTHUPpKpk%2FyNHVqEzIDueYm2EKXOq7%2BfjY4fS4KpjCEQBoG3oQ0tTaQ%3D%3D&numOfRows=100&pageNo=1&MobileOS=AND&MobileApp=AppTest&areaCode=" + areaCode + "&_type=json";

        try {
            URL url = new URL(urlStr);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            // JSON 파싱
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            JSONObject parseResponse = (JSONObject) jsonObject.get("response");

            if (parseResponse == null) {
                return new ResponseEntity<>("지역 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
            }

            JSONObject parseBody = (JSONObject) parseResponse.get("body");
            JSONObject parseItems = (JSONObject) parseBody.get("items");
            JSONArray array = (JSONArray) parseItems.get("item");

            if (array == null || array.isEmpty()) {
                return new ResponseEntity<>("해당 지역 코드에 대한 정보가 없습니다.", HttpStatus.NOT_FOUND);
            }

            // 첫 번째 아이템만 반환
            JSONObject areaInfo = (JSONObject) array.get(0);
            return new ResponseEntity<>(areaInfo.toJSONString(), HttpStatus.OK);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>("오류 발생: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static final String API_URL = "https://apis.data.go.kr/B551011/KorWithService1/areaCode1?serviceKey=YOUR_SERVICE_KEY&numOfRows=100&pageNo=1&MobileOS=AND&MobileApp=AppTest&_type=json";

    @GetMapping("/getArea/{code}")
    public Map<String, Object> getArea(@PathVariable String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> result = new HashMap<>();

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                API_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> response = responseEntity.getBody();

        if (response != null && response.get("response") instanceof Map) {
            Map<String, Object> resp = (Map<String, Object>) response.get("response");

            if (resp.get("body") instanceof Map) {
                Map<String, Object> body = (Map<String, Object>) resp.get("body");

                if (body.get("items") instanceof Map) {
                    Map<String, Object> items = (Map<String, Object>) body.get("items()");

                    if (items.get("item") instanceof List) {
                        // items 필드가 List 타입일 경우
                        List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");

                        // 코드에 해당하는 지역을 찾는다.
                        for (Map<String, Object> item : itemList) {
                            if (item.get("code").equals(code)) {
                                return item;  // 코드에 해당하는 지역 반환
                            }
                        }
                    } else {
                        result.put("message", "items 필드가 올바르지 않습니다.");
                    }
                } else {
                    result.put("message", "body에 items가 없습니다.");
                }
            } else {
                result.put("message", "응답에 body가 없습니다.");
            }
        } else {
            result.put("message", "응답이 null이거나 올바르지 않습니다.");
        }

        result.put("message", "해당 지역 코드를 찾을 수 없습니다.");
        return result;
    }

}
