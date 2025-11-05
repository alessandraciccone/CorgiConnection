package alessandraciccone.CorgiConnection.services;

import alessandraciccone.CorgiConnection.entities.Question;
import alessandraciccone.CorgiConnection.entities.Quiz;
import alessandraciccone.CorgiConnection.exceptions.BadRequestException;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
import alessandraciccone.CorgiConnection.payloads.*;
import alessandraciccone.CorgiConnection.repositories.QuestionRepository;
import alessandraciccone.CorgiConnection.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // Recupera tutti i quiz
    public List<QuizResponseDTO> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        if (quizzes.isEmpty()) {
            throw new NotFoundException("Nessun quiz è stato trovato");
        }
        return quizzes.stream().map(this::mapToResponseDTO).toList();
    }

    // Recupera quiz per ID
    public QuizResponseDTO getQuizById(UUID id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quiz con id " + id + " non trovato"));
        return mapToResponseDTO(quiz);
    }

    // Crea un quiz senza domande
    public QuizResponseDTO createQuiz(QuizDTO dto) {
        if (quizRepository.findByTitleQuiz(dto.titleQuiz()) != null) {
            throw new BadRequestException("Quiz già esistente");
        }

        Quiz quiz = new Quiz();
        quiz.setTitleQuiz(dto.titleQuiz());
        quiz.setDescriptionQuiz(dto.descriptionQuiz());

        Quiz saved = quizRepository.save(quiz);
        return mapToResponseDTO(saved);
    }

    // Crea quiz + domande in un'unica chiamata
    public QuizResponseDTO createQuizWithQuestions(QuizWithQuestionsDTO dto) {
        if (quizRepository.findByTitleQuiz(dto.titleQuiz()) != null) {
            throw new BadRequestException("Quiz già esistente");
        }

        // Creo quiz
        Quiz quiz = new Quiz();
        quiz.setTitleQuiz(dto.titleQuiz());
        quiz.setDescriptionQuiz(dto.descriptionQuiz());
        Quiz savedQuiz = quizRepository.save(quiz);

        // Creo domande collegate al quiz
        if (dto.questions() != null) {
            for (QuestionDTO qdto : dto.questions()) {
                Question question = new Question();
                question.setQuestionText(qdto.questionText());
                question.setQuiz(savedQuiz);
                question.setOrderNumber(qdto.orderNumber());
                questionRepository.save(question);
            }
        }

        return mapToResponseDTO(savedQuiz);
    }

    // Aggiorna quiz
    public QuizResponseDTO updateQuiz(UUID id, QuizUpdateDTO dto) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quiz con id " + id + " non trovato"));

        if (dto.titleQuiz() != null && !dto.titleQuiz().isBlank()) {
            Quiz existing = quizRepository.findByTitleQuiz(dto.titleQuiz());
            if (existing != null && !existing.getId().equals(id)) {
                throw new BadRequestException("Titolo già in uso");
            }
            quiz.setTitleQuiz(dto.titleQuiz());
        }

        if (dto.descriptionQuiz() != null) {
            quiz.setDescriptionQuiz(dto.descriptionQuiz());
        }

        Quiz updated = quizRepository.save(quiz);
        return mapToResponseDTO(updated);
    }

    // Cancella quiz
    public void deleteQuiz(UUID id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quiz con id " + id + " non trovato"));
        quizRepository.delete(quiz);
    }

    private QuizResponseDTO mapToResponseDTO(Quiz quiz) {
        List<UUID> questionIds = quiz.getQuestions() != null
                ? quiz.getQuestions().stream().map(Question::getId).toList()
                : List.of();

        return new QuizResponseDTO(
                quiz.getId(),
                quiz.getTitleQuiz(),
                quiz.getDescriptionQuiz(),
                questionIds
        );
    }
}
