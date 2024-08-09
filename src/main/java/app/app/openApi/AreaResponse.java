package app.app.openApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AreaResponse {

    @JsonProperty("response")
    private Response response;

    // Getter and Setter

    public static class Response {

        @JsonProperty("header")
        private Header header;

        @JsonProperty("body")
        private Body body;

        // Getter and Setter
    }

    public static class Header {
        @JsonProperty("resultCode")
        private String resultCode;

        @JsonProperty("resultMsg")
        private String resultMsg;

        // Getter and Setter
    }

    public static class Body {
        @JsonProperty("items")
        private Items items;

        @JsonProperty("numOfRows")
        private int numOfRows;

        @JsonProperty("pageNo")
        private int pageNo;

        @JsonProperty("totalCount")
        private int totalCount;

        // Getter and Setter
    }

    public static class Items {
        @JsonProperty("item")
        private List<Item> itemList;

        // Getter and Setter
    }

    public static class Item {
        @JsonProperty("rnum")
        private int rnum;

        @JsonProperty("code")
        private String code;

        @JsonProperty("name")
        private String name;

        // Getter and Setter
    }
}
