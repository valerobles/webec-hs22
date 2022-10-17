package ch.fhnw.webec.contactlist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContactControllerIT {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void testDisplayAllContact(){
        WebDriver driver = new HtmlUnitDriver();
        driver.navigate().to("http://localhost:" + port + "/contacts");
        var contactList = driver.findElements(By.cssSelector("#contacts > nav  li"));
        Assertions.assertEquals(30, contactList.size());


    }
}
