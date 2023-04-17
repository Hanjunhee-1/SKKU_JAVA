import java.util.Scanner;

public class SimpleCalculator {

	public static void main(String[] args) throws AddZeroException, SubstractZeroException, OutOfRangeException {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        String[] tokens = input.split("(?<=[-+*/])|(?=[-+*/])"); // 정규식을 이용한 분리
        
        // tokens[0] = num1
        // tokens[1] = operand
        // tokens[2] = num2

        int num1 = Integer.parseInt(tokens[0]);
        int num2 = Integer.parseInt(tokens[2]);
        int result = 0;
        
        try {
        	
        	if ((num1 == 0 || num2 == 0) && tokens[1].equals("+")) {
        		throw new AddZeroException("AddZeroException");
        	}
        	
        	if ((num1 == 0 || num2 == 0) && tokens[1].equals("-")) {
        		throw new SubstractZeroException("SubstractZeroException");
        	}
        	
        	if (num1 > 1000 || num2 > 1000) {
        		throw new OutOfRangeException("OutOfRangeException");
        	}
        	
        	if (tokens[1].equals("+")) {
        		result = num1 + num2;
        	} else if (tokens[1].equals("-")) {
        		result = num1 - num2;
        	}
        	
        	if (result < 0 || result > 1000) {
        		throw new OutOfRangeException("OutOfRangeException");
        	}
        	
        	System.out.println(result);
        } catch (AddZeroException | SubstractZeroException | OutOfRangeException e) {
        	System.out.println(e.getMessage());
        } finally {
        	scanner.close();
        }
	}
}

class AddZeroException extends Exception {
	public AddZeroException(String message) {
		super(message);
	}
}

class SubstractZeroException extends Exception {
	public SubstractZeroException(String message) {
		super(message);
	}
}

class OutOfRangeException extends Exception {
	public OutOfRangeException(String message) {
		super(message);
	}
}