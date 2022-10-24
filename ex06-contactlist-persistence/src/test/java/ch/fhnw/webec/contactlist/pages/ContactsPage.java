package ch.fhnw.webec.contactlist.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Optional;

public class ContactsPage {

    private static final String BASE_URL = "http://localhost:";

    public static ContactsPage create(WebDriver driver, int port) {
        return create(driver, port, "/contacts");
    }

    public static ContactsPage create(WebDriver driver, int port, String path) {
        driver.get(BASE_URL + port + path);
        return PageFactory.initElements(driver, ContactsPage.class);
    }

    @FindBy(css = "#contacts table")
    private List<WebElement> contactDetails;

    @FindBy(id = "select-contact-msg")
    private List<WebElement> selectContactMessage;

    @FindBy(id = "contact-error")
    private List<WebElement> errorMessage;

    @FindBy(css = "#contacts > nav li a")
    private List<WebElement> contactLinks;

    @FindBy(id = "first-name-value")
    private List<WebElement> firstNameValue;

    @FindBy(id = "last-name-value")
    private List<WebElement> lastNameValue;

    @FindBy(id = "email-value")
    private List<WebElement> emailValue;

    @FindBy(id = "phone-value")
    private List<WebElement> phoneValue;

    @FindBy(id = "job-title-value")
    private List<WebElement> jobTitleValue;

    @FindBy(id = "company-value")
    private List<WebElement> companyValue;

    @FindBy(css = "#contact-search input[type=text]")
    private WebElement searchField;

    @FindBy(css = "#contact-search input[type=submit]")
    private WebElement searchSubmitButton;

    @FindBy(css = "#contact-search .clear")
    private List<WebElement> searchClearButton;

    @FindBy(id = "contacts-found-msg")
    private List<WebElement> contactsFoundMessage;

    public Optional<WebElement> getContactDetails() {
        return contactDetails.stream().findFirst();
    }

    public Optional<WebElement> getSelectContactMessage() {
        return selectContactMessage.stream().findFirst();
    }

    public Optional<WebElement> getErrorMessage() {
        return errorMessage.stream().findFirst();
    }

    public List<WebElement> getContactLinks() {
        return contactLinks;
    }

    public Optional<String> getSelectedFirstName() {
        return extractText(firstNameValue);
    }

    public Optional<String> getSelectedLastName() {
        return extractText(lastNameValue);
    }

    public Optional<String> getSelectedEmails() {
        return extractText(emailValue);
    }

    public Optional<String> getSelectedPhones() {
        return extractText(phoneValue);
    }

    public Optional<String> getSelectedJobTitle() {
        return extractText(jobTitleValue);
    }

    public Optional<String> getSelectedCompany() {
        return extractText(companyValue);
    }

    public WebElement getSearchField() {
        return searchField;
    }

    public WebElement getSearchSubmitButton() {
        return searchSubmitButton;
    }

    public Optional<WebElement> getSearchClearButton() {
        return searchClearButton.stream().findFirst();
    }

    public Optional<WebElement> getContactsFoundMessage() {
        return contactsFoundMessage.stream().findFirst();
    }

    private static Optional<String> extractText(List<WebElement> element) {
        return element.stream().findFirst().map(WebElement::getText);
    }
}
