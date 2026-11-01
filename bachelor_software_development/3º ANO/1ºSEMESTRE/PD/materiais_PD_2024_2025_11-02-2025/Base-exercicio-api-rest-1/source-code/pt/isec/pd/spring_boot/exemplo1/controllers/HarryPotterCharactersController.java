package pt.isec.pd.spring_boot.exemplo1.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import pt.isec.pd.spring_boot.exemplo1.Application;
import pt.isec.pd.spring_boot.exemplo1.models.MovieCharacter;

@RestController
@RequestMapping("movies/harry-potter/characters")
public class HarryPotterCharactersController {

    @GetMapping
    public ResponseEntity getCharacters(@RequestParam(name = "index", required = false) Integer index) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .contentType(MediaType.parseMediaType("text/plain"))
                .body("Endpoint not implemented yet!");
    }

    @GetMapping("images/{file-name}")
    public ResponseEntity getImage(@PathVariable("file-name") String fileName) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .contentType(MediaType.parseMediaType("text/plain"))
                .body("Endpoint not implemented yet!");
    }

    @GetMapping("images")
    public ResponseEntity getFileNames(@RequestParam(name = "index", required = false) Integer index) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .contentType(MediaType.parseMediaType("text/plain"))
                .body("Endpoint not implemented yet!");
    }

    @GetMapping("images/{file-name}/length")
    public ResponseEntity getImageLength(@PathVariable("file-name") String fileName) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .contentType(MediaType.parseMediaType("text/plain"))
                .body("Endpoint not implemented yet!");
    }

}
