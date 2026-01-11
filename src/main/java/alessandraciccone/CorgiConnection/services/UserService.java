package alessandraciccone.CorgiConnection.services;

import alessandraciccone.CorgiConnection.entities.Roles;
import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.exceptions.BadRequestException;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
import alessandraciccone.CorgiConnection.payloads.UserDTO;
import alessandraciccone.CorgiConnection.payloads.UserResponseDTO;
import alessandraciccone.CorgiConnection.payloads.UserUpdateDTO;
import alessandraciccone.CorgiConnection.repositories.UserRepository;
import alessandraciccone.CorgiConnection.specifications.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//il controller chiama il service. il service parla con il db tramite la repository. il service lancia eccezioni che vengono gestite globalmente dal handler
@Service
public class UserService {
  @Autowired
    private UserRepository userRepository; //inietto la repository x interagire con il db

   @Autowired
    private PasswordEncoder passwordEncoder; //inietto password encoder x criptare la password

    //creo nuovo utente
    public UserResponseDTO createUser(UserDTO userDTO) {
        //verifico che la mail e l'username non esistano
        if (userRepository.existsByEmail(userDTO.email())) {
            throw new BadRequestException("Email già in uso!");
        }
        if (userRepository.existsByUsername(userDTO.username())) {
            throw new BadRequestException("Username già in uso!");
        }

        User newUser = new User();
        newUser.setUsername(userDTO.username());
        newUser.setEmail(userDTO.email());
        newUser.setPassword(passwordEncoder.encode(userDTO.password()));
        newUser.setFirstName(userDTO.firstName());
        newUser.setLastName(userDTO.lastName());
        newUser.setCity(userDTO.city());
        newUser.setProvince(userDTO.province());
        newUser.setProfileImage(userDTO.profileImage());
        newUser.setRegistrationDate( new Date());
        User savedUser = userRepository.save(newUser);

        //ritorna il dto di risposta

        return mapToResponseDTO(savedUser);//converto entity in dto restituisce i dati senza password
    }

    public User getUserByUsernameEntity(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utente con username " + username + " non trovato"));
    }
//recupero utente per username (entity)


    // trovo utente per id
    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Utente con id " + id + "non è stato trovato!"));
        return mapToResponseDTO(user); //converto entity in dto restituisce i dati senza password
    }


//trovo utente per emaik

    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("utente con email " + email + "non è stato trovato"));
        return mapToResponseDTO(user);
    }

    public User fetUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente con ID " + id + " non trovato"));
    }


// trovo utente per username

    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Utente con username" + username + "non è stato trovato"));
        return mapToResponseDTO(user);
    }



    //promuovo utente esustente ad admuin
    public UserResponseDTO promoteToAdmin(UUID userId) {
        User user = fetUserById(userId);
        user.setRoles(Roles.ADMIN);
        userRepository.save(user);
        return mapToResponseDTO(user);
    }


    // Aggiorno l'utente

    public UserResponseDTO updateUser(UUID id, UserUpdateDTO updateDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("utente con id " + id + "non è stato trovato"));

        //verifico se c'è un username uguale(qual'ora venisse modificato!)

        if (updateDTO.username() != null && !updateDTO.username().equals(user.getUsername())) {
            if (userRepository.existsByUsername(updateDTO.username())) {
                throw new BadRequestException("username già  in uso!");
            }
            user.setUsername(updateDTO.username());


        }

//verifico se c'è un'email uguale(qual'ora venisse modificata)
        if (updateDTO.email() != null && !updateDTO.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateDTO.email())) {
                throw new BadRequestException("Email già in uso!");
            }
            user.setEmail(updateDTO.email());
        }


        //aggiorniamo gli altri campo sono se non null

        if (updateDTO.firstName() != null) user.setFirstName(updateDTO.firstName());
        if (updateDTO.lastName() != null) user.setLastName(updateDTO.lastName());
        if (updateDTO.city() != null) user.setCity(updateDTO.city());
        if (updateDTO.province() != null) user.setProvince(updateDTO.province());
        if (updateDTO.profileImage() != null) user.setProfileImage(user.getProfileImage());


        User updateUser = userRepository.save(user);
        return mapToResponseDTO(updateUser);
    }

    //eliminiamo utente

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Utente con id" + id + "non è stato trovato"));
        userRepository.delete(user);
    }

    //troviamo tutti gli utente cpn paginazione

    public Page<UserResponseDTO> getAllUser(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    // troviamo utenti con i filtri specification

    public Page<UserResponseDTO> searchUsers(
            String city,
            String province,
            String firstName,
            String lastName,
            String username,
            Date registrationDateAfter,
            Date registrationDateBefore,
            int page,
            int size,
            String sortBy
    ) {
        Specification<User> spec = (root, query, cb) -> cb.conjunction();

        if (city != null && !city.isEmpty()) {
            spec = spec.and(UserSpecification.cityContains(city));
        }

        if (province != null && !province.isEmpty()) {
            spec = spec.and(UserSpecification.provinceContains(province));
        }

        if (firstName != null && !firstName.isEmpty()) {
            spec = spec.and(UserSpecification.firstNameContains(firstName));
        }

        if (lastName != null && !lastName.isEmpty()) {
            spec = spec.and(UserSpecification.lastNameContains(lastName));
        }

        if (username != null && !username.isEmpty()) {
            spec = spec.and(UserSpecification.usernameContains(username));
        }

        if (registrationDateAfter != null) {
            spec = spec.and(UserSpecification.registrationDataAfter(registrationDateAfter));
        }

        if (registrationDateBefore != null) {
            spec = spec.and(UserSpecification.registrationDataBefore(registrationDateBefore));
        }
//uso map x convertire ogni user in user response dto
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(spec, pageable).map(this::mapToResponseDTO);
    }

    //trovo utenti x città

public List<UserResponseDTO> getUsersByCity(String city){
        Specification<User> spec= UserSpecification.cityContains(city);
        return userRepository.findAll(spec).stream()// lo stream converte la lista in una sequenza di elementi che posso trasformare,filtrare ecc
                .map(this::mapToResponseDTO)//converte user in userresponsedto
                .collect(Collectors.toList());//raccoglie tutti gli elementi trasformati in una nuova lista
}

    public void updateProfileImage(UUID userId, String imageUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente con ID " + userId + " non trovato"));
        user.setProfileImage(imageUrl);
        userRepository.save(user);
    }

//trovo utenti per provincia

    public List<UserResponseDTO> getUsersByProvince(String province) {
        Specification<User> spec = UserSpecification.provinceContains(province);
        return userRepository.findAll(spec).stream()// lo stream converte la lista in una sequenza di elementi che posso trasformare,filtrare ecc
                .map(this::mapToResponseDTO)//converte user in userresponsedto
                .collect(Collectors.toList());//raccoglie tutti gli elementi trasformati in una nuova lista
    }


//conto utenti
    public long countUsers(){
        return userRepository.count();
    }


    //verifichiamo se la mail esiste

    public boolean emailExists(String email){
        return userRepository.existsByEmail(email);
    }


    // verifichiamo se username esiste

    public boolean usernameExists(String username){
        return userRepository.existsByUsername(username);
    }

    //convertiamo entity a responseDTO

    private UserResponseDTO mapToResponseDTO (User user) {
        return new UserResponseDTO(

                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCity(),
                user.getProvince(),
                user.getRegistrationDate(),
                user.getProfileImage()
        );
    }
}


