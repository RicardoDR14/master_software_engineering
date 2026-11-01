package pt.isec.pd.ex19;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteTimeInterface extends Remote {
    Hora getHora() throws RemoteException;
    public void putMsg(String msg)throws RemoteException;
    public String getLastMsg() throws RemoteException;
    public List<String> getMsgs() throws RemoteException;
}

