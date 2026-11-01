package pt.isec.pd.rmiClient;

import pt.isec.pd.server.RMI.ClientNotificationInterface;
import pt.isec.pd.server.RMI.RemoteServiceInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class RMIClient {
    private static RemoteServiceInterface servidor = null;

    public RMIClient(String urlServidor) throws Exception {
        this.servidor = (RemoteServiceInterface) Naming.lookup(urlServidor);
    }

    public List<String> listarUtilizadores() throws RemoteException {
        return servidor.listarUtilizadores();
    }

    public List<String> listarGrupos() throws RemoteException {
        return servidor.listarGrupos();
    }

    // Registra o listener para receber notificações do servidor
    public void registrarListener(ClientNotificationInterface listener) throws RemoteException {
        servidor.inscrever(listener);
    }

    // Cancela a inscrição do listener
    public void desconectar(ClientNotificationInterface listener) throws RemoteException {
        servidor.cancelarInscricao(listener);
    }

    // Novos métodos para interagir com notificações específicas
    public void notificarRegistroUtilizador(String email) throws RemoteException {
        servidor.notificarRegistroUtilizador(email);
    }

    public static void notificarAutenticacaoUtilizador(String email) throws RemoteException {
        servidor.notificarAutenticacaoUtilizador(email);
    }

    public void notificarInsercaoDespesa(String descricao, String grupo) throws RemoteException {
        servidor.notificarInsercaoDespesa(descricao, grupo);
    }

    public void notificarExclusaoDespesa(String descricao, String grupo) throws RemoteException {
        servidor.notificarExclusaoDespesa(descricao, grupo);
    }
}
