public class Contact {
	private String contactID;
	private String firstName;
	private String lastName;
	private String phone;
	private String address;

	public Contact(String contactID, String firstName, String lastName, String phone, String address) {
		if(contactID == null || contactID.length() > 10) {
			System.out.print("Invalid contact ID");
		}
		if(firstName == null || firstName.length() > 10) {
			System.out.print("Invalid first name");
		}
		if(lastName == null || lastName.length() > 10) {
			System.out.print("Invalid last name");
		}
		if(phone == null || phone.length() > 10) {
			System.out.print("Invalid phone");
		}
		if(address == null || address.length() > 30) {
			System.out.print("Invalid address");
		}

		this.contactID = contactID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.address = address;
	}
	
	//getters and setters

	public String getContactID() {
		return contactID;
	}

	public void setContactID(String contactID) {
		if(contactID == null || contactID.length() > 10 ) {
			System.out.print("Invalid contact ID");
		}
		this.contactID = contactID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if(firstName == null || firstName.length() > 10) {
			System.out.print("Invalid first name");
		}
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if(lastName == null || lastName.length() > 10) {
			System.out.print("Invalid last name");
		}
		
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		if(phone == null || phone.length() > 10) {
			System.out.print("Invalid phone");
		}
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		if(address == null || address.length() > 30) {
			System.out.print("Invalid address");
		}
		this.address = address;
	}
}


	
	


