package ch.fhnw.webec.contactlist.controller;

import ch.fhnw.webec.contactlist.model.Contact;
import ch.fhnw.webec.contactlist.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactsRestController {

    private final ContactService contactService;

    public ContactsRestController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public List<Contact> getAll() {
        return contactService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Contact> get(@PathVariable int id) {
        return ResponseEntity.of(contactService.findContact(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable int id) {
        var contact = contactService.findContact(id).orElse(null);
        if (contact == null) {
            return ResponseEntity.notFound().build();
        } else {
            contactService.delete(contact);
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<String> replaceContact(@PathVariable int id,
                                                 @RequestBody Contact newContact) {
        var original = contactService.findContact(id).orElse(null);
        if (original == null) {
            return ResponseEntity.notFound().build();
        } else {
            newContact.setId(id); // in case JSON contains no or different ID
            contactService.update(newContact);
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> addContact(@RequestBody Contact contact) {
        var saved = contactService.add(contact);
        return ResponseEntity.created(URI.create("/api/contact/" + saved.getId())).build();
    }
}
