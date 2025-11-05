package alessandraciccone.CorgiConnection.controllers;

import alessandraciccone.CorgiConnection.payloads.CorgiDTO;
import alessandraciccone.CorgiConnection.payloads.CorgiResponseDTO;
import alessandraciccone.CorgiConnection.payloads.CorgiUpdateDTO;
import alessandraciccone.CorgiConnection.services.CorgiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/corgis")
public class CorgiController {

   @Autowired
    private CorgiService corgiService;


   // POST http://localhost3001/corgis

    @PostMapping
    public ResponseEntity<CorgiResponseDTO> createCorgi(@RequestBody CorgiDTO  corgiDTO){
        return ResponseEntity.status(201).body(corgiService.createCorgi(corgiDTO));
    }
// GET CORGI PER ID http://localhost:3001/corgis/{id}

    @GetMapping("/{id}")
    public ResponseEntity<CorgiResponseDTO> getCorgiById(@PathVariable UUID id){
        return ResponseEntity.ok(corgiService.getCorgiById(id));
    }


    //AGGIORNO UN CORGI http://localhost:3001/corgis/{id}

    @PutMapping("/{id}")
    public ResponseEntity<CorgiResponseDTO> updateCorgi(@PathVariable UUID id, @RequestBody CorgiUpdateDTO updateDTO){
        return ResponseEntity.ok(corgiService.updateCorgi(id, updateDTO));
    }

    //CANCELLO UN CORGI   http://localhost:3001/corgis/{id}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DELETEcORGI(@PathVariable UUID id){
        corgiService.deleteCorgi(id);
        return ResponseEntity.noContent().build();
    }

    // TROVO TUTTI I CORGI SENZA PAGINAZIONE O CON PAGINAZIONE
    // http://localhost:3001/corgis(senza)
    //http://localhost:3001/corgis?page=0&size=10&sortBy=name(con)

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


//TROVO IL CORGI DI UN PROPRIETARIO http://localhost:3001/corgis/owner/{ownerId}
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
