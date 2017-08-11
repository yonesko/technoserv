package gleb.data;

import java.time.ZonedDateTime;

public class NewsItem {
    private final String content;
    private final ZonedDateTime publicationDateTime;

    public NewsItem(String content, ZonedDateTime publicationDateTime) {
        this.content = content;
        this.publicationDateTime = publicationDateTime;
    }

    public String getContent() {
        return content;
    }

    public ZonedDateTime getPublicationDateTime() {
        return publicationDateTime;
    }
}
