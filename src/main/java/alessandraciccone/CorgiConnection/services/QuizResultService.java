package alessandraciccone.CorgiConnection.services;

import alessandraciccone.CorgiConnection.entities.Answer;
import alessandraciccone.CorgiConnection.entities.Question;
import alessandraciccone.CorgiConnection.entities.Quiz;
import alessandraciccone.CorgiConnection.entities.QuizResult;
import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
import alessandraciccone.CorgiConnection.payloads.QuizResultDTO;
import alessandraciccone.CorgiConnection.payloads.QuizResultResponseDTO;
import alessandraciccone.CorgiConnection.payloads.QuizResultUpdateDTO;
import alessandraciccone.CorgiConnection.payloads.QuizSubmissionDTO;
import alessandraciccone.CorgiConnection.repositories.AnswerRepository;
import alessandraciccone.CorgiConnection.repositories.QuestionRepository;
import alessandraciccone.CorgiConnection.repositories.QuizRepository;
import alessandraciccone.CorgiConnection.repositories.QuizResultRepository;
import alessandraciccone.CorgiConnection.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuizResultService {
    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    // ✅ NUOVO: Valuta il quiz e salva il risultato
    public QuizResultResponseDTO evaluateQuiz(QuizSubmissionDTO submission, User user) {
        int score = 0;
        int totalQuestions = submission.answers().size();

        UUID quizId = null;

        // Calcola il punteggio controllando ogni risposta
        for (QuizSubmissionDTO.AnswerSubmission answerSubmission : submission.answers()) {
            // Trova la risposta selezionata
            Answer selectedAnswer = answerRepository.findById(answerSubmission.answerId())
                    .orElseThrow(() -> new NotFoundException("Risposta non trovata con ID: " + answerSubmission.answerId()));

            // Se è corretta, incrementa il punteggio
            if (Boolean.TRUE.equals(selectedAnswer.getCorrect())) {
                score++;
            }

            // Ottieni il quiz dalla prima domanda (tutte appartengono allo stesso quiz)
            if (quizId == null) {
                Question question = questionRepository.findById(answerSubmission.questionId())
                        .orElseThrow(() -> new NotFoundException("Domanda non trovata con ID: " + answerSubmission.questionId()));
                quizId = question.getQuiz().getId();
            }
        }

        // Calcola la percentuale
        double percentage = (double) score / totalQuestions * 100;

        // Trova il quiz
        UUID finalQuizId = quizId;
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz non trovato con ID: " + finalQuizId));

        // Crea e salva il risultato
        QuizResult quizResult = new QuizResult(user, quiz, score, totalQuestions);
        QuizResult saved = quizResultRepository.save(quizResult);

        return mapToResponseDTO(saved);
    }

    // Nuovo quiz result (manuale)
    public QuizResultResponseDTO createResult(QuizResultDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new NotFoundException("Utente non trovato con ID: " + dto.userId()));

        Quiz quiz = quizRepository.findById(dto.quizId())
                .orElseThrow(() -> new NotFoundException("Quiz non trovato con ID: " + dto.quizId()));

        QuizResult result = new QuizResult(user, quiz, dto.score(), dto.totalQuestions());
        QuizResult saved = quizResultRepository.save(result);
        return mapToResponseDTO(saved);
    }

    // Restituisco i risultati
    public List<QuizResultResponseDTO> getAllResults() {
        return quizResultRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public QuizResultResponseDTO getResultById(UUID id) {
        QuizResult result = quizResultRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Risultato del quiz non trovato con ID: " + id));
        return mapToResponseDTO(result);
    }

    // Aggiorno punteggio e domande
    public QuizResultResponseDTO updateResult(UUID id, QuizResultUpdateDTO dto) {
        QuizResult result = quizResultRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("QuizResult non trovato con ID: " + id));

        result.setScore(dto.score());
        result.setTotalQuestions(dto.totalQuestions());

        QuizResult updated = quizResultRepository.save(result);
        return mapToResponseDTO(updated);
    }

    // Elimino risultato
    public void deleteResult(UUID id) {
        if (!quizResultRepository.existsById(id)) {
            throw new NotFoundException("QuizResult non trovato con ID: " + id);
        }
        quizResultRepository.deleteById(id);
    }

    private QuizResultResponseDTO mapToResponseDTO(QuizResult result) {
        double percentage = 0;
        if (result.getTotalQuestions() != null && result.getTotalQuestions() > 0) {
            percentage = (result.getScore() * 100.0) / result.getTotalQuestions();
        }

        return new QuizResultResponseDTO(
                result.getId(),
                result.getUser().getId(),
                result.getQuiz().getId(),
                result.getScore(),
                result.getTotalQuestions(),
                percentage
        );
    }


}