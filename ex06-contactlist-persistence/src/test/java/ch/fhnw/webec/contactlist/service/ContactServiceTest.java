package ch.fhnw.webec.contactlist.service;

import ch.fhnw.webec.contactlist.SampleContactsAdder;
import ch.fhnw.webec.contactlist.model.ContactListEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.rangeClosed;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ContactServiceTest {

    ContactService service;

    ContactServiceTest() throws IOException {
        var mapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        service = new ContactService();
        new SampleContactsAdder(mapper, service).addSampleContacts();
    }

    @Test
    void contactListIds() {
        var contactList = service.getContactList(null);
        assertNotNull(contactList);
        var ids = contactList.stream()
                .mapToInt(ContactListEntry::getId)
                .toArray();
        assertArrayEquals(rangeClosed(1, 30).toArray(), ids);
    }

    @Test
    void contactListName() {
        var contactList = service.getContactList(null);
        assertNotNull(contactList);
        Assertions.assertFalse(contactList.isEmpty());
        Assertions.assertEquals("Mabel Guppy", contactList.get(0).getName());
    }

    @Test
    void search() {
        var results = service.getContactList("Engineer");
        Assertions.assertEquals(Set.of("Graeme Impett", "Chilton Treversh"),
                results.stream().map(ContactListEntry::getName).collect(toSet()));
    }

    @Test
    void searchIgnoresCase() {
        var results = service.getContactList("mO");
        Assertions.assertEquals(Set.of("Moll Mullarkey", "Seana Burberye"), // one in name, one in email
                results.stream().map(ContactListEntry::getName).collect(toSet()));
    }
}

