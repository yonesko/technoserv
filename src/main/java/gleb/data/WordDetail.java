package gleb.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordDetail {
    private final List<String> contentList;
    private final Map<String, Integer> dayToTTF;

    public WordDetail() {
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
