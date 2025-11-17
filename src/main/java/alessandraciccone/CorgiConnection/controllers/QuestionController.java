package alessandraciccone.CorgiConnection.controllers;

import alessandraciccone.CorgiConnection.payloads.QuestionDTO;
import alessandraciccone.CorgiConnection.payloads.QuestionResponseDTO;
import alessandraciccone.CorgiConnection.payloads.QuestionUpdateDTO;
import alessandraciccone.CorgiConnection.services.QuestionService;
import jakarta.validation.Valid;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/questions")
@Validated
public class QuestionController {
    @Autowired
    public QuestionService questionService;


    //OOTTENGO TUTTE LE DOMANDE
    //http://localhost:8888/questions
    @GetMapping
    public ResponseEntity<List<QuestionResponseDTO>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }


    //OTTENGO DOMANDA PER ID
    //http://localhost:3001/questions{id}

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> getQuestionById(@PathVariable UUID id){
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }


    //CREO  HUOVA DOMANDA
    //http://localhost:3001/questions

    @PostMapping
    public ResponseEntity<QuestionResponseDTO>createQuestion(@Valid @RequestBody QuestionDTO dto){
        return ResponseEntity.ok(questionService.createQuestion(dto));
    }


    //AGGIORMO DOMANDA
    //http://localhost:3001/questions{id}

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> updateQuestion(
            @PathVariable UUID id,
            @Valid @RequestBody QuestionUpdateDTO dto
            ){
        return  ResponseEntity.ok(questionService.updateQuestion(id, dto));
    }

    //CANCELLO DOMANDA
//http://localhost:3001/questions{id}
   @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable UUID id){
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
   }
}
