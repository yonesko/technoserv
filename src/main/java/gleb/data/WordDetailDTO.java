package gleb.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordDetailDTO {
    private List<String> contentList;
    private Map<String, Integer> dayToTTF;

    public WordDetailDTO() {
        contentList = new ArrayList<>();
        dayToTTF = new HashMap<>();
    }

    public List<String> getContentList() {
        return contentList;
    }

    public Map<String, Integer> getDayToTTF() {
        return dayToTTF;
    }
}
