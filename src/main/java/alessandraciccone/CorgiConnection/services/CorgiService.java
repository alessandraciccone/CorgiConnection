package alessandraciccone.CorgiConnection.services;

import alessandraciccone.CorgiConnection.entities.Corgi;
import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
import alessandraciccone.CorgiConnection.payloads.CorgiDTO;
import alessandraciccone.CorgiConnection.payloads.CorgiResponseDTO;
import alessandraciccone.CorgiConnection.payloads.CorgiUpdateDTO;
import alessandraciccone.CorgiConnection.payloads.OwnerSummaryDTO;
import alessandraciccone.CorgiConnection.repositories.CorgiRepository;
import alessandraciccone.CorgiConnection.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CorgiService {
    @Autowired
    private CorgiRepository corgiRepository;
    @Autowired
    private UserRepository userRepository;


    //creo un nuovo corgi

    public CorgiResponseDTO createCorgi(CorgiDTO corgiDTO) {
        User owner = userRepository.findById(corgiDTO.owner_Id()).orElseThrow(() -> new NotFoundException("proprietario con id" + corgiDTO.owner_Id() + "non trovato"));

        Corgi newCorgi = new Corgi();
        newCorgi.setName(corgiDTO.name());
        newCorgi.setAge(corgiDTO.age());
        newCorgi.setGender(corgiDTO.gender());
        newCorgi.setType(corgiDTO.type());
        newCorgi.setColor(corgiDTO.color());
        newCorgi.setPersonality(corgiDTO.personality());
        newCorgi.setPhoto(corgiDTO.photo() != null ? corgiDTO.photo() : "/images/default-corgi.jpg");
        newCorgi.setOwner(owner);

        Corgi saveCorgi = corgiRepository.save(newCorgi);
        return mapToResponseDTO(saveCorgi);

    }
    //trovo corgi x id

    public CorgiResponseDTO getCorgiById(UUID id){
        Corgi corgi = corgiRepository.findById(id).orElseThrow(()-> new NotFoundException("Il cagnolino con id "+id+"nonè stato trovato"));
        return mapToResponseDTO(corgi);
    }
        //aggiorno corgi

public CorgiResponseDTO updateCorgi(UUID id, CorgiUpdateDTO updateDTO){
        Corgi corgi= corgiRepository.findById(id).orElseThrow(()-> new NotFoundException("Il cagnolino con id" + id +"non è stato trovato"));


    if (updateDTO.name() != null) corgi.setName(updateDTO.name());
    if (updateDTO.age() != null) corgi.setAge(updateDTO.age());
    if (updateDTO.gender() != null) corgi.setGender(updateDTO.gender());
    if (updateDTO.type() != null) corgi.setType(updateDTO.type());
    if (updateDTO.color() != null) corgi.setColor(updateDTO.color());
    if (updateDTO.personality() != null) corgi.setPersonality(updateDTO.personality());
    if (updateDTO.photo() != null) corgi.setPhoto(updateDTO.photo());

    Corgi updatedCorgi = corgiRepository.save(corgi);
    return mapToResponseDTO(updatedCorgi);

}

//cancello un cane

public void deleteCorgi(UUID id){
        Corgi corgi=corgiRepository.findById(id).orElseThrow(()-> new NotFoundException("Il cagnolino con id" +id +"non è stato trovato"));
        corgiRepository.delete(corgi);
}

//trovo tutti i cani con paginazione

    public Page<CorgiResponseDTO> getAllCorgis(int page, int size, String sortBy){
        Pageable pageable= PageRequest.of(page, size, Sort.by(sortBy));
        return corgiRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    //trovo tutti i cani senza paginazione

    public List<CorgiResponseDTO> getAllCorgis(){
        return corgiRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    //trovo i cani di un proprietario specifico

    public List<CorgiResponseDTO> getCorgiByOwner(UUID owner_Id){
        //verifico che esista il rproprietario
        User owner=userRepository.findById(owner_Id).orElseThrow(()-> new NotFoundException("Il prorietario con id "
                +"non è stato trovato"));

        return corgiRepository.findAll()
                .stream()
                .filter(corgi->corgi.getOwner().getId().equals(owner_Id))
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

    }


//conto tutti i cani
public long countCorgis(){
        return corgiRepository.count();
}

//cerco tutti i cani di un proprietario

    public long countCorgisByOwner(UUID owner_Id){
        return corgiRepository.findAll()
                .stream()
                .filter(corgi->corgi.getOwner().getId().equals(owner_Id))
                .count();
    }
       o, 

//converto entity a response
    private CorgiResponseDTO mapToResponseDTO(Corgi corgi) {
        OwnerSummaryDTO ownerSummary = new OwnerSummaryDTO(
                corgi.getOwner().getId(),
                corgi.getOwner().getUsername(),
                corgi.getOwner().getCity(),
                corgi.getOwner().getProfileImage()
        );

        return new CorgiResponseDTO(
                corgi.getId(),
                corgi.getName(),
                corgi.getAge(),
                corgi.getGender(),
                corgi.getType(),
                corgi.getColor(),
                corgi.getPersonality(),
                corgi.getPhoto(),
                ownerSummary
        );
    }
}


