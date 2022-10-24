package ch.fhnw.webec.contactlist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AboutPageIT {

    @Value("${local.server.port}")
    int port;

    @Test
    void fhnwLink() {
        var driver = new HtmlUnitDriver();
        driver.navigate().to("http://localhost:%d/about".formatted(port));
        var fhnwLink = driver.findElement(By.partialLinkText("FHNW"));
        assertEquals("https://www.fhnw.ch/de/studium/technik", fhnwLink.getAttribute("href"));
    }

    @Test
    void statsTable() {
        var driver = new HtmlUnitDriver();
        driver.navigate().to("http://localhost:%d/about".formatted(port));
        var tables = driver.findElements(By.tagName("table"));
        assertEquals(1, tables.size());
        var table = tables.get(0);
        assertEquals("30", table.findElement(By.id("contact-count")).getText());
        assertEquals("49", table.findElement(By.id("phone-number-count")).getText());
        assertEquals("45", table.findElement(By.id("email-count")).getText());
    }
}
