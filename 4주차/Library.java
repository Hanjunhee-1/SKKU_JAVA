import java.util.ArrayList;
public class Library {
	
	// main() 함수에서 선언하는 것이 아닌 class 에서 선언
	static ArrayList<Book> books = new ArrayList<>();
	
	public static void main(String[] args) {
		Library library = new Library();
		
		// 0 번째 책 대출
		library.Borrow(0);
		
		// 1 번째 책 대출
		library.Borrow(1);
		
		// 0 번째 책 반납
		library.Return(0);
		
		// 1 번째 책 다시 대출 시도 --> error print 
		library.Borrow(1);
		
		// 1 번째 책 반납
		library.Return(1);
		
		// 1 번째 책 다시 반납 시도 --> error print
		library.Return(1);
		
		/** 
		 * 예상 출력 결과
		 * 
		 * You borrowed the book 0.
		 * You borrowed the book 1.
		 * Book 0 has been returned.
		 * Book 1 can not be borrowed.
		 * Book 1 has been returned.
		 * Book 1 has already been returned.
		*/
	}
	
	// Library class 의 생성자
	public Library() {
		// 미리 선언되어있던 것에 책 정보들 추가
		for (int i=0; i<10; i++) {
			Book book = new Book("Returned");
			books.add(book);
		}
	}
	
	void Borrow(int n) {
		if (books.get(n).States.equals("Borrowed")) {
			System.out.println("Book " + Integer.toString(n) + " can not be borrowed.");
		} 
		
		else if (books.get(n).States.equals("Returned")) {
			// 책을 빌린 것이니 상태를 "Borrowed" 로 변경.
			books.get(n).States = "Borrowed";
			System.out.println("You borrowed the book " + Integer.toString(n) + ".");
		}
	}
	
	void Return(int n) {
		if (books.get(n).States.equals("Borrowed")) {
			// 책을 반납한 것이니 상태를 "Returned" 로 변경.
			books.get(n).States = "Returned";
			System.out.println("Book " + Integer.toString(n) + " has been returned.");
		}
		
		else if (books.get(n).States.equals("Returned")) {
			System.out.println("Book " + Integer.toString(n) + " has already been returned.");
		}
	}

}

class Book {
	// 책의 상태 저장
	String States = new String();
	
	// 기본 생성자
	public Book() {}
	
	public Book(String States) {
		this.States = States;
	}
}