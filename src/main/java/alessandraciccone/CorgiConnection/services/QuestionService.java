package alessandraciccone.CorgiConnection.services;

import alessandraciccone.CorgiConnection.entities.Answer;
import alessandraciccone.CorgiConnection.entities.Question;
import alessandraciccone.CorgiConnection.entities.Quiz;
import alessandraciccone.CorgiConnection.exceptions.BadRequestException;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
import alessandraciccone.CorgiConnection.payloads.QuestionDTO;
import alessandraciccone.CorgiConnection.payloads.QuestionResponseDTO;
import alessandraciccone.CorgiConnection.payloads.QuestionUpdateDTO;
import alessandraciccone.CorgiConnection.repositories.QuestionRepository;
import alessandraciccone.CorgiConnection.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    public List<QuestionResponseDTO> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            throw new NotFoundException("Nessuna domanda trovata");
        }
        return questions.stream().map(this::mapToResponseDTO).toList();
    }

    public QuestionResponseDTO getQuestionById(UUID id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Domanda con id " + id + " non trovata"));
        return mapToResponseDTO(question);
    }

    public List<QuestionResponseDTO> getQuestionsByQuiz(UUID quizId) {
        List<Question> questions = questionRepository.findByQuiz_Id(quizId);
        if (questions.isEmpty()) {
            throw new NotFoundException("Nessuna domanda trovata per il quiz con id: " + quizId);
        }
        return questions.stream().map(this::mapToResponseDTO).toList();
    }

    public QuestionResponseDTO createQuestion(QuestionDTO dto) {
        Quiz quiz = quizRepository.findById(dto.quizId())
                .orElseThrow(() -> new BadRequestException("Quiz non valido o inesistente"));

        Question question = new Question();
        question.setQuestionText(dto.questionText());
        question.setQuiz(quiz);
        question.setOrderNumber(dto.orderNumber());

        Question saved = questionRepository.save(question);
        return mapToResponseDTO(saved);
    }

    public QuestionResponseDTO updateQuestion(UUID id, QuestionUpdateDTO dto) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Domanda non trovata con id: " + id));

        if (dto.questionText() != null && !dto.questionText().isBlank())
            question.setQuestionText(dto.questionText());

        if (dto.quizId() != null) {
            Quiz newQuiz = quizRepository.findById(dto.quizId())
                    .orElseThrow(() -> new BadRequestException("Quiz non valido o inesistente"));
            question.setQuiz(newQuiz);
        }

        if (dto.orderNumber() != null)
            question.setOrderNumber(dto.orderNumber());

        Question updated = questionRepository.save(question);
        return mapToResponseDTO(updated);
    }

    public void deleteQuestion(UUID id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Domanda non trovata con id: " + id));
        questionRepository.delete(question);
    }

    private QuestionResponseDTO mapToResponseDTO(Question question) {
        List<UUID> answerIds = question.getAnswers() != null
                ? question.getAnswers().stream().map(Answer::getId).toList()
                : List.of();

        return new QuestionResponseDTO(
                question.getId(),
                question.getQuestionText(),
                question.getQuiz() != null ? question.getQuiz().getId() : null,
                answerIds,
                question.getOrderNumber()
        );
    }
}
