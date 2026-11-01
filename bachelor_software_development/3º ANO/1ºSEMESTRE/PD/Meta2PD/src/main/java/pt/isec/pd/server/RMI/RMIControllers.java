package pt.isec.pd.server.RMI;

import org.springframework.stereotype.Component;
import pt.isec.pd.server.Data.Grupo;
import pt.isec.pd.server.Data.Utilizador;
import pt.isec.pd.server.Database.ManagerDB;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

@Component
public class RMIControllers extends UnicastRemoteObject implements RemoteServiceInterface {

    private final ManagerDB dbManager;
    private final List<ClientNotificationInterface> clientes;

    public RMIControllers(ManagerDB dbManager) throws RemoteException {
        this.dbManager = dbManager;
        this.clientes = new ArrayList<>();
    }

    @Override
    public void registrarNotificacao(String mensagem) throws RemoteException {
        notificarClientes(mensagem);
    }

    @Override
    public void notificarRegistroUtilizador(String email) throws RemoteException {
        String mensagem = "Novo utilizador registrado: " + email;
        notificarClientes(mensagem);
    }

    @Override
    public void notificarAutenticacaoUtilizador(String email) throws RemoteException {
        String mensagem = "Utilizador autenticado: " + email;
        notificarClientes(mensagem);
    }

    @Override
    public void notificarInsercaoDespesa(String descricao, String grupo) throws RemoteException {
        String mensagem = "Despesa inserida no grupo '" + grupo + "': " + descricao;
        notificarClientes(mensagem);
    }

    @Override
    public void notificarExclusaoDespesa(String descricao, String grupo) throws RemoteException {
        String mensagem = "Despesa removida do grupo '" + grupo + "': " + descricao;
        notificarClientes(mensagem);
    }

    public void notificarClientes(String mensagem) {
        synchronized (clientes) {
            for (ClientNotificationInterface cliente : clientes) {
                try {
                    cliente.notificarEvento(mensagem);
                } catch (RemoteException e) {
                    System.err.println("Falha ao notificar cliente: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public List<String> listarUtilizadores() throws RemoteException {
        List<Utilizador> utilizadores = dbManager.obterTodosUtilizadores();
        List<String> usuariosStrings = new ArrayList<>();
        for (Utilizador utilizador : utilizadores) {
            usuariosStrings.add(utilizador.getEmail());
        }
        return usuariosStrings;
    }

    @Override
    public List<String> listarGrupos() throws RemoteException {
        List<Grupo> grupos = dbManager.obterTodosGrupos();
        List<String> gruposStrings = new ArrayList<>();
        for (Grupo grupo : grupos) {
            gruposStrings.add(grupo.getNomeGrupo());
        }
        return gruposStrings;
    }

    @Override
    public void inscrever(ClientNotificationInterface cliente) throws RemoteException {
        synchronized (clientes) {
            if (!clientes.contains(cliente)) {
                clientes.add(cliente);
                System.out.println("Novo cliente inscrito.");
            }
        }
    }

    @Override
    public void cancelarInscricao(ClientNotificationInterface cliente) throws RemoteException {
        synchronized (clientes) {
            clientes.remove(cliente);
            System.out.println("Cliente removido.");
        }
    }
}
