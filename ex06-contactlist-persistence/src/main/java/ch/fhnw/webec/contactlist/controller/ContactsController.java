package ch.fhnw.webec.contactlist.controller;

import ch.fhnw.webec.contactlist.service.ContactService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@Validated
public class ContactsController {

    private final ContactService service;

    @Value("${contact-list.search.min-length}")
    private int minSearchLength;

    public ContactsController(ContactService service) {
        this.service = service;
    }

    @GetMapping("/contacts")
    public String contacts(String search, Model model) {
        checkSearch(search);
        model.addAttribute("contactList", service.getContactList(search));
        return "contacts";
    }

    @GetMapping("/contacts/{id}")
    public String showContact(@PathVariable int id, String search, Model model) {
        checkSearch(search);
        var contact = service.findContact(id).orElseThrow(ContactNotFound::new);
        model.addAttribute("contactList", service.getContactList(search));
        model.addAttribute("contact", contact);
        return "contacts";
    }

    private void checkSearch(String search) {
        if (search != null && search.length() < minSearchLength) {
            throw new InvalidSearch();
        }
    }

    @ExceptionHandler(InvalidSearch.class)
    @ResponseStatus(BAD_REQUEST)
    public String invalidSearch(Model model) {
        model.addAttribute("contactList", service.getContactList(null));
        model.addAttribute("errorMessage",
                "Search text must have at least %s characters".formatted(minSearchLength));
        return "contacts";
    }

    @ExceptionHandler(ContactNotFound.class)
    @ResponseStatus(NOT_FOUND)
    public String notFound(Model model) {
        model.addAttribute("contactList", service.getContactList(null));
        model.addAttribute("errorMessage", "Contact not found");
        return "contacts";
    }

    private static class InvalidSearch extends RuntimeException {}

    private static class ContactNotFound extends RuntimeException {}
}
