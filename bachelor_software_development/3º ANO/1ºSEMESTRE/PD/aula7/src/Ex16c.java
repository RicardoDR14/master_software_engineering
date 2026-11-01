import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

// Classe para armazenar as informações do personagem
class Character {
    private int index;
    private String fullName;
    private String birthdate;
    private String hogwartsHouse;
    private String interpretedBy;
    private String image;
    private String[] children;

    // Construtor
    public Character(int index, String fullName, String birthdate, String hogwartsHouse, String interpretedBy, String image, String[] children) {
        this.index = index;
        this.fullName = fullName;
        this.birthdate = birthdate;
        this.hogwartsHouse = hogwartsHouse;
        this.interpretedBy = interpretedBy;
        this.image = image;
        this.children = children;
    }

    // Métodos para exibir as informações
    public void printInfo() {
        System.out.println("index: " + index);
        System.out.println("fullName: " + fullName);
        System.out.println("birthdate: " + birthdate);
        System.out.println("hogwartsHouse: " + hogwartsHouse);
        System.out.println("interpretedBy: " + interpretedBy);
        System.out.println("image: " + image);

        if (children.length > 0) {
            System.out.print("children: [");
            for (String child : children) {
                System.out.print(child + ", ");
            }
            System.out.println("]");
        } else {
            System.out.println("children: []");
        }
    }
}

public class Ex16c {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java HarryPotterAPI <index>");
            return;
        }

        String index = args[0];
        String urlString = "https://potterapi-fedeperin.vercel.app/en/characters?index=" + index;

        try {
            // Fazer a conexão HTTP
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Verificar a resposta da conexão
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                // Ler a resposta
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Processar o JSON utilizando Gson
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(response.toString());

                // Verifique se a resposta é um objeto JSON
                if (jsonElement.isJsonObject()) {
                    com.google.gson.JsonObject characterGson = jsonElement.getAsJsonObject();

                    // Extraindo os campos com Gson
                    int characterIndex = characterGson.get("index").getAsInt();
                    String fullName = characterGson.get("fullName").getAsString();
                    String birthdate = characterGson.get("birthdate").getAsString();
                    String hogwartsHouse = characterGson.get("hogwartsHouse").getAsString();
                    String interpretedBy = characterGson.get("interpretedBy").getAsString();
                    String image = characterGson.get("image").getAsString();

                    // Tratamento dos filhos usando javax.json
                    JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
                    JsonObject characterJson = jsonReader.readObject();
                    JsonArray childrenArray = characterJson.getJsonArray("children");
                    String[] children = new String[childrenArray.size()];
                    for (int i = 0; i < childrenArray.size(); i++) {
                        children[i] = childrenArray.getString(i);
                    }

                    // Criar o objeto Character
                    Character character = new Character(characterIndex, fullName, birthdate, hogwartsHouse, interpretedBy, image, children);

                    // Exibir as informações
                    character.printInfo();
                } else {
                    System.out.println("Erro: resposta não é um objeto JSON.");
                }
            } else {
                System.out.println("Erro na conexão: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
