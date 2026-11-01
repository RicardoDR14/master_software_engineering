package pt.isec.pd.httpClient;

import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) throws RemoteException {
        AppHttp appHttp = new AppHttp();
        appHttp.start();
    }
}