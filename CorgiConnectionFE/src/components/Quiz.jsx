import { useEffect, useState } from "react";

const Quiz = () => {
  const [questions, setQuestions] = useState([]);
  const [answers, setAnswers] = useState({});
  const [allAnswers, setAllAnswers] = useState([]);
  const [result, setResult] = useState(null);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [showQuiz, setShowQuiz] = useState(false);

  const token = localStorage.getItem("token");

  useEffect(() => {
    if (!showQuiz) return;

    const fetchData = async () => {
      try {
        const baseUrl = import.meta.env.VITE_API_URL;

        const questionsRes = await fetch(`${baseUrl}/questions`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const questionsData = await questionsRes.json();

        const answersRes = await fetch(`${baseUrl}/answers`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const answersData = await answersRes.json();

        setQuestions(questionsData.filter((q) => q.answerIds.length > 0));
        setAllAnswers(answersData);
      } catch (error) {
        console.error("Errore fetch:", error);
      }
    };

    fetchData();
  }, [token, showQuiz]);

  const getAnswersForQuestion = (questionId) => {
    return (
      questions
        .find((q) => q.id === questionId)
        ?.answerIds.map((answerId) => allAnswers.find((a) => a.id === answerId))
        .filter(Boolean) || []
    );
  };

  const handleAnswerChange = (questionId, answerId) => {
    setAnswers((prev) => ({ ...prev, [questionId]: answerId }));
  };

  const handleSubmit = async () => {
    // Prepara DTO per backend
    const dto = {
      answers: Object.entries(answers).map(([questionId, answerId]) => ({
        questionId,
        answerId,
      })),
    };

    console.log("📤 DTO inviato:", dto);
    console.log("📤 JSON:", JSON.stringify(dto));

    try {
      const baseUrl = import.meta.env.VITE_API_URL;

      const res = await fetch(`${baseUrl}/quiz-results/submit`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(dto),
      });

      console.log("📥 Response status:", res.status);

      if (!res.ok) {
        const errorText = await res.text();
        console.error(" Errore backend:", errorText);
        alert(`Errore ${res.status}: ${errorText}`);
        return;
      }

      const resultData = await res.json();
      console.log("✅ Risultato ricevuto:", resultData);
      setResult(resultData);
    } catch (error) {
      console.error("❌ Errore submit:", error);
      alert("Errore durante l'invio del quiz: " + error.message);
    }
  };

  // --- PAGINA INIZIALE ---
  if (!showQuiz) {
    return (
      <div className="quiz-page-wrapper">
        <button className="quiz-start-btn" onClick={() => setShowQuiz(true)}>
          🐾 Inizia il Quiz Pet-Friendly
        </button>
      </div>
    );
  }

  if (questions.length === 0) return <p>Caricamento quiz...</p>;

  const currentQuestion = questions[currentIndex];
  const questionAnswers = getAnswersForQuestion(currentQuestion.id);

  return (
    <div className="quiz-page-wrapper">
      <div className="quiz-container">
        <h3>Quiz Pet-Friendly 🐾</h3>

        {/* DOMANDA */}
        <div className="quiz-question">
          <p>
            <strong>
              Domanda {currentIndex + 1}/{questions.length}:
            </strong>
          </p>
          <h4>{currentQuestion.questionText}</h4>

          <div className="quiz-answers">
            {questionAnswers.map((a) => (
              <label
                key={a.id}
                className={`quiz-radio-label ${
                  answers[currentQuestion.id] === a.id ? "selected" : ""
                }`}
              >
                <input
                  type="radio"
                  name={`question-${currentQuestion.id}`}
                  value={a.id}
                  checked={answers[currentQuestion.id] === a.id}
                  onChange={() => handleAnswerChange(currentQuestion.id, a.id)}
                />
                {a.answerText}
              </label>
            ))}
          </div>
        </div>

        {/* BOTTONI */}
        <div className="quiz-nav">
          <button
            disabled={currentIndex === 0}
            onClick={() => setCurrentIndex(currentIndex - 1)}
          >
            😎 Indietro
          </button>

          {currentIndex < questions.length - 1 ? (
            <button
              disabled={!answers[currentQuestion.id]}
              onClick={() => setCurrentIndex(currentIndex + 1)}
            >
              Avanti 😎
            </button>
          ) : (
            <button
              onClick={handleSubmit}
              disabled={Object.keys(answers).length !== questions.length}
            >
              ✔️ Invia Risposte
            </button>
          )}
        </div>

        {/* RISULTATO */}
        {result && (
          <div className="quiz-result">
            <h4>🎉 Risultato Quiz</h4>
            <p>
              <strong>Punteggio:</strong> {result.score} /{" "}
              {result.totalQuestions}
            </p>
            <p>
              <strong>Percentuale:</strong>{" "}
              {result.percentage === 100 || result.percentage === 0
                ? result.percentage + "%"
                : result.percentage.toFixed(1) + "%"}
            </p>

            {result.percentage === 100 && (
              <p style={{ color: "green", fontWeight: "bold" }}>
                🏆 Perfetto! Sei un esperto di animali!
              </p>
            )}
            {result.percentage >= 70 && result.percentage < 100 && (
              <p style={{ color: "orange", fontWeight: "bold" }}>
                👍 Ottimo lavoro!
              </p>
            )}
            {result.percentage < 70 && (
              <p style={{ color: "red", fontWeight: "bold" }}>
                💪 Continua a studiare!
              </p>
            )}

            <button
              className="quiz-start-btn"
              onClick={() => {
                setShowQuiz(false);
                setResult(null);
                setAnswers({});
                setCurrentIndex(0);
              }}
            >
              🐣 Ricomincia
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Quiz;
