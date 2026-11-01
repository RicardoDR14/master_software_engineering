package pt.isec.pd.ex19;

import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemoteTimeService extends UnicastRemoteObject implements RemoteTimeInterface {

    protected List<String> msgs = new ArrayList<>();

    protected RemoteTimeService() throws RemoteException {
        super();
    }

    @Override
    public synchronized Hora getHora() throws RemoteException {
        LocalTime now = LocalTime.now();
        Hora atual = new Hora(now.getHour(), now.getMinute(), now.getSecond());

        try {
            // Obtém o endereço do cliente que fez o pedido
            String clientHost = getClientHost();
            System.out.println("Pedido recebido de: " + clientHost);
        } catch (Exception e) {
            System.err.println("Não foi possível identificar o cliente.");
        }
        return atual;
    }

    @Override
    public synchronized void putMsg(String msg) throws RemoteException {
        if (msg != null)
            try {
                msgs.add("("+ getClientHost()+") : "+msg);
            } catch (ServerNotActiveException ex){
                msgs.add(msg);
            }
        System.out.println(msg);
    }

    @Override
    public synchronized String getLastMsg() throws RemoteException {
        if (msgs.isEmpty()) return null;

        return msgs.getLast();
    }

    @Override
    public synchronized List<String> getMsgs() throws RemoteException {
        return msgs;
    }
}

