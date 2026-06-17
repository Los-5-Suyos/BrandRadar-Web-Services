package brandradar.reputationmonitoring.infrastructure.providers;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class GoogleNewsRssProvider {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public List<Mention> fetchMentions(Long brandId, String brandName) {
        List<Mention> mentions = new ArrayList<>();
        try {
            String encoded = URLEncoder.encode(brandName, StandardCharsets.UTF_8);
            String url = "https://news.google.com/rss/search?q=" + encoded + "&hl=es&gl=PE&ceid=PE:es";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "application/rss+xml, application/xml, text/xml")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(
                        new java.io.ByteArrayInputStream(response.body().getBytes(StandardCharsets.UTF_8)));

                NodeList items = doc.getElementsByTagName("item");
                int limit = Math.min(items.getLength(), 10);

                for (int i = 0; i < limit; i++) {
                    Element item = (Element) items.item(i);
                    String title = getTagValue("title", item);
                    String link = getTagValue("link", item);
                    String pubDate = getTagValue("pubDate", item);
                    String source = getTagValue("source", item);

                    if (title == null || title.isBlank()) continue;

                    Instant publishedAt = Instant.now();
                    if (pubDate != null && !pubDate.isBlank()) {
                        try {
                            DateTimeFormatter fmt = DateTimeFormatter.ofPattern(
                                    "EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                            publishedAt = ZonedDateTime.parse(pubDate, fmt).toInstant();
                        } catch (Exception ignored) {}
                    }

                    mentions.add(Mention.create(
                            brandId, title, "NEWS",
                            link != null ? link : "",
                            source != null ? source : "Google News",
                            publishedAt));
                }
                log.info("GoogleNewsRssProvider - Fetched {} mentions for brand={}", mentions.size(), brandName);
            }
        } catch (Exception e) {
            log.error("GoogleNewsRssProvider - Error for brand={}: {}", brandName, e.getMessage(), e);
        }
        return mentions;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodes = element.getElementsByTagName(tag);
        if (nodes.getLength() > 0 && nodes.item(0).getChildNodes().getLength() > 0) {
            return nodes.item(0).getChildNodes().item(0).getNodeValue();
        }
        return null;
    }
}