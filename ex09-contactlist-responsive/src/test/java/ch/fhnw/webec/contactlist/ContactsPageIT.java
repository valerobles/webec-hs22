package ch.fhnw.webec.contactlist;

import ch.fhnw.webec.contactlist.pages.ContactsPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ContactsPageIT {

    @Value("${local.server.port}")
    int port;

    HtmlUnitDriver driver = new HtmlUnitDriver();

    @Test
    void initialPageShowsNoContactDetails() {
        var page = ContactsPage.create(driver, port);
        assertEquals(empty(), page.getContactDetails());
        assertTrue(page.getSelectContactMessage().isPresent());
    }

    @Test
    void clickingContactShowsContactDetails() {
        var page = ContactsPage.create(driver, port);
        page.getContactLinks().get(0).click();
        assertTrue(page.getContactDetails().isPresent());
    }

    @Test
    void clickingContactShowsCorrectContactDetails() {
        var page = ContactsPage.create(driver, port);
        page.getContactLinks().get(1).click();

        assertEquals(Optional.of("Lauree"), page.getSelectedFirstName());
        assertEquals(Optional.of("Clouter"), page.getSelectedLastName());
        assertEquals(Optional.of("alyman0@economist.com"), page.getSelectedEmails());
        assertThat(page.getSelectedPhones().orElseThrow(AssertionFailedError::new))
                .contains(List.of("636-671-9948", "976-316-0820", "773-304-6113"));
        assertEquals(Optional.of("Senior Editor"), page.getSelectedJobTitle());
        assertEquals(Optional.of("Livepath"), page.getSelectedCompany());
    }

    @Test
    void emptyEmailsNotShown() {
        var page = ContactsPage.create(driver, port);
        page.getContactLinks().get(0).click();

        assertEquals(Optional.of("Mabel"), page.getSelectedFirstName());
        assertEquals(Optional.of("Guppy"), page.getSelectedLastName());
        assertEquals(Optional.empty(), page.getSelectedEmails());
    }

    @Test
    void emptyPhonesNotShown() {
        var page = ContactsPage.create(driver, port);
        page.getContactLinks().get(3).click();

        assertEquals(Optional.of("Bax"), page.getSelectedFirstName());
        assertEquals(Optional.of("McGrath"), page.getSelectedLastName());
        assertEquals(Optional.empty(), page.getSelectedPhones());
    }

    @Test
    void emptyJobTitleAndCompanyNotShown() {
        var page = ContactsPage.create(driver, port);
        page.getContactLinks().get(5).click();

        assertEquals(Optional.of("Moll"), page.getSelectedFirstName());
        assertEquals(Optional.of("Mullarkey"), page.getSelectedLastName());
        assertEquals(Optional.empty(), page.getSelectedJobTitle());
        assertEquals(Optional.empty(), page.getSelectedCompany());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/contacts", "/contacts/1", "/contacts/23"})
    void clearButtonAbsentBeforeSearch(String path) {
        var page = ContactsPage.create(driver, port, path);
        assertEquals(Optional.empty(), page.getSearchClearButton());
    }

    @Test
    void clearButtonPresentAfterSearch() {
        var page = ContactsPage.create(driver, port);
        page.getSearchField().sendKeys("mo");
        page.getSearchSubmitButton().submit();

        assertTrue(page.getSearchClearButton().isPresent());
    }

    @Test
    void searchFiltersContactList() {
        var page = ContactsPage.create(driver, port);
        page.getSearchField().sendKeys("mo");
        page.getSearchSubmitButton().submit();

        assertEquals(Set.of("Moll Mullarkey", "Seana Burberye"),
                page.getContactLinks().stream().map(WebElement::getText).collect(toSet()));
    }

    @Test
    void searchShowsNumberOfResults() {
        var page = ContactsPage.create(driver, port);
        page.getSearchField().sendKeys("com");
        page.getSearchSubmitButton().submit();

        var msg = page.getContactsFoundMessage().orElseThrow(AssertionFailedError::new);
        assertThat(msg.getText()).contains("19");
    }

    @Test
    void clearShowsAllContactsAgain() {
        var page = ContactsPage.create(driver, port);
        page.getSearchField().sendKeys("mo");
        page.getSearchSubmitButton().submit();

        page.getSearchClearButton().orElseThrow(AssertionFailedError::new).click();

        assertEquals(30, page.getContactLinks().size());
    }

    @Test
    void searchPersistsWhenClickingContact() {
        var page = ContactsPage.create(driver, port);
        page.getSearchField().sendKeys("mo");
        page.getSearchSubmitButton().submit();
        page.getContactLinks().get(0).click();

        assertEquals(Set.of("Moll Mullarkey", "Seana Burberye"),
                page.getContactLinks().stream().map(WebElement::getText).collect(toSet()));

        page.getContactLinks().get(1).click();
        assertEquals(Set.of("Moll Mullarkey", "Seana Burberye"),
                page.getContactLinks().stream().map(WebElement::getText).collect(toSet()));
    }

    @Test
    void shortSearchShowErrorMessage() {
        var page = ContactsPage.create(driver, port, "/contacts?search=e");
        assertTrue(page.getErrorMessage().isPresent());
        assertThat(page.getErrorMessage().get().getText()).containsPattern("at least \\d+ characters");
    }
}
