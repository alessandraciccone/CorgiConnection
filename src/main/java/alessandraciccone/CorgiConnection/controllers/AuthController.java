package alessandraciccone.CorgiConnection.controllers;

import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
import alessandraciccone.CorgiConnection.payloads.UserDTO;
import alessandraciccone.CorgiConnection.payloads.UserResponseDTO;
import alessandraciccone.CorgiConnection.security.JwtTool;
import alessandraciccone.CorgiConnection.services.UserService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserDTO userDTO) {
        Map<String, Object> response = new HashMap<>();

        // Controlli preliminari
        if (userDTO.username() == null || userDTO.username().isBlank()) {
            response.put("error", "username non può essere vuoto!");
            return ResponseEntity.badRequest().body(response);
        }

        if (userDTO.email() == null || userDTO.email().isBlank()) {
            response.put("error", "l'email non può essere vuota!");
            return ResponseEntity.badRequest().body(response);
        }

        if (userDTO.password() == null || userDTO.password().isBlank()) {
            response.put("error", "la password non può essere vuota!");
            return ResponseEntity.badRequest().body(response);
        }

        if (userService.usernameExists(userDTO.username())) {
            response.put("error", "username già esistente");
            return ResponseEntity.badRequest().body(response);
        }

        if (userService.emailExists(userDTO.email())) {
            response.put("error", "email già esistente");
            return ResponseEntity.badRequest().body(response);
        }

        UserResponseDTO createdUser = userService.createUser(userDTO);

        response.put("message", "Utente registrato con successo");
        response.put("userId", createdUser.id());
        return ResponseEntity.status(201).body(response);
    }

    //login post http://localhost:3001/auth/login

@PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody  UserDTO userDTO){
        Map<String, Object>response= new HashMap<>();


        if(userDTO.username()==null||userDTO.username().isEmpty()){
            response.put("error"," Username non può essere vuoto!");
            return ResponseEntity.badRequest().body(response);
        }

        if(userDTO.password()==null||userDTO.password().isBlank()){
            response.put("error","La password non può essere vuota");
            return ResponseEntity.badRequest().body(response);
        }

    try {
        // Recupero utente per username
        User user = userService.getUserByUsernameEntity(userDTO.username());

        // Verifico la password
        if (!passwordEncoder.matches(userDTO.password(), user.getPassword())) {
            response.put("error", "Credenziali non valide");
            return ResponseEntity.status(401).body(response);
        }

        // String token = jwtService.generateToken(user);

        response.put("message", "Login avvenuto con successo");
        response.put("userId", user.getId());
        // response.put("token", token);
        return ResponseEntity.ok(response);

    } catch (NotFoundException e) {
        response.put("error", "Credenziali non valide");
        return ResponseEntity.status(401).body(response);
    }
}
}


