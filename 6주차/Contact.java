import java.util.*;

public class Contact {
	
	static HashMap<String, String> contact = new HashMap<String, String>();
		
	// show all contents
	void show() {
		
		// HashMap 에서는 정렬을 지원하지 않음.
		List<Map.Entry<String, String>> list = new ArrayList<>(contact.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        for (Map.Entry<String, String> entry : list) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
	}
	
	// find phoneNumber
	String find(String name) {
		return contact.get(name);
	}
	
	// add information
	void add(String name, String phone) {
		if (contact.get(name) != null) {
			System.out.println("error");
		} else {
			contact.put(name, phone);
		}
	}
	
	// delete information
	void delete(String name) {
		
		if (contact.get(name) == null) {
			System.out.println("error");
		} else {
			contact.remove(name);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Contact c = new Contact();
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			String userInput = sc.nextLine();
			String[] inputs = userInput.split(" ");
			
			// inputs[0] => method
			// inputs[1] => name
			// inputs[2] => phoneNumber
			
			if (inputs[0].equals("show")) {
				// call show()
				c.show();
			} else if (inputs[0].equals("add")) {
				try {
					// call add()	
					c.add(inputs[1], inputs[2]);	
				} catch(Exception e) {
					// ArrayIndexOutOfBoundsException
					System.out.println("error");
				}
			} else if (inputs[0].equals("delete")) {
				// call delete()
				c.delete(inputs[1]);
			} else if (inputs[0].equals("find")) {
				// call find()
				System.out.println(c.find(inputs[1])); 
			} else {
				break;
			}
		}
		
	}

}
