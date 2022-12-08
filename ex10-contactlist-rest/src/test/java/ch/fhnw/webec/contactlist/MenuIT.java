package ch.fhnw.webec.contactlist;

import ch.fhnw.webec.contactlist.pages.Page;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class MenuIT {

    @Value("${local.server.port}")
    int port;

    HtmlUnitDriver driver = new HtmlUnitDriver();

    @ParameterizedTest
    @ValueSource(strings = {"/", "/contacts", "/contacts/1", "/contacts/23", "/about"})
    void containsMenu(String path) {
        var page = Page.create(driver, port, path);
        var hrefs = page.getMenuLinks().stream()
                .map(e -> path(e.getAttribute("href")))
                .collect(toList());
        assertEquals(List.of("/", "/contacts", "/about"), hrefs);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/", "/contacts", "/contacts/1", "/contacts/23", "/about"})
    void correctMenuLinkHighlighted(String path) {
        var page = Page.create(driver, port, path);
        var links = page.getMenuLinks();

        // for "/contacts/{id}" the link with "/contacts" should be highlighted, otherwise
        // simply the link with the current path
        var highlightedPath = path.replaceAll("/\\d+", "");
        for (var link : links) {
            var classValue = link.getAttribute("class");
            var classes = classValue != null ? classValue : "";
            assertEquals(path(link.getAttribute("href")).equals(highlightedPath),
                    classes.contains("active"));
        }
    }

    private static String path(String url) {
        return url.replaceAll("http://[^/]+", "");
    }
}
