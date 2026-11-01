package pt.isec.pd.ex19;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TimeServer {
    public static void main(String[] args) {
        try {
            // Cria a instância do serviço remoto
            RemoteTimeService timeService = new RemoteTimeService();

            // Inicializa o RMI Registry local na porta 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // Registra o serviço remoto no RMI Registry com o nome "timeserver"
            registry.rebind("timeserver", timeService);

            System.out.println("TimeServer está em execução...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
