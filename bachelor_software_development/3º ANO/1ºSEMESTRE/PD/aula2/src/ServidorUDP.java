import java.net.*;
import java.io.*;
public class ServidorUDP
{
    // UDP PORT TO WHICH SERVICE IS BOUND
    public static final int SERVICE_PORT = 7000;
    // MAX SIZE OF PACKET, LARGE ENOUGH FOR ALMOST ANY CLIENT
    public static final int BUFSIZE = 4096;
    // SOCKET USED FOR READING AND WRITING UDP PACKETS
    private DatagramSocket socket = null;

    public ServidorUDP() //constructor
    {
        try
        {
// BIND TO THE SPECIFIED UDP PORT
            socket = new DatagramSocket( SERVICE_PORT );
            System.out.println("Server active on port "+socket.getLocalPort());
        }catch (Exception e){
            System.err.println ("Unable to bind port");
        }
    }
    public void serviceClients()
    {
        if(socket == null) return;
// CREATE A BUFFER LARGE ENOUGH FOR INCOMING PACKETS
        byte[] buffer = new byte[BUFSIZE];
        while(true){
            try {
// CREATE A DATAGRAMPACKET FOR READING UDP PACKETS
                DatagramPacket packet = new DatagramPacket( buffer, BUFSIZE );
// RECEIVE INCOMING PACKETS
                socket.receive(packet);
                System.out.println("Packet received from " + packet.getAddress()
                        + ":" + packet.getPort() + " of length "
                        + packet.getLength());
                String msg = new String(packet.getData(), 0,
                        packet.getLength());
                System.out.println(msg);


                String message = "TIME";
                byte[] sendbuf = message.getBytes();
                DatagramPacket volta = new DatagramPacket(sendbuf,
                        sendbuf.length, packet.getAddress(),
                        SERVICE_PORT);
                socket.send(packet);
            }catch (IOException e){
                System.err.println ("Error : " + e);
            }
        } // while
    } // serviceClientes() method
    public static void main(String args[])
    {
        ServidorUDP server = new ServidorUDP();
        server.serviceClients();
    }
}


