package alessandraciccone.CorgiConnection.services;

import alessandraciccone.CorgiConnection.entities.Quiz;
import alessandraciccone.CorgiConnection.entities.QuizResult;
import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.payloads.QuizResultDTO;
import alessandraciccone.CorgiConnection.payloads.QuizResultResponseDTO;
import alessandraciccone.CorgiConnection.payloads.QuizResultUpdateDTO;
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

//nuovo quiz result
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
//restituisco i tisultati
public List<QuizResultResponseDTO> getAllResults() {
    return quizResultRepository.findAll().stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
}
    public QuizResultResponseDTO getResultById(UUID id) {
        QuizResult result = quizResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Risultato del quiz non trovato con ID: " + id));

        return mapToResponseDTO(result);
    }

//aggiorno ounteggio e domande

    public QuizResultResponseDTO updateResult(UUID id, QuizResultUpdateDTO dto) {
        QuizResult result = quizResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizResult non trovato con ID: " + id));

        result.setScore(dto.score());
        result.setTotalQuestions(dto.totalQuestions());

        QuizResult updated = quizResultRepository.save(result);
        return mapToResponseDTO(updated);
    }

//elimino risultato
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
