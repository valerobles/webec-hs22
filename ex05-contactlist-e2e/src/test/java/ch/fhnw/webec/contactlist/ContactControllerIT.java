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
        var page = ContactsPage.create(driver,port);
        Assertions.assertEquals(30, page.getLinks().size());


    }

    @Test
    public void testClickContactLink(){
        var driver = new HtmlUnitDriver();
        var page = ContactsPage.create(driver,port);
        page.getLinks().get(0).click();
        var table = page.getTable();
        Assertions.assertTrue(table.isPresent());
        Assertions.assertTrue(table.get().getText().contains("Mabel"));

    }


}
