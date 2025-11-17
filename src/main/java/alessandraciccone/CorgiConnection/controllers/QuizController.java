package alessandraciccone.CorgiConnection.controllers;


import alessandraciccone.CorgiConnection.payloads.QuizDTO;
import alessandraciccone.CorgiConnection.payloads.QuizResponseDTO;
import alessandraciccone.CorgiConnection.payloads.QuizUpdateDTO;
import alessandraciccone.CorgiConnection.payloads.QuizWithQuestionsDTO;
import alessandraciccone.CorgiConnection.services.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/quizzes")
@Validated
public class QuizController {

    @Autowired
    public QuizService quizService;


    // OTTENGO TUTTI I QUIZ
    // GET http://localhost:8888/quizzes
    @GetMapping
    public ResponseEntity<List<QuizResponseDTO>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }
    // OTTENGO QUIZ PER ID
    // GET http://localhost:3001/quizzes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> getQuizById(@PathVariable UUID id) {
        return ResponseEntity.ok(quizService.getQuizById(id));
    }

    // CREO NUOVO QUIZ (senza domande)
    // POST http://localhost:3001/quizzes
    @PostMapping
    public ResponseEntity<QuizResponseDTO> createQuiz(@Valid @RequestBody QuizDTO dto) {
        return ResponseEntity.ok(quizService.createQuiz(dto));
    }

    // CREO QUIZ CON DOMANDE IN UN'UNICA CHIAMATA
    // POST http://localhost:3001/quizzes/full
    @PostMapping("/full")
    public ResponseEntity<QuizResponseDTO> createQuizWithQuestions(@Valid @RequestBody QuizWithQuestionsDTO dto) {
        return ResponseEntity.ok(quizService.createQuizWithQuestions(dto));
    }

    // AGGIORNO QUIZ
    // PUT http://localhost:3001/quizzes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> updateQuiz(
            @PathVariable UUID id,
            @Valid @RequestBody QuizUpdateDTO dto) {
        return ResponseEntity.ok(quizService.updateQuiz(id, dto));
    }
    // CANCELLO QUIZ
    // DELETE http://localhost:3001/quizzes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable UUID id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

}
