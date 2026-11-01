package pt.isec.pd.httpClient;

import pt.isec.pd.rmiClient.RMIClient;

import java.rmi.RemoteException;
import java.util.Scanner;

public class AppHttp {
    private final HttpClient httpClient = new HttpClient();

    public void start() throws RemoteException {

        String userEmail = "";
        boolean isLoggedIn = false;
        Scanner scanner = new Scanner(System.in);

        while (!isLoggedIn) {
            System.out.println("\n=== Aplicação Cliente HTTP ===");
            System.out.println("\n1. Registar");
            System.out.println("2. Login");
            System.out.println("3. Sair");
            System.out.print("Digite o comando: ");
            int command;
            try {
                command = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um número");
                continue;
            }
            switch (command) {
                case 1 -> {
                    System.out.print("Digite o email: ");
                    String email = scanner.nextLine().trim();

                    System.out.print("Digite o password: ");
                    String password = scanner.nextLine().trim();

                    System.out.print("Digite o telefone: ");
                    String telefone = scanner.nextLine().trim();

                    isLoggedIn = httpClient.register(email, password, telefone);
                    if (isLoggedIn) {
                        System.out.println("Registado com sucesso!\n");
                        userEmail = email;
                        RMIClient.notificarAutenticacaoUtilizador(userEmail);
                    }
                }
                case 2 -> {
                    System.out.print("Digite o email: ");
                    String email = scanner.nextLine().trim();

                    System.out.print("Digite o password: ");
                    String password = scanner.nextLine().trim();

                    isLoggedIn = httpClient.login(email, password);
                    if (isLoggedIn) {
                        System.out.println("Logado com sucesso!\n");
                        userEmail = email;
                    }
                }
                case 3 -> {
                    System.out.println("Sair...");
                    return;
                }
                default -> System.out.println("Número invalido, tente novamente\n");
            }
        }
        while (isLoggedIn) {
            System.out.println("\n=== Aplicação Cliente HTTP ===");
            System.out.println("\n1. Entrar num grupo");
            System.out.println("2. Listar grupos");
            System.out.println("3. Adicionar uma despesa");
            System.out.println("4. Listar despesas");
            System.out.println("5. Eliminar uma despesa");
            System.out.println("6. Sair");
            System.out.print("Digite o comando: ");
            int command;
            try {
                command = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um número");
                continue;
            }
            switch (command) {
                case 1 -> httpClient.enterGroup(scanner);
                case 2 -> httpClient.listGroups();
                case 3 -> httpClient.addExpense(scanner, userEmail);
                case 4 -> httpClient.getExpenses(scanner);
                case 5 -> httpClient.deleteExpense(scanner);
                case 6 -> {
                    System.out.println("Sair...");
                    return;
                }
                default -> System.out.println("Número invalido, tente novamente\n");
            }
        }
    }
}
