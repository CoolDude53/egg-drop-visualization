package mc.mechanics.game;

public class Question
{
    private String question;
    private int answer;
    private boolean answered;

    public Question(String question, int answer)
    {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion()
    {
        return question;
    }

    public int getAnswer()
    {
        return answer;
    }

    public boolean isAnswered()
    {
        return answered;
    }

    public void setAnswered(boolean answered)
    {
        this.answered = answered;
    }
}
