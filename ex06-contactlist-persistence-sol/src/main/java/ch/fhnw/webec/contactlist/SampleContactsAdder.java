package ch.fhnw.webec.contactlist;

import ch.fhnw.webec.contactlist.model.Contact;
import ch.fhnw.webec.contactlist.service.ContactService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@ConditionalOnProperty("contact-list.add-sample-contacts")
public class SampleContactsAdder implements CommandLineRunner {

    public static final String JSON_FILE = "contacts.json";

    private static final Logger logger = LoggerFactory.getLogger(SampleContactsAdder.class);

    private final ObjectMapper mapper;
    private final ContactService contactService;

    public SampleContactsAdder(ObjectMapper mapper,
                               ContactService contactService) {
        this.mapper = mapper;
        this.contactService = contactService;
    }

    @Override
    public void run(String... args) throws IOException {
        addSampleContacts();
    }

    public void addSampleContacts() throws IOException {
        if (contactService.getContactList(null).isEmpty()) {
            logger.info("Adding sample contacts");
            loadSampleContacts(mapper).forEach(contactService::add);
        }
    }

    public static List<Contact> loadSampleContacts(ObjectMapper mapper) throws IOException {
        return mapper.readValue(SampleContactsAdder.class.getResource(JSON_FILE),
                new TypeReference<List<Contact>>() {});
    }
}
