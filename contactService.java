import java.util.HashMap;
import java.util.Map;

public class ContactService {

	private Map<String, Contact> contacts = new HashMap<>();
	
	public void addContact(Contact contact) {
		if(contacts.containsKey(contact.getContactID())) {
			System.out.print("Contact ID already exists");
		}
		contacts.put(contact.getContactID(), contact);
	}

	public void deleteContact(String contactID) {
		if(!contacts.containsKey(contactID)) {
			System.out.print("Contact ID not found");
		}
		contacts.remove(contactID);
	}

	public void updateContact(String contactID, String firstName, String lastName, String phone, String address) {
		Contact contact = contacts.get(contactID);
		if(contact == null) {
			System.out.print("Contact ID not found");
			return;
		}
		if (firstName != null && firstName.length() <= 10) {
			contact.setFirstName(firstName); 
		}
		if(lastName != null && lastName.length() <= 10) {
			contact.setLastName(lastName);
		}
		if(phone != null && phone.length() <= 10) {
			contact.setPhone(phone);
		}
		if (address != null && address.length() <= 30) {
			contact.setAddress(address);
		}
	}

	public Contact getContact(String contactID) {
		return contacts.get(contactID);
	}
}
