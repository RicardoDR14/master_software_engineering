package pt.isec.pd.server.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientNotificationInterface extends Remote {
    void notificarEvento(String message) throws RemoteException;
}

