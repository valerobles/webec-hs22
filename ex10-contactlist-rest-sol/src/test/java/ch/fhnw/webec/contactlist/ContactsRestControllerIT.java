package ch.fhnw.webec.contactlist;

import ch.fhnw.webec.contactlist.data.ContactRepository;
import ch.fhnw.webec.contactlist.model.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureTestDatabase
public class ContactsRestControllerIT {

    @Autowired
    TestRestTemplate template;
    @Autowired
    ContactRepository repo;

    @Test
    public void getAll() {
        var response = template.exchange("/api/contact", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Contact>>() {});
        var list = response.getBody();
        assertEquals(30, list.size());
        var first = list.get(0);
        assertEquals("Mabel", first.getFirstName());
        assertEquals("Guppy", first.getLastName());
    }

    @Test
    public void get() {
        // determine ID first
        var id = repo.findAll().stream().findFirst().get().getId();

        var contact = template.getForObject("/api/contact/" + id, Contact.class);
        assertEquals("Mabel", contact.getFirstName());
        assertEquals("Guppy", contact.getLastName());
    }

    @Test
    @DirtiesContext
    public void deleteContact() {
        var listBefore = template.exchange("/api/contact", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Contact>>() {}).getBody();
        assertEquals(30, listBefore.size());

        var id = repo.findAll().stream().findFirst().get().getId();
        var response = template.exchange("/api/contact/" + id, HttpMethod.DELETE, null, Void.class);
        assertEquals(NO_CONTENT, response.getStatusCode());

        var listAfter = template.getForObject("/api/contact", List.class);
        assertEquals(29, listAfter.size());

        response = template.getForEntity("/api/contact/" + id, Void.class);
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    public void replaceContact() {
        var id = repo.findAll().stream().findFirst().get().getId();

        var contactBefore = template.getForObject("/api/contact/" + id, Contact.class);
        assertEquals("Mabel", contactBefore.getFirstName());
        assertEquals("Guppy", contactBefore.getLastName());

        var newContact = new Contact();
        newContact.setFirstName("Tester");
        var request = RequestEntity.put("/api/contact/" + id)
                .contentType(APPLICATION_JSON)
                .body(newContact);
        var response = template.exchange(request, String.class);
        assertNull(response.getBody());
        assertEquals(NO_CONTENT, response.getStatusCode());

        var contactAfter = template.getForObject("/api/contact/" + id, Contact.class);
        assertEquals("Tester", contactAfter.getFirstName());
        assertNull(contactAfter.getLastName());
        assertNull(contactAfter.getCompany());
        assertNull(contactAfter.getJobTitle());
        assertEquals(emptyList(), contactAfter.getEmail());
        assertEquals(emptyList(), contactAfter.getPhone());
    }

    @Test
    @DirtiesContext
    public void addContact() {
        // determine max ID first to infer next one (this is not super robust...)
        var id = repo.findAll().stream().mapToInt(Contact::getId).max().getAsInt() + 1;

        var responseBefore = template.getForEntity("/api/contact/" + id, Contact.class);
        assertEquals(NOT_FOUND, responseBefore.getStatusCode());
        assertNull(responseBefore.getBody());

        var newContact = new Contact();
        newContact.setFirstName("Mike");
        newContact.setLastName("Miller");
        var request = RequestEntity.post("/api/contact")
                .contentType(APPLICATION_JSON)
                .body(newContact);
        var response = template.exchange(request, String.class);
        assertNull(response.getBody());
        assertEquals(CREATED, response.getStatusCode());

        var location = response.getHeaders().getLocation();
        assertNotNull(location);
        assertEquals("/api/contact/" + id, location.toString());

        var responseAfter = template.getForEntity("/api/contact/" + id, Contact.class);
        assertEquals(OK, responseAfter.getStatusCode());
        var contactAfter = responseAfter.getBody();
        assertNotNull(contactAfter);
        assertEquals("Mike", contactAfter.getFirstName());
        assertEquals("Miller", contactAfter.getLastName());
        assertNull(contactAfter.getCompany());
        assertNull(contactAfter.getJobTitle());
        assertEquals(emptyList(), contactAfter.getEmail());
        assertEquals(emptyList(), contactAfter.getPhone());
    }
}
