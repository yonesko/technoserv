package gleb.data;

import java.time.ZonedDateTime;

public class NewsItemTTF extends NewsItem {
    private final int totalTermFreq;
    public NewsItemTTF(String id, String content, ZonedDateTime publicationDateTime, int totalTermFreq) {
        super(id, content, publicationDateTime);
        this.totalTermFreq = totalTermFreq;
    }

    public int getTotalTermFreq() {
        return totalTermFreq;
    }
}
