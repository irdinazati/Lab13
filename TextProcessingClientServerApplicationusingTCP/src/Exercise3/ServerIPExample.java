package Exercise3;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerIPExample {
    public static void main(String[] args) {
        try {
            InetAddress serverAddress = InetAddress.getLocalHost();
            String serverIP = serverAddress.getHostAddress();
            System.out.println("Server IP Address: " + serverIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}