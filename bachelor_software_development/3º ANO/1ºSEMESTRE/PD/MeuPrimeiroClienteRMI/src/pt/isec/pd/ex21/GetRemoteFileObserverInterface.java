package pt.isec.pd.ex21;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GetRemoteFileObserverInterface extends Remote {
    public void notifyNewOperationConcluded(String description) throws RemoteException;
}
