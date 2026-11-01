/*
 * Esta classe tambem implementa uma interface remota (GetRemoteFileClientInterface)
 * que deve incluir o metodo:
 *
 *       void writeFileChunk(byte [] fileChunk, int nbytes) throws java.io.IOException
 *
 */

package pt.isec.pd.ex21;

import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Jose'
 */
public class GetRemoteFileClientService extends UnicastRemoteObject implements GetRemoteFileClientInterface, Remote{

    FileOutputStream fout = null;

    protected GetRemoteFileClientService() throws RemoteException {}


    public synchronized void setFout(FileOutputStream fout) {
        this.fout = fout;
    }

    public synchronized void writeFileChunk(byte[] fileChunk, int nbytes) throws IOException {
        if (fout == null) {
            throw new IOException("Nao foi possivel escrever o ficheiro!");
        }
        fout.write(fileChunk, 0, nbytes);
    }

}
