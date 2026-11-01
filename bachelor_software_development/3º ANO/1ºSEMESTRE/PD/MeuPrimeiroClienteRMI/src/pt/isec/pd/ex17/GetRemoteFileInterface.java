package pt.isec.pd.ex17;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GetRemoteFileInterface extends Remote {
    byte[] getFileChunk(String fileName, long offset) throws RemoteException;
}