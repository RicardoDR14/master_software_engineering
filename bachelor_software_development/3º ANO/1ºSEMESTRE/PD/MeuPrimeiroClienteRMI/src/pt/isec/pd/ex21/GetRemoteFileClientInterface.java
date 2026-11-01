package pt.isec.pd.ex21;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Jose'
 */


public interface GetRemoteFileClientInterface extends Remote {
    void writeFileChunk(byte [] fileChunk, int nbytes) throws RemoteException, java.io.IOException;
}