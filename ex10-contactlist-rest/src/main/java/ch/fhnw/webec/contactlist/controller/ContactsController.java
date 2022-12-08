package ch.fhnw.webec.contactlist.controller;

import ch.fhnw.webec.contactlist.service.ContactService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

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

    @GetMapping("/contacts/add")
    public String addContact() {
        return "add-contact";
    }

    @PostMapping("/contacts/add")
    public String addContact(@RequestParam @NotBlank String firstName,
                             @RequestParam @NotBlank String lastName,
                             @RequestParam String jobTitle,
                             @RequestParam String company) {
        var created = service.add(firstName.strip(), lastName.strip(),
                jobTitle.isBlank() ? null : jobTitle.strip(),
                company.isBlank() ? null : company.strip());
        return "redirect:/contacts/" + created.getId();
    }

    @GetMapping("/contacts/{id}")
    public String showContact(@PathVariable int id, String search, Model model) {
        checkSearch(search);
        var contact = service.findContact(id).orElseThrow(ContactNotFound::new);
        model.addAttribute("contactList", service.getContactList(search));
        model.addAttribute("contact", contact);
        return "contacts";
    }

    @GetMapping("/contacts/{id}/edit")
    public String editContact(@PathVariable int id, Model model) {
        var contact = service.findContact(id).orElseThrow(ContactNotFound::new);
        model.addAttribute("contact", contact);
        return "edit-contact";
    }

    @PostMapping("/contacts/{id}/edit")
    public String editContact(@PathVariable int id,
                              @RequestParam @NotBlank String firstName,
                              @RequestParam @NotBlank String lastName,
                              @RequestParam String jobTitle,
                              @RequestParam String company,
                              @RequestParam String addEmail,
                              @RequestParam String addPhone,
                              HttpServletRequest request) {
        var contact = service.findContact(id).orElseThrow(ContactNotFound::new);
        contact.setFirstName(firstName.strip());
        contact.setLastName(lastName.strip());
        contact.setJobTitle(jobTitle.isBlank() ? null : jobTitle.strip());
        contact.setCompany(company.isBlank() ? null : company.strip());
        for (int i = contact.getEmail().size(); i >= 0; i--) {
            if (request.getParameterMap().containsKey("deleteEmail" + i)) {
                contact.getEmail().remove(i);
            }
        }
        if (!addEmail.isBlank()) {
            contact.getEmail().add(addEmail.strip());
        }
        for (int i = contact.getPhone().size(); i >= 0; i--) {
            if (request.getParameterMap().containsKey("deletePhone" + i)) {
                contact.getPhone().remove(i);
            }
        }
        if (!addPhone.isBlank()) {
            contact.getPhone().add(addPhone.strip());
        }
        service.update(contact);
        return "redirect:/contacts/" + id;
    }

    @PostMapping("/contacts/{id}/delete")
    public String deleteContact(@PathVariable int id) {
        var contact = service.findContact(id).orElseThrow(ContactNotFound::new);
        service.delete(contact);
        return "redirect:/contacts";
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
