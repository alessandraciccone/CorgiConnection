package alessandraciccone.CorgiConnection.services;

import alessandraciccone.CorgiConnection.entities.Answer;
import alessandraciccone.CorgiConnection.entities.Question;
import alessandraciccone.CorgiConnection.entities.Quiz;
import alessandraciccone.CorgiConnection.entities.QuizResult;
import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.payloads.AnswerSubmissionDTO;
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

    // ========================================
    // NUOVO METODO: SOTTOMISSIONE QUIZ
    // ========================================
    public QuizResultResponseDTO submitQuiz(QuizSubmissionDTO dto, UUID userId) {
        System.out.println("🔍 Service - submitQuiz chiamato");
        System.out.println("🔍 UserId: " + userId);
        System.out.println("🔍 Numero risposte: " + dto.answers().size());

        // Trova user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + userId));

        int correctAnswers = 0;
        int totalQuestions = dto.answers().size();
        UUID quizId = null;

        // Valuta risposte
        for (AnswerSubmissionDTO answerDto : dto.answers()) {
            try {
                if (answerDto.questionId() == null || answerDto.answerId() == null) {
                    System.err.println("❌ ID null trovato: " + answerDto);
                    continue;
                }

                System.out.println("🔍 Cerco Question ID: " + answerDto.questionId());
                System.out.println("🔍 Cerco Answer ID: " + answerDto.answerId());

                // Trova question
                Question question = questionRepository.findById(answerDto.questionId())
                        .orElseThrow(() -> new RuntimeException("Domanda non trovata: " + answerDto.questionId()));

                // Prendi quizId dalla prima domanda
                if (quizId == null && question.getQuiz() != null) {
                    quizId = question.getQuiz().getId();
                    System.out.println("✅ Quiz ID trovato: " + quizId);
                }

                // Trova answer
                Answer userAnswer = answerRepository.findById(answerDto.answerId())
                        .orElseThrow(() -> new RuntimeException("Risposta non trovata: " + answerDto.answerId()));

                System.out.println("✅ Question: " + question.getQuestionText());
                System.out.println("✅ Answer: " + userAnswer.getAnswerText());
                System.out.println("✅ Is correct? " + userAnswer.getCorrect());

                // Controlla se corretta
                if (Boolean.TRUE.equals(userAnswer.getCorrect())) {
                    correctAnswers++;
                }

            } catch (Exception e) {
                System.err.println("❌ Errore elaborazione risposta: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Trova quiz
        if (quizId == null) {
            throw new RuntimeException("Impossibile determinare il quiz dalle domande");
        }

        UUID finalQuizId = quizId;
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz non trovato con ID: " + finalQuizId));

        System.out.println("✅ Quiz trovato: " + quiz.getTitleQuiz());
        System.out.println("✅ Punteggio: " + correctAnswers + "/" + totalQuestions);

        // Salva risultato
        QuizResult result = new QuizResult(user, quiz, correctAnswers, totalQuestions);
        QuizResult saved = quizResultRepository.save(result);

        System.out.println("✅ Risultato salvato con ID: " + saved.getId());

        return mapToResponseDTO(saved);
    }

    // ========================================
    // CREA NUOVO QUIZ RESULT (metodo originale)
    // ========================================
    public QuizResultResponseDTO createResult(QuizResultDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + dto.userId()));

        Quiz quiz = quizRepository.findById(dto.quizId())
                .orElseThrow(() -> new RuntimeException("Quiz non trovato con ID: " + dto.quizId()));

        QuizResult result = new QuizResult(
                user,
                quiz,
                dto.score(),
                dto.totalQuestions()
        );

        QuizResult saved = quizResultRepository.save(result);
        return mapToResponseDTO(saved);
    }

    // ========================================
    // RESTITUISCO TUTTI I RISULTATI
    // ========================================
    public List<QuizResultResponseDTO> getAllResults() {
        return quizResultRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ========================================
    // RISULTATO PER ID
    // ========================================
    public QuizResultResponseDTO getResultById(UUID id) {
        QuizResult result = quizResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Risultato del quiz non trovato con ID: " + id));

        return mapToResponseDTO(result);
    }

    // ========================================
    // AGGIORNO PUNTEGGIO E DOMANDE
    // ========================================
    public QuizResultResponseDTO updateResult(UUID id, QuizResultUpdateDTO dto) {
        QuizResult result = quizResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizResult non trovato con ID: " + id));

        result.setScore(dto.score());
        result.setTotalQuestions(dto.totalQuestions());

        QuizResult updated = quizResultRepository.save(result);
        return mapToResponseDTO(updated);
    }

    // ========================================
    // ELIMINO RISULTATO
    // ========================================
    public void deleteResult(UUID id) {
        if (!quizResultRepository.existsById(id)) {
            throw new RuntimeException("QuizResult non trovato con ID: " + id);
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