package alessandraciccone.CorgiConnection.controllers;

import alessandraciccone.CorgiConnection.entities.User;
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

    // GET all
    @GetMapping
    public ResponseEntity<List<QuizResultResponseDTO>> getAllResults() {
        return ResponseEntity.ok(quizResultService.getAllResults());
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<QuizResultResponseDTO> getResultById(@PathVariable UUID id) {
        return ResponseEntity.ok(quizResultService.getResultById(id));
    }

    // POST quiz submission
    @PostMapping
    public ResponseEntity<QuizResultResponseDTO> submitQuiz(
            @RequestBody QuizSubmissionDTO submission,
            @AuthenticationPrincipal User user) {

        QuizResultResponseDTO result = quizResultService.evaluateQuiz(submission, user);
        return ResponseEntity.ok(result);
    }

    // PUT update quiz result
    @PutMapping("/{id}")
    public ResponseEntity<QuizResultResponseDTO> updateResult(
            @PathVariable UUID id,
            @RequestBody QuizResultUpdateDTO dto) {

        return ResponseEntity.ok(quizResultService.updateResult(id, dto));
    }

    // DELETE result
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable UUID id) {
        quizResultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }
}
