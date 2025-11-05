package alessandraciccone.CorgiConnection.controllers;


import alessandraciccone.CorgiConnection.payloads.UserDTO;
import alessandraciccone.CorgiConnection.payloads.UserResponseDTO;
import alessandraciccone.CorgiConnection.payloads.UserUpdateDTO;
import alessandraciccone.CorgiConnection.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // CREA NUOVO UTENTE
    //http://localhost:3001/users
    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    // TROVA UTENTE PER ID
    //http://localhost:3001/users/{id}
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable UUID id) {
        return userService.fetUserById(id);
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

