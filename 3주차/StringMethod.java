public class StringMethod {

    static String addString(String s1, int index, String s2) {
        String firstString = new String();
        String secondString = new String();
        String resultString = new String();
        
        firstString = s1.substring(0, index+1);
        secondString = s1.substring(index+1);
        resultString = firstString + s2 + secondString;

        return resultString;
    }

    static String reverse(String s) {
        String resultString = new String();

        for (int i=s.length()-1; i>=0; i--) {
            resultString += s.charAt(i);
        }
        
        return resultString;
    }

    static String removeString(String s1, String s2) {
        String resultString = new String();
        int index = s1.indexOf(s2);

        while (index != -1) {
            s1 = s1.substring(0, index) + s1.substring(index + s2.length());
            index = s1.indexOf(s2);
        }
        resultString = s1;
        return resultString;
    }

    public static void main(String args[]) {
        System.out.println(addString("0123456", 3, "-"));
        System.out.println(reverse("abc"));
        System.out.println(removeString("01001000", "00"));
    }
}