package pt.isec.pd.ex21;

import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;

/**
 *
 * @author Jose'
 */
public class GetRemoteFileObserver extends UnicastRemoteObject implements GetRemoteFileObserverInterface {

    public GetRemoteFileObserver() throws RemoteException {}

    @Override
    public void notifyNewOperationConcluded(String description) throws RemoteException {
        System.out.println("-> " + description);
        System.out.println();
    }

    public static void main(String[] args) {
        try{

            /*
             * Se existirem varias interfaces de rede activas na maquina onde corre esta aplicacao RMI,
             * convem definir de forma explicita o endereco que deve ser incluido na referencia remota do servico
             * RMI criado. Para o efeito, o endereco deve ser atribuido 'a propriedade java.rmi.server.hostname.
             *
             * Pode ser no codigo atraves do metodo System.setProperty():
             *      - System.setProperty("java.rmi.server.hostname", "10.65.129.232"); //O endereco usado e' apenas um exemplo
             *      - System.setProperty("java.rmi.server.hostname", args[1]); //Neste caso, assume-se que o endereco e' passado como segundo argumento na linha de comando
             *
             * Tambem pode ser como opcao passada 'a maquina virtual Java:
             *      - java -Djava.rmi.server.hostname=10.202.128.22 GetRemoteFileObserver ... //O endereco usado e' apenas um exemplo
             *      - No Netbeans: Properties -> Run -> VM Options -> -Djava.rmi.server.hostname=10.202.128.22 //O endereco usado e' apenas um exemplo
             */

            if(args.length < 2){
                System.out.println("Deve passar 2 argumentos na linha de comando:");
                System.out.println("    1 - endereco do RMI registry onde esta' registado o servico remoto de download de ficheiros");
                System.out.println("    2 - endereco IP da interface de rede que deve ser incluido na referencia do servico RMI local");
                System.exit(1);
            }
            //Localiza o servico remoto nomeado "GetRemoteFile"
            String objectUrl = "rmi://"+args[0]+"/servidor-ficheiros-pd";
            GetRemoteFileServiceInterface getRemoteFileService = (GetRemoteFileServiceInterface)Naming.lookup(objectUrl);

            System.setProperty("java.rmi.server.hostname", args[1]); // 2º argumento da linha de comando
            //Cria e lanca o servico
            GetRemoteFileObserver observer = new GetRemoteFileObserver();
            System.out.println("Servico GetRemoteFileObserver criado e em execucao...");

            //adiciona observador no servico remoto
            getRemoteFileService.addObserver(observer);

            System.out.println("<Enter> para terminar...");
            System.out.println();
            System.in.read();

            getRemoteFileService.removeObserver(observer);
            UnicastRemoteObject.unexportObject(observer, true);

        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        }catch(IOException | NotBoundException e){
            System.out.println("Erro - " + e);
            System.exit(1);
        }
    }
}
