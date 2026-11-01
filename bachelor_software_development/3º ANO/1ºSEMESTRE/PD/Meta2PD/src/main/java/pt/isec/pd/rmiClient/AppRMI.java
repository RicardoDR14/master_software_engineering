package pt.isec.pd.rmiClient;

import java.rmi.RemoteException;
import java.util.Scanner;

public class AppRMI {
    private final RMIClient cliente;
    private AsyncServerListener listener;

    public AppRMI(RMIClient cliente) {
        this.cliente = cliente;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n\n===== Menu RMI =====");
            System.out.println("1. Listar Utilizadores");
            System.out.println("2. Listar Grupos");
            System.out.println("3. Inscrever-se em Notificações");
            System.out.println("4. Cancelar Inscrição em Notificações");
            System.out.println("5. Notificar Registro de Utilizador");
            System.out.println("6. Notificar Autenticação de Utilizador");
            System.out.println("7. Notificar Inserção de Despesa");
            System.out.println("8. Notificar Exclusão de Despesa");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consome a linha em branco

            try {
                switch (opcao) {
                    case 1 -> listarUtilizadores();
                    case 2 -> listarGrupos();
                    case 3 -> registrarListener();
                    case 4 -> desconectarListener();
                    case 5 -> notificarRegistroUtilizador(scanner);
                    case 6 -> notificarAutenticacaoUtilizador(scanner);
                    case 7 -> notificarInsercaoDespesa(scanner);
                    case 8 -> notificarExclusaoDespesa(scanner);
                    case 0 -> {
                        System.out.println("Saindo...");
                        desconectarListener();
                        System.exit(0);
                    }
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (RemoteException e) {
                System.err.println("Erro ao se comunicar com o servidor: " + e.getMessage());
            }
        }
    }

    private void listarUtilizadores() throws RemoteException {
        System.out.println("Utilizadores:");
        cliente.listarUtilizadores().forEach(usuario -> System.out.println("- " + usuario));
    }

    private void listarGrupos() throws RemoteException {
        System.out.println("Grupos:");
        cliente.listarGrupos().forEach(grupo -> System.out.println("- " + grupo));
    }

    private void registrarListener() throws RemoteException {
        if (listener == null) {
            listener = new AsyncServerListener();
            cliente.registrarListener(listener);
            System.out.println("Listener registrado com sucesso!");
        } else {
            System.out.println("Listener já está registrado.");
        }
    }

    private void desconectarListener() throws RemoteException {
        if (listener != null) {
            cliente.desconectar(listener);
            System.out.println("Listener desconectado com sucesso!");
        } else {
            System.out.println("Listener já está desinscrito.");
        }
    }

    private void notificarRegistroUtilizador(Scanner scanner) throws RemoteException {
        System.out.print("Digite o email do utilizador para registro: ");
        String email = scanner.nextLine();
        cliente.notificarRegistroUtilizador(email);
        System.out.println("Notificado o registro do utilizador: " + email);
    }

    private void notificarAutenticacaoUtilizador(Scanner scanner) throws RemoteException {
        System.out.print("Digite o email do utilizador para autenticação: ");
        String email = scanner.nextLine();
        cliente.notificarAutenticacaoUtilizador(email);
        System.out.println("Notificado a autenticação do utilizador: " + email);
    }

    private void notificarInsercaoDespesa(Scanner scanner) throws RemoteException {
        System.out.print("Digite a descrição da despesa: ");
        String descricao = scanner.nextLine();
        System.out.print("Digite o grupo da despesa: ");
        String grupo = scanner.nextLine();
        cliente.notificarInsercaoDespesa(descricao, grupo);
        System.out.println("Notificada a inserção da despesa: " + descricao + " no grupo: " + grupo);
    }

    private void notificarExclusaoDespesa(Scanner scanner) throws RemoteException {
        System.out.print("Digite a descrição da despesa para exclusão: ");
        String descricao = scanner.nextLine();
        System.out.print("Digite o grupo da despesa: ");
        String grupo = scanner.nextLine();
        cliente.notificarExclusaoDespesa(descricao, grupo);
        System.out.println("Notificada a exclusão da despesa: " + descricao + " no grupo: " + grupo);
    }
}
