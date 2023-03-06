import java.util.ArrayList;
import java.util.Scanner;

public class NumberConverter {

    public static void converter(int userInput, int number, String notice) {
        ArrayList<Integer> list = new ArrayList<Integer>();

        while (userInput > 0) {
            int element = userInput % number;
            if (element >= 10) {
                element += 87;
            }
            list.add(element);
            userInput /= number;
        }

        System.out.print(notice);
        for (int i=list.size()-1; i>=0; i--) {
            int j = list.get(i);
            if (j>=97) {
                System.out.print((char)j);
            } else {
                System.out.print(j);
            }
        }
        System.out.println();
    }
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        int userInput = scan.nextInt();
        scan.close();
        converter(userInput, 2, "b ");
        converter(userInput, 8, "o ");
        converter(userInput, 16, "h ");
    }
}