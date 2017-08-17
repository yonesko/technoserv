package gleb.config;

import com.rometools.rome.feed.synd.SyndEntry;
import org.springframework.beans.factory.annotation.Value;
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
    public MessageSource<SyndEntry> rssMessageSource(@Value("${feed.url}") String url) throws MalformedURLException {
        return new FeedEntryMessageSource(new URL(url), "news");
    }

    @Bean
    public IntegrationFlow pollingFlow(MessageSource<SyndEntry> messageSource, SolrIndexingServiceActivator activator) throws MalformedURLException {
        return IntegrationFlows.from(messageSource, c -> c.poller(Pollers.fixedRate(3, TimeUnit.SECONDS)))
                .handle(activator)
                .get();
    }
}
