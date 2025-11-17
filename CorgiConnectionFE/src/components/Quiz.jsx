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
        const questionsRes = await fetch("http://localhost:8888/questions", {
          headers: { Authorization: `Bearer ${token}` },
        });
        const questionsData = await questionsRes.json();

        const answersRes = await fetch("http://localhost:8888/answers", {
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
    const dto = {
      answers: Object.entries(answers).map(([questionId, answerId]) => ({
        questionId,
        answerId,
      })),
    };

    try {
      const res = await fetch("http://localhost:8888/quiz-results", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(dto),
      });

      if (!res.ok) throw new Error(`HTTP ${res.status}`);

      setResult(await res.json());
    } catch (error) {
      console.error("Errore submit:", error);
      alert("Errore durante l'invio del quiz");
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
            <h4>Risultato Quiz</h4>
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
