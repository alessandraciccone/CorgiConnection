package alessandraciccone.CorgiConnection.payloads;

import java.util.UUID;

public record AnswerSubmissionDTO(
        UUID questionId,
        UUID answerId
) {}
// questo record serve a inviare l'id della domanda e l'id della risposta scelta dall'utente quando risponde a una domanda del quiz //