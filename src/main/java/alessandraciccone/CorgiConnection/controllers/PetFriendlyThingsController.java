package alessandraciccone.CorgiConnection.controllers;

import alessandraciccone.CorgiConnection.entities.ThingsType;
import alessandraciccone.CorgiConnection.payloads.PetFriendlyThingsDTO;
import alessandraciccone.CorgiConnection.payloads.PetFriendlyThingsResponseDTO;
import alessandraciccone.CorgiConnection.payloads.PetFriendlyThingsUpdateDTO;
import alessandraciccone.CorgiConnection.services.PetFriendlyThingsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pet-friendly-things")
public  class PetFriendlyThingsController {

    @Autowired
    public PetFriendlyThingsService petFriendlyThingsService;


    // creo nuovo evento
    //http://localhost:8888/pet-friendly-things
    @PostMapping
    public ResponseEntity<PetFriendlyThingsResponseDTO> createPetFriendlyThing( @Valid @RequestBody PetFriendlyThingsDTO petFriendlyThingsDTO) {
        PetFriendlyThingsResponseDTO createdThing = petFriendlyThingsService.createPetFriendlyThing(petFriendlyThingsDTO);
        return ResponseEntity.ok(createdThing);
    }
    //trovo evento per id
    //http://localhost:8888/pet-friendly-things/{id]

    @GetMapping("/{id}")
    public ResponseEntity<PetFriendlyThingsResponseDTO> getPetFriendlyThingById(@PathVariable UUID id) {
        PetFriendlyThingsResponseDTO thing = petFriendlyThingsService.getFriendlyThingById(id);
        return ResponseEntity.ok(thing);
    }

//cerco tutti gli eventi
    //http://localhost:8888/pet-friendly-things
    @GetMapping("/all/no-page")
    public ResponseEntity<List<PetFriendlyThingsResponseDTO>> getAllPetFriendlyThingsNoPage() {
        List<PetFriendlyThingsResponseDTO> allThings = petFriendlyThingsService.findAll();
        return ResponseEntity.ok(allThings);
    }


    // aggiorno evento
//http://localhost:8888/pet-friendly-things/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PetFriendlyThingsResponseDTO> updatePetFriendlyThing(@PathVariable UUID id, @RequestBody PetFriendlyThingsUpdateDTO updateDTO) {
        PetFriendlyThingsResponseDTO updatedThing = petFriendlyThingsService.updatePetFriendlyThing(id, updateDTO);
        return ResponseEntity.ok(updatedThing);
    }

    //elimino evento
//http://localhost:8888/pet-friendly-things/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetFriendlyThing(@PathVariable UUID id) {
        petFriendlyThingsService.deletePetFriendlyThing(id);
        return ResponseEntity.noContent().build();
    }


    //dasattivo un evento
//http://localhost:8888/pet-friendly-things/{id}/deactivate
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<PetFriendlyThingsResponseDTO> deactivatePetFriendlyThing(@PathVariable UUID id) {
        PetFriendlyThingsResponseDTO deactivatedThing = petFriendlyThingsService.deactivatePetFriendlyThing(id);
        return ResponseEntity.ok(deactivatedThing);
    }
    //riattivo un evento
   //http://localhost:8888/pet-friendly-things/{id}/reactivate
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<PetFriendlyThingsResponseDTO> reactivatePetFriendlyThing(@PathVariable UUID id) {
        PetFriendlyThingsResponseDTO reactivatedThing = petFriendlyThingsService.reactivatePetFriendlyThing(id);
        return ResponseEntity.ok(reactivatedThing);
    }
    //  ottengo tutti gli eventi con paginazione
    //http://localhost:8888/pet-friendly-things?page={page}&size={size}&sortBy={sortBy}
    @GetMapping
    public ResponseEntity<List<PetFriendlyThingsResponseDTO>> getAllPetFriendlyThings(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy) {
        return ResponseEntity.ok(petFriendlyThingsService.getAllPetFriendlyThings(page, size, sortBy).getContent());
    }

    //ottengo solo eventi attivi
//http://localhost:8888/pet-friendly-things/active
    @GetMapping("/active")
    public ResponseEntity<List<PetFriendlyThingsResponseDTO>> getActivePetFriendlyThings() {
        List<PetFriendlyThingsResponseDTO> activeThings = petFriendlyThingsService.getActivePetFriendlyThings();
        return ResponseEntity.ok(activeThings);
    }

//cerco eventi con filtri
//http://localhost:8888/pet-friendly-things/search?name={name}&type={type}&city={city}&district={district}&region={region}&address={address}&descriptionKeyword={descriptionKeyword}&eventDateAfter={eventDateAfter}&eventDateBefore={eventDateBefore}&startDate={startDate}&endDate={endDate}&page={page}&size={size}&sortBy={sortBy}

    @GetMapping("/search")
    public ResponseEntity<Page<PetFriendlyThingsResponseDTO>> searchPetFriendlyThings(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ThingsType type,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String descriptionKeyword,
            @RequestParam(required = false)Date exactEventDate,
            @RequestParam(required = false) String eventDateAfter,
            @RequestParam(required = false) String eventDateBefore,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "petFriendlyName") String sortBy) {

        // Converto le stringhe delle date in oggetti Date
        Date eventDateAfterParsed = parseDate(eventDateAfter);
        Date eventDateBeforeParsed = parseDate(eventDateBefore);
        Date startDateParsed = parseDate(startDate);
        Date endDateParsed = parseDate(endDate);

        Page<PetFriendlyThingsResponseDTO> things = petFriendlyThingsService.searchPetFriendlyThings(
                name, type, city, district, region, address, descriptionKeyword,exactEventDate,
                eventDateAfterParsed, eventDateBeforeParsed, startDateParsed, endDateParsed, page, size, sortBy);

        return ResponseEntity.ok(things);
    }

    // Metodo di utilità per il parsing delle stringhe in oggetti Date
    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            // Specifica il formato della data che ti aspetti (ad esempio yyyy-MM-dd o un altro formato)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            // Gestisci l'eccezione se il parsing fallisce
            e.printStackTrace();
            return null;
        }
    }


// contot tutti gli eventi
    //http://localhost:8888/pet-friendly-things/count
@GetMapping("/count")
public ResponseEntity<Long> countAllPetFriendlyThings() {
    long count = petFriendlyThingsService.countAllPetFriendlyThings();
    return ResponseEntity.ok(count);
}

// conto gli eventi x tipo
//http://localhost:8888/pet-friendly-things/count/type?type={type}
@GetMapping("/count/type")
public ResponseEntity<Long> countByType(@RequestParam ThingsType type) {
    long count = petFriendlyThingsService.countByType(type);
    return ResponseEntity.ok(count);
}

//conto gli eventi per città
    //http://localhost:8888/pet-friendly-things/count/city?city={city}
public ResponseEntity<Long> countByCity(@RequestParam String city) {
    long count = petFriendlyThingsService.countByCity(city);
    return ResponseEntity.ok(count);
}
}

