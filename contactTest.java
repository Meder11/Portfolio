import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContactTest {
   
    public void testContactCreation() {
        Contact contact = new Contact("1234567890", "John", "Doe", "0123456789", "123 Main St");
        assertEquals("1234567890", contact.getContactID());
        assertEquals("John", contact.getFirstName());
        assertEquals("Doe", contact.getLastName());
        assertEquals("0123456789", contact.getPhone());
        assertEquals("123 Main St", contact.getAddress());
    }

   
    public void testInvalidContactID() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact(null, "John", "Doe", "0123456789", "123 Main St");
        });
    }

   
    public void testContactCreation2() {
        Contact contact = new Contact("290893280", "Eddy", "Marcel", "1234567890", "621 Don Ave");
        assertEquals("290893280", contact.getContactID());
        assertEquals("Eddy", contact.getFirstName());
        assertEquals("Marcel", contact.getLastName());
        assertEquals("1234567890", contact.getPhone());
        assertEquals("621 Don Ave", contact.getAddress());
    }

   
    public void testInvalidContact2() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact(null, "Eddy", "Marcel", "1234567890", "621 Don Ave");
        });
    }
}
