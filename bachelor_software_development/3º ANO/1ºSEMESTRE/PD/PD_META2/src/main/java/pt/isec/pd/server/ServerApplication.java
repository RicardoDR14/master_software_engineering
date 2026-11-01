package pt.isec.pd.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pt.isec.pd.server.Database.ManagerDB;

@SpringBootApplication
public class ServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
		ManagerDB managerDB = new ManagerDB();
		managerDB.initializeDatabase();
	}
}
