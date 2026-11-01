package pt.isec.pd.server.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteServiceInterface extends Remote {
    // Notificações de eventos
    void registrarNotificacao(String mensagem) throws RemoteException;

    // Listagem de utilizadores e grupos
    List<String> listarUtilizadores() throws RemoteException;
    List<String> listarGrupos() throws RemoteException;

    // Inscrição e cancelamento de notificações
    void inscrever(ClientNotificationInterface cliente) throws RemoteException;
    void cancelarInscricao(ClientNotificationInterface cliente) throws RemoteException;

    // Métodos para eventos específicos
    void notificarRegistroUtilizador(String email) throws RemoteException;
    void notificarAutenticacaoUtilizador(String email) throws RemoteException;
    void notificarInsercaoDespesa(String descricao, String grupo) throws RemoteException;
    void notificarExclusaoDespesa(int id, String grupo) throws RemoteException;
}
