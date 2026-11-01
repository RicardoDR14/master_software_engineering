package pt.isec.pd.rmiClient;

public class Main {
    public static void main(String[] args) {
        try {
            String serverUrl = "rmi://localhost:1099/RemoteService";

            RMIClient client = new RMIClient(serverUrl);

            AppRMI app = new AppRMI(client);
            app.start();
        } catch (Exception e) {
            System.err.println("Failed to start the client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}