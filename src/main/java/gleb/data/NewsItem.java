package gleb.data;

import java.time.ZonedDateTime;

public class NewsItem {
    private final String id;
    private final String content;
    private final ZonedDateTime publicationDateTime;

    public NewsItem(String id, String content, ZonedDateTime publicationDateTime) {
        this.id = id;
        this.content = content;
        this.publicationDateTime = publicationDateTime;
    }

    public String getContent() {
        return content;
    }

    public ZonedDateTime getPublicationDateTime() {
        return publicationDateTime;
    }

    public String getId() {
        return id;
    }
}
