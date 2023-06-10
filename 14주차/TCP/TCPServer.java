import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPServer {
    public static void main(String args[]) {
        ServerSocket ss;
        Scanner sc = new Scanner(System.in);

        System.out.println("Server Start!");

        try {
            ss = new ServerSocket(8000);
            Socket soc = ss.accept();
            OutputStream out = soc.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            while (true) {
                dos.writeUTF(sc.nextLine());
            } 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
