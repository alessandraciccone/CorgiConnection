package alessandraciccone.CorgiConnection.controllers;


import alessandraciccone.CorgiConnection.payloads.QuizResultResponseDTO;
import alessandraciccone.CorgiConnection.payloads.QuizResultUpdateDTO;
import alessandraciccone.CorgiConnection.services.QuizResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/quiz-results")
public class QuizResultsController {

    @Autowired
    private QuizResultService quizResultService;

    //PRENDO TUTTI RISULTATI
    //http://localhost/8888/quiz-results

    @GetMapping
    public ResponseEntity<List<QuizResultResponseDTO>> getAllResults(){
        return ResponseEntity.ok(quizResultService.getAllResults());
    }

    //PRENDO UN RISULTATO PER ID
    //http://localhost:8888/quiz-resukt/{id}

    @GetMapping("/{id}")
    public ResponseEntity<QuizResultResponseDTO> getResultById(@PathVariable UUID id) {
        return ResponseEntity.ok(quizResultService.getResultById(id));
    }

    //CREO UN NUOVO RISULKTATO
    //http://localhost:8888/quiz-results

    @PostMapping
    public ResponseEntity<QuizResultResponseDTO> createResult(@RequestBody alessandraciccone.CorgiConnection.payloads.QuizResultDTO dto){
        return ResponseEntity.ok(quizResultService.createResult(dto));
    }

    //AGGIORNO UN NRISULTATO
    //http://localhost:8888/quiz-results/{id}

    @PutMapping("/{id}")
    public ResponseEntity<QuizResultResponseDTO> updateResult(
            @PathVariable UUID id,
            @RequestBody QuizResultUpdateDTO dto){
        return ResponseEntity.ok(quizResultService.updateResult(id, dto));
    }

    //DELETE BY id
    //htpp://localhost:8888/quiz-results/{id}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable UUID id){
        quizResultService.deleteResult(id);
        return ResponseEntity.noContent().build();}
}
