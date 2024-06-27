import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContactServiceTest {
    private ContactService service;
    private Contact contact;

    @BeforeEach
    public void setUp() {
        service = new ContactService();
        contact = new Contact("1234567890", "John", "Doe", "0123456789", "123 Main St");
        service.addContact(contact);
    }

    @AfterEach
    public void tearDown() {
        service = null;
        contact = null;
    }

    @Test
    public void testAddContact() {
        assertEquals(contact, service.getContact("1234567890"));
    }

    @Test
    public void testDeleteContact() {
        service.deleteContact("1234567890");
        assertNull(service.getContact("1234567890"));
    }

    @Test
    public void testUpdateContact() {
        service.updateContact("1234567890", "Jane", "Doe", "0987654321", "456 Elm St");
        assertEquals("Jane", contact.getFirstName());
        assertEquals("0987654321", contact.getPhone());
        assertEquals("456 Elm St", contact.getAddress());
    }
}
