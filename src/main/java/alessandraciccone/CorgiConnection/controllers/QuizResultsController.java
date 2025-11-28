package alessandraciccone.CorgiConnection.controllers;

import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.payloads.QuizResultDTO;
import alessandraciccone.CorgiConnection.payloads.QuizResultResponseDTO;
import alessandraciccone.CorgiConnection.payloads.QuizResultUpdateDTO;
import alessandraciccone.CorgiConnection.payloads.QuizSubmissionDTO;
import alessandraciccone.CorgiConnection.services.QuizResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/quiz-results")
public class QuizResultsController {

    @Autowired
    private QuizResultService quizResultService;

    // NUOVO ENDPOINT: SOTTOMISSIONE QUIZ
    // POST http://localhost:8888/quiz-results/submit
    @PostMapping("/submit")
    public ResponseEntity<QuizResultResponseDTO> submitQuiz(
            @RequestBody QuizSubmissionDTO dto,
            @AuthenticationPrincipal User user
    ) {
        try {
            System.out.println("===========================================");
            System.out.println(" SUBMIT QUIZ - Inizio");
            System.out.println(" DTO ricevuto: " + dto);
            System.out.println(" Numero risposte: " + (dto != null && dto.answers() != null ? dto.answers().size() : "NULL"));

            if (user == null) {
                System.err.println(" USER È NULL - Utente non autenticato!");
                return ResponseEntity.status(401).build();
            }

            System.out.println(" User autenticato: " + user.getId() + " - " + user.getUsername());

            QuizResultResponseDTO result = quizResultService.submitQuiz(dto, user.getId());

            System.out.println(" Risultato calcolato e salvato: " + result);
            System.out.println("===========================================");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.err.println(" ERRORE NEL CONTROLLER: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    // PRENDO TUTTI I RISULTATI
    // GET http://localhost:8888/quiz-results
    @GetMapping
    public ResponseEntity<List<QuizResultResponseDTO>> getAllResults() {
        return ResponseEntity.ok(quizResultService.getAllResults());
    }

    // PRENDO UN RISULTATO PER ID
    // GET http://localhost:8888/quiz-results/{id}
    @GetMapping("/{id}")
    public ResponseEntity<QuizResultResponseDTO> getResultById(@PathVariable UUID id) {
        return ResponseEntity.ok(quizResultService.getResultById(id));
    }


    // POST http://localhost:8888/quiz-results
    @PostMapping
    public ResponseEntity<QuizResultResponseDTO> createResult(@RequestBody QuizResultDTO dto) {
        return ResponseEntity.ok(quizResultService.createResult(dto));
    }


    // PUT http://localhost:8888/quiz-results/{id}

    @PutMapping("/{id}")
    public ResponseEntity<QuizResultResponseDTO> updateResult(
            @PathVariable UUID id,
            @RequestBody QuizResultUpdateDTO dto) {
        return ResponseEntity.ok(quizResultService.updateResult(id, dto));
    }


    // DELETE BY ID
    // DELETE http://localhost:8888/quiz-results/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable UUID id) {
        quizResultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }
}