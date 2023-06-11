import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPServer {

    public void start() throws IOException {
        DatagramSocket socket = new DatagramSocket(8000);
        DatagramPacket inPacket, outPacket;

        byte[] inMsg = new byte[10];
        byte[] outMsg;

        while (true) {
            inPacket = new DatagramPacket(inMsg, inMsg.length);
            socket.receive(inPacket);

            InetAddress address = inPacket.getAddress();
            int port = inPacket.getPort();

            SimpleDateFormat sdfDate = new SimpleDateFormat("[hh:mm:ss]");
            String time = sdfDate.format(new Date());
            outMsg = time.getBytes();

            outPacket = new DatagramPacket(outMsg, outMsg.length, address, port);
            socket.send(outPacket);
        }
    }

    public static void main(String args[]) {
        try {
            new UDPServer().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
