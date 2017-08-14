package gleb.config;

import com.rometools.rome.feed.synd.SyndEntry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Configuration
public class Integration {

    @Bean
    public MessageSource<SyndEntry> rssMessageSource() throws MalformedURLException {
        return new FeedEntryMessageSource(new URL("https://lenta.ru/rss"), "news");
    }

    @Bean
    public IntegrationFlow pollingFlow(SolrIndexingServiceActivator activator) throws MalformedURLException {
        return IntegrationFlows.from(rssMessageSource(), c -> c.poller(Pollers.fixedRate(3, TimeUnit.SECONDS )))
                .handle(activator)
                .get();
    }
}
