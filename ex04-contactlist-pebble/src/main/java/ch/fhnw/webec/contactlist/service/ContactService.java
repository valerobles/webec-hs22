package ch.fhnw.webec.contactlist.service;

import ch.fhnw.webec.contactlist.model.Contact;
import ch.fhnw.webec.contactlist.model.ContactListEntry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class ContactService {

    private static final String JSON_FILE = "contacts.json";

    private final Map<Integer, Contact> contacts;

    public ContactService(ObjectMapper mapper) throws IOException {
        var contactsList = mapper.readValue(ContactService.class.getResource(JSON_FILE),
                new TypeReference<List<Contact>>() {});
        contacts = contactsList.stream()
                .collect(toMap(Contact::getId, identity()));
    }

    public List<ContactListEntry> getContactList() {
        return contacts.values().stream()
                .sorted(comparing(Contact::getId))
                .map(c -> new ContactListEntry(c.getId(), c.getFirstName() + " " + c.getLastName()))
                .collect(toList());
    }

    public Optional<Contact> findContact(int id) {
        return Optional.ofNullable(contacts.get(id));
    }
}
