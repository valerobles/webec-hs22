package ch.fhnw.webec.contactlist.data;

import ch.fhnw.webec.contactlist.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ContactRepository extends JpaRepository<Contact, Integer> {


}
