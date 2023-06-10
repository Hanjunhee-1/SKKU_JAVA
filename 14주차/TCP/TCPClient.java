import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

public class TCPClient {
    static final String ServerIP = "localhost";
    public static void main(String args[]) {
        Socket soc;

        try {
            soc = new Socket(ServerIP, 8000);
            InputStream in = soc.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            while (true) {
                System.out.println(dis.readUTF());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
