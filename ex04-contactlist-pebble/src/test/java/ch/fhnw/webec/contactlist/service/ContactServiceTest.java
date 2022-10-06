package ch.fhnw.webec.contactlist.service;

import ch.fhnw.webec.contactlist.model.ContactListEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;
import static org.junit.jupiter.api.Assertions.*;

class ContactServiceTest {

    ContactService service;

    ContactServiceTest() throws IOException {
        var mapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.service = new ContactService(mapper);
    }

    @Test
    void contactListIds() {
        var contactList = service.getContactList();
        assertNotNull(contactList);
        var ids = contactList.stream()
                .map(ContactListEntry::getId)
                .collect(toList());
        assertEquals(rangeClosed(1, 30).boxed().collect(toList()), ids);
    }

    @Test
    void contactListName() {
        var contactList = service.getContactList();
        assertNotNull(contactList);
        assertFalse(contactList.isEmpty());
        assertEquals("Mabel Guppy", contactList.get(0).getName());
    }

    @Test
    void findContact() {
        var contact = service.findContact(1).orElseThrow(AssertionFailedError::new);
        assertEquals(1, contact.getId());
        assertEquals("Mabel", contact.getFirstName());
        assertEquals("Guppy", contact.getLastName());
        assertEquals(List.of("405-580-6403"), contact.getPhone());
        assertEquals(emptyList(), contact.getEmail());
        assertEquals("Librarian", contact.getJobTitle());
        assertEquals("Photolist", contact.getCompany());
    }

    @Test
    void findContactAbsent() {
        var contact = service.findContact(98213);
        assertTrue(contact.isEmpty());
    }
}
