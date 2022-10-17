package ch.fhnw.webec.contactlist;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Optional;

public class ContactsPage {

    public static ContactsPage create(WebDriver driver, int port){
        driver.navigate().to("http://localhost:" + port + "/contacts");
        return PageFactory.initElements(driver,ContactsPage.class);
    }

    @FindBy(css = "#contacts > nav  li")
    private List<WebElement> links;

    @FindBy(css = "#contacts > nav li a")
    private List<WebElement> table;

    public List<WebElement> getLinks() {
        return links;
    }

    public Optional<WebElement> getTable(){
        return table.stream().findFirst();
    }


}
