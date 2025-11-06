package alessandraciccone.CorgiConnection.controllers;

import alessandraciccone.CorgiConnection.entities.Corgi;
import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.exceptions.BadRequestException;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
import alessandraciccone.CorgiConnection.exceptions.UnauthorizedException;
import alessandraciccone.CorgiConnection.payloads.CorgiDTO;
import alessandraciccone.CorgiConnection.payloads.CorgiResponseDTO;
import alessandraciccone.CorgiConnection.payloads.CorgiUpdateDTO;
import alessandraciccone.CorgiConnection.services.CloudinaryService;
import alessandraciccone.CorgiConnection.services.CorgiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/corgis")
public class CorgiController {

   @Autowired
    private CorgiService corgiService;

@Autowired
private CloudinaryService cloudinaryService;
   // POST http://localhost:8888/corgis

    @PostMapping
    public ResponseEntity<CorgiResponseDTO> createCorgi(@RequestBody CorgiDTO  corgiDTO){
        return ResponseEntity.status(201).body(corgiService.createCorgi(corgiDTO));
    }
// GET CORGI PER ID http://localhost:8888/corgis/{id}

    @GetMapping("/{id}")
    public ResponseEntity<CorgiResponseDTO> getCorgiById(@PathVariable UUID id){
        return ResponseEntity.ok(corgiService.getCorgiById(id));
    }

    //aggiungo una foto
    //POST http://localhost:8888/corgis/{id}/photo

    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CorgiResponseDTO> uploadCorgiPhoto(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Il file immagine è obbligatorio e non può essere vuoto");
        }
//validazioone solo img
        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            throw new BadRequestException("Il file caricato non è un'immagine valida");
        }

        long maxSizeBytes = 5L * 1024 * 1024;  //5mg
        if (file.getSize() > maxSizeBytes) {
            throw new BadRequestException("Il file immagine supera la dimensione massima consentita di 5 MB");
        }

        CorgiResponseDTO dto = corgiService.getCorgiById(id);
        if (dto == null) {
            throw new NotFoundException("Il cagnolino con id " + id + " non è stato trovato");
        }


        String photo = cloudinaryService.upload(file, "corgis/photos");

        corgiService.updateCorgiPhoto(id, photo);

        CorgiResponseDTO responseDto = corgiService.getCorgiById(id);
        return ResponseEntity.ok(responseDto);
    }


    //AGGIORNO UN CORGI http://localhost:8888/corgis/{id}

    @PutMapping("/{id}")
    public ResponseEntity<CorgiResponseDTO> updateCorgi(@PathVariable UUID id, @RequestBody CorgiUpdateDTO updateDTO){
        return ResponseEntity.ok(corgiService.updateCorgi(id, updateDTO));
    }

    //CANCELLO UN CORGI   http://localhost:8888/corgis/{id}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DELETEcORGI(@PathVariable UUID id){
        corgiService.deleteCorgi(id);
        return ResponseEntity.noContent().build();
    }

    // TROVO TUTTI I CORGI SENZA PAGINAZIONE O CON PAGINAZIONE
    // http://localhost:8888/corgis(senza)
    //http://localhost:8888/corgis?page=0&size=10&sortBy=name(con)

@GetMapping
    public ResponseEntity<?> getCorgis(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "name") String sortBy
){
        if (page!=null&& size != null){
            return ResponseEntity.ok(corgiService.getAllCorgis(page, size,sortBy));

        }else{
            return ResponseEntity.ok(corgiService.getAllCorgis());
        }
}


//TROVO IL CORGI DI UN PROPRIETARIO http://localhost:8888/corgis/owner/{ownerId}
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<CorgiResponseDTO>> getCorgiByOwner(@PathVariable UUID ownerId){
        return  ResponseEntity.ok(corgiService.getCorgiByOwner(ownerId));
    }

    //CONTO TUTTI I CORGI http://localhost:3001/corgis/count

    @GetMapping("/count")
    public ResponseEntity<Long> countCorgi(){
        return ResponseEntity.ok(corgiService.countCorgis());
    }

    //CONTO I CORGI DI UN PROPRIETARIO http://localhost:3001/corgis/count/owner/{ownerId}
    @GetMapping("/count/owner/{ownerId}")
    public ResponseEntity<Long> countCorgisByOwner(@PathVariable UUID ownerId){
        return ResponseEntity.ok(corgiService.countCorgisByOwner(ownerId));
    }

}
