package pt.isec.pd.spring_boot.exemplo1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import pt.isec.pd.spring_boot.exemplo1.Application;
import pt.isec.pd.spring_boot.exemplo1.models.MovieCharacter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("movies/harry-potter/characters")
public class HarryPotterCharactersController {

    @GetMapping
    public ResponseEntity getCharacters(@RequestParam(name = "index", required = false) Integer index) {

        try {
            File file = new File(Application.resourceDirectory + "\\data" + File.separator + "\\characters-description.json");

            if (!file.exists() || !file.isFile())
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.parseMediaType("text/plain"))
                        .body("Erro: file not found!");


            ObjectMapper mapper = new ObjectMapper();
            List<MovieCharacter> characterList = mapper.readValue(file, List.class);

            if (index != null && (index < 0 || index >= characterList.size())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.parseMediaType("text/plain"))
                        .body("Out of range index!");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/json"))
                    .body(index == null ? characterList : characterList.get(index));


        } catch (SecurityException | IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("images/{file-name}")
    public ResponseEntity getImage(@PathVariable("file-name") String fileName) {
        try {
            File file = new File(Application.resourceDirectory + File.separator + "images" + File.separator + fileName);

            if (!file.exists() || !file.isFile())
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.parseMediaType("text/plain"))
                        .body("Erro: file not found!");

            /*// Lê o conteúdo do arquivo como bytes
            byte[] imageBytes = Files.readAllBytes(file.toPath());

            // Determina o tipo MIME com base na extensão do arquivo
            MediaType mediaType = MediaType.IMAGE_JPEG;
            if (fileName.endsWith(".png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (fileName.endsWith(".gif")) {
                mediaType = MediaType.IMAGE_GIF;
            }*/

            UrlResource resource = new UrlResource(file.toURI());

            return ResponseEntity.ok()
                    /*.contentType(mediaType)
                    .body(imageBytes);*/
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);


        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("images")
    public ResponseEntity getFileNames(@RequestParam(name = "index", required = false) Integer index) {

        try {
            File folder = new File(Application.resourceDirectory + "\\images");

            if (!folder.exists() || !folder.isDirectory()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.parseMediaType("text/plain"))
                        .body(List.of("Directory not found: images"));
            }

            File[] files = folder.listFiles();
            if (files == null || files.length == 0) {
                return ResponseEntity.ok().body(List.of());
            }

            List<String> fileNames = new ArrayList<>();
            for (File file : files) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }

            if (index != null && (index < 0 || index >= fileNames.size())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.parseMediaType("text/plain"))
                        .body("Out of range index!");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(index == null ? fileNames : "\"" + fileNames.get(index) + "\"");


        } catch (SecurityException ex) {
            System.out.println(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("images/{file-name}/length")
    public ResponseEntity getImageLength(@PathVariable("file-name") String fileName) {
        try {
            File file = new File(Application.resourceDirectory + File.separator + "images" + File.separator + fileName);

            if (!file.exists() || !file.isFile())
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.parseMediaType("text/plain"))
                        .body("Erro: file not found!");

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/plain"))
                    .body("Length of file \"" + fileName + "\": " + file.length() + " bytes");
        } catch (SecurityException ex) {
            System.out.println(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping("images/{new-file-name}")
    public ResponseEntity uploadNewFile(@PathVariable("new-file-name") String fileName,
                                        @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
                                        @RequestBody byte[] fileContent) {
        // Verifica se o tipo de conteúdo é PNG
        if (!contentType.equals(MediaType.IMAGE_PNG_VALUE)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("Erro: Apenas arquivos PNG são permitidos.");
        }



        // Cria o diretório de imagens se ele não existir
        File newFile = new File(Application.resourceDirectory +File.separator+"images"+File.separator+fileName);

        if (newFile.exists()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Erro: O arquivo já existe.");
        }

        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            fos.write(fileContent);
            fos.flush();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar o arquivo.");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Arquivo PNG salvo com sucesso.");
    }
}
