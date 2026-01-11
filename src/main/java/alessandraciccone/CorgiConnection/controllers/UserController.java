package alessandraciccone.CorgiConnection.controllers;


import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
import alessandraciccone.CorgiConnection.payloads.UserDTO;
import alessandraciccone.CorgiConnection.payloads.UserResponseDTO;
import alessandraciccone.CorgiConnection.payloads.UserUpdateDTO;
import alessandraciccone.CorgiConnection.services.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // CREA NUOVO UTENT
    //http://localhost:3001/users
    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    // TROVA UTENTE PER ID
    //http://localhost:3001/users/{id}
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    // TROVA UTENTE PER USERNAME
    //http://localhost:3001/users/{username}
    @GetMapping("/username/{username}")
    public UserResponseDTO getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    // TROVA UTENTE PER EMAIL
    //    http://localhost:3001/users//email/{email}
    @GetMapping("/email/{email}")
    public UserResponseDTO getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    // AGGIORNA UTENTE
    //http://localhost:3001/users/{id}

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable UUID id, @RequestBody UserUpdateDTO updateDTO) {
        return userService.updateUser(id, updateDTO);
    }

    // ELIMINA UTENTE
    //http://localhost:3001/users/{id}

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    // LISTA TUTTI GLI UTENTI CON PAGINAZIONE
    // esempio : pagina 1, 5 utenti x mail http://localhost:3001/users?page=1&size=5&sortBy=email

    @GetMapping
    public Page<UserResponseDTO> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy
    ) {
        return userService.getAllUser(page, size, sortBy);
    }
//promuovo utente esistente ad admin
    @PutMapping("/{id}/promote")
    public UserResponseDTO promoteToAdmin(@PathVariable UUID id) {
        return userService.promoteToAdmin(id);
    }

    @PostMapping("users/{id}/profile-image")
    public ResponseEntity<User> uploadProfileImage(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file )throws  IOException{
        if(file==null||file.isEmpty()){
            throw new BadRequestException("file con immagine vuota o mancante");
        }

        String contentType= file.getContentType();
        if(contentType==null|| !contentType.startsWith("image/")){
            throw  new BadRequestException("Il file caricato non è idoneo!");
        }

        long maxSize = 5 * 1024 * 1024; // 5mg
        if (file.getSize() > maxSize) {
            throw new BadRequestException("Il file immagine supera la dimensione massima consentita di 5 MB");
        }
        // verifico utente esistemte e autorizzazione

        User user = userService.fetUserById(id);
        if(user== null){
            throw new NotFoundException("Utente con id" +id +"non trovato");
        }



        User responseDTO= userService.fetUserById(id);
        return ResponseEntity.ok(responseDTO);

    }



    // RICERCA UTENTI CON FILTRI
    // base link http://localhost:3001/users/search
    //esempio: cerco post con città milano http://localhost:3001/users/search?city=Milano
    // altro esemopio: cerco posto con provincia milano e username alessandra http://localhost:3001/users/search?province=MI&username=alessandra
    @GetMapping("/search")
    public Page<UserResponseDTO> searchUsers(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Date registrationDateAfter,
            @RequestParam(required = false) Date registrationDateBefore,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy
    ) {
        return userService.searchUsers(city, province, firstName, lastName, username, registrationDateAfter, registrationDateBefore, page, size, sortBy);
    }

    // TROVA UTENTI PER CITTÀ
    //http://localhost:3001/users/city/{city}
    @GetMapping("/city/{city}")
    public List<UserResponseDTO> getUsersByCity(@PathVariable String city) {
        return userService.getUsersByCity(city);
    }


    // TROVA UTENTI PER PROVINCIA
    //http://localhost:3001/users/province/{province}
    @GetMapping("/province/{province}")
    public List<UserResponseDTO> getUsersByProvince(@PathVariable String province) {
        return userService.getUsersByProvince(province);
    }

    // CONTO UTENTI
    //http://localhost:3001/users/count
    @GetMapping("/count")
    public long countUsers() {
        return userService.countUsers();
    }
}

