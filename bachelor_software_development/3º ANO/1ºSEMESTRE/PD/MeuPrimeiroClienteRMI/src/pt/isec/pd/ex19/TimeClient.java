package pt.isec.pd.ex19;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class TimeClient {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TimeClient <host>");
            return;
        }

        String host = args[0];
        try {
            // Obtém o registro RMI no host especificado
            Registry registry = LocateRegistry.getRegistry(host, 1099);

            // Procura o serviço remoto registrado como "timeserver"
            RemoteTimeInterface timeService = (RemoteTimeInterface) registry.lookup("timeserver");

            // Chama o método remoto para obter a hora atual
            Hora horaAtual = timeService.getHora();

            // Exibe a hora no formato definido
            System.out.println("Hora atual: " + horaAtual);

            timeService.putMsg("Olá!!!!");
            timeService.putMsg("Do Ricardo!");

            System.out.println("Última mensagem:");
            String last = timeService.getLastMsg();
            System.out.println(last);

            System.out.println("Mensagens:");
            List<String> msgs = timeService.getMsgs();
            for (String m : msgs)
                System.out.println(m);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



