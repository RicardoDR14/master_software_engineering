package ex9.tcp;
import pt.isec.pd.aula4.Time;

import java.net.*;
import java.io.*;
import java.util.*;

public class TcpSerializedTimeClientIncomplete {

    public static final int MAX_SIZE = 4000;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10; //segundos

    public static void main(String[] args) throws IOException {

        //Calendar response;
        Time response;
        
        if(args.length != 2){
            System.out.println("Sintaxe: java TcpSerializedTimeClientIncomplete serverAddress serverUdpPort");
            return;
        }

        try(Socket socket = new Socket(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
            ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream())){

            socket.setSoTimeout(TIMEOUT*1000);

            //Serializa a string TIME_REQUEST para o OutputStream associado a socket
            oout.writeObject(TIME_REQUEST);
            oout.flush();

            //Deserializa a resposta recebida em socket
            //response = (Calendar) oin.readObject();
            response = (Time) oin.readObject();
            
            if(response == null){
                System.out.println("O servidor nao enviou qualquer respota antes de"
                        + " fechar aligacao TCP!");   
            }else{                
                System.out.println("Hora indicada pelo servidor: " + response.getHour());
            }
            
        }catch(Exception e){
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
        }
   }
  
}
