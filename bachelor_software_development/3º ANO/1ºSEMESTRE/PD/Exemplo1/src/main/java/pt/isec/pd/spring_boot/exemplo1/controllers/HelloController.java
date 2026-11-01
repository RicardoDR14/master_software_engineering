package pt.isec.pd.spring_boot.exemplo1.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hello")
public class HelloController {
    @GetMapping("/hello")
    //@GetMapping()
    public String hello() {
        return "Hello!";
    }

    /*@PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("Authorization") String credentials){
        return new ResponseEntity<>();
    }*/

    /*@GetMapping("/hello/{lang}")
    //@GetMapping("{lang}")
    public String helloLanguages(@PathVariable("lang") String language,
                                 @RequestParam(name = "name", required = false, defaultValue = "") String name) {

        return switch (language.toUpperCase()){
            case "UK" -> "Hello " + name + "!";
            case "PT" -> "Ola' " + name + "!";
            case "ES" -> "Hola " + name + "!";
            case "FR" -> "Salut " + name + "!";
            default -> "Unsupported language!";
        };
    }


    @GetMapping("{lang}")
    public ResponseEntity<String> helloLanguages(@PathVariable("lang") String language,
                                                 @RequestParam(name = "name", required = false, defaultValue = "") String name) {

        String responseBody;

        switch (language.toUpperCase()){
            case "UK" -> responseBody = "Hello " + name + "!";
            case "PT" -> responseBody ="Ola' " + name + "!";
            case "ES" -> responseBody = "Ola " + name + "!";
            case "FR" -> responseBody = "Salut " + name + "!";
            default -> {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not supported language:" + language + ".");
            }
        }

        //pt.isec.pd.spring_boot.exemplo1.Application.log.info(responseBody);

        return ResponseEntity.ok(responseBody);
    }*/
}


