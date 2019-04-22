package com.team7.homelessstories;

import java.util.ArrayList;

public class BuildStory {
}

class Decision {
    private String decisionText;
    private ArrayList<Answer> answers;

    public Decision(String decisionText, ArrayList<Answer> answers){
        this.decisionText = decisionText;
        this.answers = answers;
    }

    public String getDecisionText(){
        return decisionText;
    }

    public ArrayList<Answer> getAnswers(){
        return answers;
    }
}

class Answer {
    private boolean real;
    private String answer;
    private String answerText;

    public Answer(boolean real, String answer, String answerText){
        this.real = real;
        this.answer = answer;
        // Maybe not necessary if real answer?
        this.answerText = answerText;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAnswerText() {
        return answerText;
    }

    public boolean getReal(){
        return real;
    }
}
