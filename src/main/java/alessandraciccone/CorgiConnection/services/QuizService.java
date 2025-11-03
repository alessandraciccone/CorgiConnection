package alessandraciccone.CorgiConnection.services;


import alessandraciccone.CorgiConnection.entities.Question;
import alessandraciccone.CorgiConnection.entities.Quiz;
import alessandraciccone.CorgiConnection.exceptions.BadRequestException;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
import alessandraciccone.CorgiConnection.payloads.QuizDTO;
import alessandraciccone.CorgiConnection.payloads.QuizResponseDTO;
import alessandraciccone.CorgiConnection.payloads.QuizUpdateDTO;
import alessandraciccone.CorgiConnection.payloads.UserUpdateDTO;
import alessandraciccone.CorgiConnection.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    //prendo tutti i quind

    public List<QuizResponseDTO> getAllQuizzes(){
        List<Quiz> quizzes= quizRepository.findAll();

        if(quizzes.isEmpty()){
            throw  new NotFoundException("Nessun quiz è stato trovato");
        }

        return quizzes.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

//cerca x id

    public QuizResponseDTO getQuizById(UUID id){
        Quiz quiz= quizRepository.findById(id).orElseThrow(()-> new NotFoundException("Quiz con id" + id +"non trovato"));
        return mapToResponseDTO(quiz);
    }

    //crea nuovo quiz


    public QuizResponseDTO createQuiz(QuizDTO dto){
        //controllo se il titolo esiste

        if(quizRepository.findByTitleQuiz(dto.titleQuiz())!=null){
            throw  new BadRequestException("QUiz già esistente");
        }

        Quiz quiz = new Quiz();
        quiz.setTitleQuiz(dto.titleQuiz());
        quiz.setDescriptionQuiz(dto.descriptionQuiz());

        //gestiamo successivamente le domande

        Quiz saved= quizRepository.save(quiz);
        return mapToResponseDTO(saved);
    }



    // aggiorno quiz

public QuizResponseDTO updateQuiz(UUID id, QuizUpdateDTO dto){
        Quiz quiz= quizRepository.findById(id).orElseThrow(()-> new NotFoundException("Quiz con id " +id +"non è stato trovato"));

        if(dto.titleQuiz()!= null && !dto.titleQuiz().isBlank()){
            //verofochiamo se esiste il titolo

            Quiz existings = quizRepository.findByTitleQuiz(dto.titleQuiz());
            if(existings!= null &&!existings.getId().equals(id)){
                throw new BadRequestException("Titolo già in uso");
            }
            quiz.setTitleQuiz(dto.titleQuiz());
        }
        if(dto.descriptionQuiz()!= null)
            quiz.setDescriptionQuiz(dto.descriptionQuiz());

        Quiz updated= quizRepository.save(quiz);
        return mapToResponseDTO(updated);
}



// cancello quiz
    public void deleteQuiz(UUID id){
        Quiz quiz =quizRepository.findById(id).orElseThrow(()-> new NotFoundException("Quiz con id "+ id+"non è stato trovato"));
   quizRepository.delete(quiz);
    }






    //li trasformo da entity a response

    private QuizResponseDTO mapToResponseDTO(Quiz quiz){
        List <UUID> questionIds= quiz.getQuestions()!=null ? quiz.getQuestions()
                .stream()//viene presa la lista di question, si crea uno stream che mappa gli id delle questione
                .map(Question::getId)
                .toList()//tutto trasformato in lista
        :List.of();//se question è null allora viene creata una lista vuota immnutabile
        //siamo di fronte a una forma compatta di if/else. se getQuestion non è null allora accade la condizione dopo ?, altrimenti quella dopo :

        return new QuizResponseDTO(
                quiz.getId(),
                quiz.getTitleQuiz(),
                quiz.getDescriptionQuiz(),
                questionIds
        );
    }
}
