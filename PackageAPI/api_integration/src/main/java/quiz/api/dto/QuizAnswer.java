package quiz.api.dto;

public class QuizAnswer {
    private String answer;
    private boolean isCorrect;

    public QuizAnswer(String answer, boolean isCorrect) {
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
