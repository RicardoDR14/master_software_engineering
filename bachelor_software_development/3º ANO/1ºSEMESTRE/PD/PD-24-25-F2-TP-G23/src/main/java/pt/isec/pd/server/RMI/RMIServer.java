package pt.isec.pd.server.RMI;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@Component
public class RMIServer {

    @Autowired
    private RMIControllers rmiControllers;

    @PostConstruct
    public void start() {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("RemoteService", rmiControllers);
            System.out.println("Servidor RMI está em execução...");
        } catch (RemoteException e) {
            System.err.println("Falha ao iniciar o servidor RMI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
