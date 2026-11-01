package pt.isec.pd.ex21;

/**
 * @author Jose'
 */
public interface GetRemoteFileServiceInterface extends java.rmi.Remote {
    byte[] getFileChunk(String fileName, long offset) throws java.rmi.RemoteException, java.io.IOException;
    void getFile(String fileName, GetRemoteFileClientInterface cliRef) throws java.io.IOException;

    void addObserver(GetRemoteFileObserverInterface observer) throws java.rmi.RemoteException;
    void removeObserver(GetRemoteFileObserverInterface observer) throws java.rmi.RemoteException;

}
