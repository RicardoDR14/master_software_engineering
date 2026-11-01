import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Ex16d {

    public static void main(String[] args) {
        String urlString = "https://potterapi-fedeperin.vercel.app/en/characters";
        String saveDirectory = "./characters_images/";

        // Criar diretório se não existir
        File directory = new File(saveDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

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

                // Usar Gson para processar o JSON
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(response.toString());

                if (jsonElement.isJsonArray()) {
                    com.google.gson.JsonArray charactersArrayGson = jsonElement.getAsJsonArray();

                    // Usar javax.json para iterar e processar as imagens
                    JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
                    JsonArray charactersArray = jsonReader.readArray();

                    for (int i = 0; i < charactersArray.size(); i++) {
                        JsonObject characterJson = charactersArray.getJsonObject(i);
                        String fullName = characterJson.getString("fullName");
                        String imageUrl = characterJson.getString("image");

                        // Baixar a imagem
                        downloadImage(imageUrl, saveDirectory + sanitizeFileName(fullName) + ".png");
                    }

                    System.out.println("Imagens baixadas com sucesso!");
                } else {
                    System.out.println("Erro: resposta não é um array JSON.");
                }
            } else {
                System.out.println("Erro na conexão: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Função para baixar a imagem
    private static void downloadImage(String imageUrl, String destinationFile) {
        try {
            URL url = new URL(imageUrl);
            BufferedImage img = ImageIO.read(url);
            File file = new File(destinationFile);
            ImageIO.write(img, "png", file);
        } catch (IOException e) {
            System.out.println("Erro ao baixar a imagem: " + imageUrl);
            e.printStackTrace();
        }
    }

    // Função para garantir que o nome do arquivo seja válido
    private static String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }
}
