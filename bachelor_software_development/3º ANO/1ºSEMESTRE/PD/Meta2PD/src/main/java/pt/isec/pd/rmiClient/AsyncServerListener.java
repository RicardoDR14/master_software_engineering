package pt.isec.pd.rmiClient;

import pt.isec.pd.server.RMI.ClientNotificationInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AsyncServerListener extends UnicastRemoteObject implements ClientNotificationInterface {

    public AsyncServerListener() throws RemoteException {
        super();
    }

    @Override
    public void notificarEvento(String message) throws RemoteException {
        System.out.println("\nNotificacao: " + message);
    }
}
