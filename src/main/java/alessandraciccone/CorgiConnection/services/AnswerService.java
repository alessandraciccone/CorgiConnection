package alessandraciccone.CorgiConnection.services;

import alessandraciccone.CorgiConnection.entities.Answer;
import alessandraciccone.CorgiConnection.entities.Question;
import alessandraciccone.CorgiConnection.exceptions.BadRequestException;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
import alessandraciccone.CorgiConnection.payloads.AnswerDTO;
import alessandraciccone.CorgiConnection.payloads.AnswerResponseDTO;
import alessandraciccone.CorgiConnection.payloads.AnswerUpdateDTO;
import alessandraciccone.CorgiConnection.payloads.QuestionSummaryDTO;
import alessandraciccone.CorgiConnection.repositories.AnswerRepository;
import alessandraciccone.CorgiConnection.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnswerService {



    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    //creo nuova risposta
    public AnswerResponseDTO createAnswer(AnswerDTO answerDTO) {
        // Verifica che la domanda esista
        Question question = questionRepository.findById(answerDTO.questionId())
                .orElseThrow(() -> new NotFoundException(
                        "Domanda con id " + answerDTO.questionId() + " non trovata"));

        // Verifica che non ci siano già 4 risposte per questa domanda
        long answersCount = answerRepository.findAll().stream()
                .filter(a -> a.getQuestion().getId().equals(answerDTO.questionId()))
                .count();

        if (answersCount >= 4) {
            throw new BadRequestException(
                    "Una domanda può avere massimo 4 risposte");
        }

        Answer newAnswer = new Answer();
        newAnswer.setAnswerText(answerDTO.answerText());
        newAnswer.setCorrect(answerDTO.isCorrect());
        newAnswer.setQuestion(question);

        Answer savedAnswer = answerRepository.save(newAnswer);
        return mapToResponseDTO(savedAnswer);
    }

    //cerco x id
    public AnswerResponseDTO getAnswerById(UUID id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Risposta con id " + id + " non trovata"));
        return mapToResponseDTO(answer);
    }

    //aggiorno risposta
    public AnswerResponseDTO updateAnswer(UUID id, AnswerUpdateDTO updateDTO) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Risposta con id " + id + " non trovata"));

        if (updateDTO.answerText() != null) {
            answer.setAnswerText(updateDTO.answerText());
        }
        if (updateDTO.isCorrect() != null) {
            answer.setCorrect(updateDTO.isCorrect());
        }

        Answer updatedAnswer = answerRepository.save(answer);
        return mapToResponseDTO(updatedAnswer);
    }
//elimino risposta

    public void deleteAnswer(UUID id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Risposta con id " + id + " non trovata"));
        answerRepository.delete(answer);
    }


    //trovo tutte le risposte con paginazione e senza
    public Page<AnswerResponseDTO> getAllAnswers(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return answerRepository.findAll(pageable).map(this::mapToResponseDTO);
    }


    public List<AnswerResponseDTO> getAllAnswers() {
        return answerRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

//trovo risposte x domanda
public List<AnswerResponseDTO> getAnswersByQuestion(UUID questionId) {
    // Verifica che la domanda esista
    Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new NotFoundException(
                    "Domanda con id " + questionId + " non trovata"));

    return answerRepository.findAll().stream()
            .filter(answer -> answer.getQuestion().getId().equals(questionId))
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
}

//trovo risposte giuste
public List<AnswerResponseDTO> getCorrectAnswers() {
    return answerRepository.findByIsCorrect(true).stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
}

//trovo risposte sbagliate



    public List<AnswerResponseDTO> getInCorrectAnswers() {
        return answerRepository.findByIsCorrect(false).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


//trovo risposta corretta x domanda

    public AnswerResponseDTO getCorrectAnswerForQuestion(UUID questionId) {
        // verifico che la domanda esista
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException(
                        "Domanda con id " + questionId + " non trovata"));

        Answer correctAnswer = answerRepository.findAll().stream()
                .filter(answer -> answer.getQuestion().getId().equals(questionId))
                .filter(Answer::getCorrect)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        "Nessuna risposta corretta trovata per questa domanda"));

        return mapToResponseDTO(correctAnswer);
    }

//ve3rifico se la risposta è corretta
public boolean isAnswerCorrect(UUID answerId) {
    Answer answer = answerRepository.findById(answerId)
            .orElseThrow(() -> new NotFoundException(
                    "Risposta con id " + answerId + " non trovata"));
    return answer.getCorrect();
}

//conto tutte le risposte

    public long countAllAnswers() {
        return answerRepository.count();
    }
//comto le rispopste x domanda

    public long countAnswersByQuestion(UUID questionId) {
        return answerRepository.findAll().stream()
                .filter(answer -> answer.getQuestion().getId().equals(questionId))
                .count();
    }

    //conto risposte corrette e risposte sbagliate

    public long countCorrectAnswers() {
        return answerRepository.findByIsCorrect(true).size();
    }


    public long countIncorrectAnswers() {
        return answerRepository.findByIsCorrect(false).size();
    }


    private AnswerResponseDTO mapToResponseDTO(Answer answer) {
        QuestionSummaryDTO questionSummary = new QuestionSummaryDTO(
                answer.getQuestion().getId(),
                answer.getQuestion().getQuestionText()
        );

        return new AnswerResponseDTO(
                answer.getId(),
                answer.getAnswerText(),
                answer.getCorrect(),
                questionSummary
        );
    }



}
