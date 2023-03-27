
public class Animal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String name = "Arcs";
		float weight = (float)8.5565;
		String nameSlave = "SKKU";
		
		Cat cat = new Cat(name, weight, nameSlave);
		cat.getName();
		cat.setName("Arcs2");
		cat.getName();
		cat.getWeight();
		cat.setWeight((float)8.556);
		cat.getWeight();
		cat.getNameSlave();
		cat.setNameSlave("SNU");
		cat.getNameSlave();
		cat.bark();
		cat.getFood();
		
	}

}

abstract class Mammal extends Animal {
	
	abstract void bark();
	
	abstract void getFood();
}


final class Cat extends Mammal {
	// TODO
	
	String name = new String();
	String nameSlave = new String();
	float weight;
	
	Cat(String name, float weight, String nameSlave) {
		this.name = name;
		this.weight = weight;
		this.nameSlave = nameSlave;
	}
	
	void getName() {
		System.out.println("Name: " + this.name);
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	void getWeight() {
		System.out.print("Weight: ");
		System.out.println(this.weight);
	}
	
	void setWeight(float weight) {
		this.weight = weight;
	}
	
	void getNameSlave() {
		System.out.println("NameSlave: " + this.nameSlave);
	}
	
	void setNameSlave(String nameSlave) {
		this.nameSlave = nameSlave;
	}
	
	
	void bark() {
		System.out.println("Meow");
	}
	
	void getFood() {
		System.out.println("Fish");
	}
}

final class Dog extends Mammal {
	// TODO
	
	String name = new String();
	String nameMaster = new String();
	float weight;

	Dog(String name, float weight, String nameMaster) {
		this.name = name;
		this.weight = weight;
		this.nameMaster = nameMaster;
	}
	
	void getName() {
		System.out.println("Name: " + this.name);
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	void getWeight() {
		System.out.print("Weight: ");
		System.out.println(this.weight);
	}
	
	void setWeight(float weight) {
		this.weight = weight;
	}
	
	void getNameMaster() {
		System.out.println("NameMaster: " + this.nameMaster);
	}
	
	void setNameMaster(String nameMaster) {
		this.nameMaster = nameMaster;
	}
	
	void bark() {
		System.out.println("Bowbow");
	}
	
	void getFood() {
		System.out.println("Apple");
	}
}

abstract class Reptile extends Animal {
	
	abstract void getFood();
}

final class Crocodile extends Reptile {
	// TODO
	
	String name = new String();
	float weight;
	
	Crocodile(String name, float weight) {
		this.name = name;
		this.weight = weight;
	}
	
	void getName() {
		System.out.println("Name: " + this.name);
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	void getWeight() {
		System.out.print("Weight: ");
		System.out.println(this.weight);
	}
	
	void setWeight(float weight) {
		this.weight = weight;
	}
	
	void getFood() {
		System.out.println("Meat");
	}
}