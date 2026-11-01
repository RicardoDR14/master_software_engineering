import java.net.*;
import java.io.*;

public class ClienteUDP {
    // UDP PORT TO WHICH SERVICE IS BOUND
    public static final int SERVICE_PORT = 7000;
    // MAX SIZE OF PACKET
    public static final int BUFSIZE = 256;

    public static void main(String args[]) {
        /*if (args.length != 1) {
            System.err.println("Syntax - java EchoClient hostname");
            return;
        }
        String hostname = args[0];*/
// GET AN INETADDRESS FOR THE SPECIFIED HOSTNAME
        InetAddress addr = null;
        try {
// RESOLVE THE HOSTNAME TO AN INETADDR
            addr = InetAddress.getByName("10.65.129.56");//pra mim
        } catch (UnknownHostException e) {
            System.err.println("Unable to resolve host");
            return;
        }
        try {
// BIND TO ANY FREE PORT
            DatagramSocket socket = new DatagramSocket();
// SET A TIMEOUT VALUE OF TWO SECONDS
            socket.setSoTimeout(30 * 1000);
            for (int i = 1; i <= 10; i++) {
// COPY SOME DATA TO OUR BYTE ARRAY
                String message = "TIME " + i;
                byte[] sendbuf = message.getBytes();
// CREATE A PACKET TO SEND TO THE UDP SERVER
                DatagramPacket sendPacket = new DatagramPacket(sendbuf,
                        sendbuf.length, addr,
                        SERVICE_PORT);
                System.out.println("Sending packet to " + "10.65.129.56");
// SEND THE PACKET
                socket.send(sendPacket);
                System.out.print("Waiting for packet.... ");
// CREATE A SMALL PACKET FOR RECEIVING UDP PACKETS
                byte[] recbuf = new byte[BUFSIZE];
                DatagramPacket receivePacket = new DatagramPacket(recbuf, BUFSIZE);
// DECLARE A TIMEOUT FLAG
                boolean timeout = false;
// CATCH ANY INTERRUPTEDIOEXCEPTION THAT IS THROWN WHILE WAITING A UDP PKT
                try {
                    socket.receive(receivePacket);
                } catch (InterruptedIOException e) {
                    timeout = true;
                }
                if (!timeout) {
                    System.out.println("Packet received!");
                    System.out.println("Details : " + receivePacket.getAddress());
// CREATE AND DISPLAY A STRING BASED ON PACKET CONTENT
                    String msg = new String(receivePacket.getData(), 0,
                            receivePacket.getLength());
                    System.out.println(msg);
                } else { // TIMEOUT HAS OCCURED
                    System.out.println("packet lost!");
                }
// SLEEP FOR A SECOND, TO ALLOW USER TO SEE PACKET
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            } // for
        } catch (IOException e) {
            System.err.println("Socket error " + e);
        }
    } // main
}