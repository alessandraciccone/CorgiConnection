package alessandraciccone.CorgiConnection.controllers;

import alessandraciccone.CorgiConnection.payloads.AnswerDTO;
import alessandraciccone.CorgiConnection.payloads.AnswerResponseDTO;
import alessandraciccone.CorgiConnection.payloads.AnswerUpdateDTO;
import alessandraciccone.CorgiConnection.services.AnswerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    // CREA NUOVA RISPOSTA
    //POST http://localhost:3001/answers
     @PostMapping
    public ResponseEntity<List<AnswerResponseDTO>> createAnswers(@Valid @RequestBody List<AnswerDTO> answers) {
        List<AnswerResponseDTO> saved = answers.stream()
                .map(answerService::createAnswer)
                .collect(Collectors.toList());
        return ResponseEntity.ok(saved);
    }


    // OTTENGO RISPOSTA PER ID
    // GET http://localhost:3001/answers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponseDTO> getAnswerById(@PathVariable UUID id) {
        return ResponseEntity.ok(answerService.getAnswerById(id));
    }

    // MODIFICO RISPOSTA
    // PUT http://localhost:3001/answers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponseDTO> updateAnswer(
            @PathVariable UUID id,
            @Valid @RequestBody AnswerUpdateDTO dto
    ) {
        return ResponseEntity.ok(answerService.updateAnswer(id, dto));
    }

    // CANCELLO RISPOSTA
    // DELETE http://localhost:3001/answers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable UUID id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }

    // OTTENGO TUTTE LE RISPOSTE
    // GET http://localhost:3001/answers
    @GetMapping
    public ResponseEntity<List<AnswerResponseDTO>> getAllAnswers() {
        return ResponseEntity.ok(answerService.getAllAnswers());
    }

    // OTTENGO TUTTE LE RISPOSTE DI UNA DOMANDA
    // GET http://localhost:3001/answers/question/{questionId}
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<AnswerResponseDTO>> getAnswersByQuestion(@PathVariable UUID questionId) {
        return ResponseEntity.ok(answerService.getAnswersByQuestion(questionId));
    }

    // OTTENGO LA RISPOSTA CORRETTA DI UNA DOMANDA
    // GET http://localhost:3001/answers/question/{questionId}/correct
    @GetMapping("/question/{questionId}/correct")
    public ResponseEntity<AnswerResponseDTO> getCorrectAnswerForQuestion(@PathVariable UUID questionId) {
        return ResponseEntity.ok(answerService.getCorrectAnswerForQuestion(questionId));
    }
}
