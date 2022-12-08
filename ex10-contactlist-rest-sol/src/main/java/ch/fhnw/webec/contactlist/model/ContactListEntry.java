package ch.fhnw.webec.contactlist.model;

public class ContactListEntry {

    private final int id;
    private final String name;

    public ContactListEntry(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
