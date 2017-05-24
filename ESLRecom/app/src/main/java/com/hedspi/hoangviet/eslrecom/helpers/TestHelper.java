package com.hedspi.hoangviet.eslrecom.helpers;

import com.hedspi.hoangviet.eslrecom.commons.Preference;
import com.hedspi.hoangviet.eslrecom.models.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by viet on 4/13/17.
 */

public class TestHelper {
    public static int rightAnsweredQuestionNum = 0;
    public static int wrongAnsweredQuestionNum = 0;
    public static List<Question> questionBank;
    public static List<Question> usedQuestion = new ArrayList<>();
    public static String inputDifficulty;
    public static String currentUserProficiency;
    public static String currentTestDifficulty;
    public static String lowerAchivedDifficulty;
    public static String upperAchivedDifficulty;
    public static String currentType;
    public static double currentPR;
    public static int questionNum = 1;

    public static final double UBN = 0.02;
    public static final double LBM = 7;
    public static final int MAX_QUESTION = 5;

    public static final int STATUS_END = 1;
    public static final int STATUS_CONTINUE = 2;

    public static void initTextHelper(List<Question> data){
        questionBank = data;
    }

    public static void initNewLevelTest(String type, String difficulty){
        inputDifficulty = difficulty;
        currentTestDifficulty = inputDifficulty;
        currentType = type;
        rightAnsweredQuestionNum = 0;
        wrongAnsweredQuestionNum = 0;
        questionNum = 1;
        lowerAchivedDifficulty = null;
        upperAchivedDifficulty = null;
        usedQuestion = new ArrayList<>();
    }

    private static void resetProperties(){
        rightAnsweredQuestionNum = 0;
        wrongAnsweredQuestionNum = 0;
        currentPR = 0;
    }

    public static Question getQuestion(){
        return getQuestion(currentTestDifficulty, currentType);
    }

    public static int reevaluate(Boolean result){
        if (result){
            rightAnsweredQuestionNum++;
        }else{
            wrongAnsweredQuestionNum++;
        }

        questionNum++;

        caculatePR();

        if (currentPR <= UBN){
            return downSituation();
        }else {
            if (currentPR >= LBM){
                return upSituation();
            }else{
                return undetermineSituation();
            }
        }
    }

    private static int downSituation(){
        if (currentTestDifficulty.equals(Preference.BEGINNER) || lowerAchivedDifficulty !=null) {
            currentUserProficiency = currentTestDifficulty;
            return STATUS_END;
        }
        else{
            upperAchivedDifficulty = currentTestDifficulty;
            degradeLevel();
            resetProperties();
            return STATUS_CONTINUE;
        }
    }

    private static int upSituation(){
        if (currentTestDifficulty.equals(Preference.ADVANCE) || upperAchivedDifficulty!=null) {
            currentUserProficiency = currentTestDifficulty;
            return STATUS_END;
        }
        else{
            lowerAchivedDifficulty = currentTestDifficulty;
            upgradeLevel();
            resetProperties();
            return STATUS_CONTINUE;
        }
    }

    private static int undetermineSituation(){
        int totalQuestion = wrongAnsweredQuestionNum + rightAnsweredQuestionNum;
        if (totalQuestion >= MAX_QUESTION){
            if (currentTestDifficulty.equals(Preference.ADVANCE) && inputDifficulty.equals(Preference.ADVANCE)){
                degradeLevel();
                return STATUS_CONTINUE;
            }else{
                currentUserProficiency = currentTestDifficulty;
                return STATUS_END;
            }
        }else{
            return STATUS_CONTINUE;
        }
    }

    private static void degradeLevel(){
        switch (currentTestDifficulty){
            case Preference.BEGINNER:
            case Preference.ELEMENTARY:
                currentTestDifficulty = Preference.BEGINNER;
                break;
            case Preference.INTERMEDIATE:
                currentTestDifficulty = Preference.ELEMENTARY;
                break;
            case Preference.UPPER_INTERMEDIATE:
                currentTestDifficulty = Preference.INTERMEDIATE;
                break;
            case Preference.ADVANCE:
                currentTestDifficulty = Preference.UPPER_INTERMEDIATE;
                break;
        }
    }

    private static void upgradeLevel(){
        switch (currentTestDifficulty){
            case Preference.BEGINNER:
                currentTestDifficulty = Preference.ELEMENTARY;
                break;
            case Preference.ELEMENTARY:
                currentTestDifficulty = Preference.INTERMEDIATE;
                break;
            case Preference.INTERMEDIATE:
                currentTestDifficulty = Preference.UPPER_INTERMEDIATE;
                break;
            case Preference.UPPER_INTERMEDIATE:
            case Preference.ADVANCE:
                currentTestDifficulty = Preference.ADVANCE;
                break;
        }
    }

    public static String getCurrentUserProficiency() {
        return currentUserProficiency;
    }

    public static String getCurrentType() {
        return currentType;
    }

    public static int getQuestionNum() {
        return questionNum;
    }

    private static void caculatePR(){
        double upper = Math.pow(0.85,rightAnsweredQuestionNum)*Math.pow(0.15, wrongAnsweredQuestionNum);
        double bottom = Math.pow(0.35, rightAnsweredQuestionNum)*Math.pow(0.65, wrongAnsweredQuestionNum);
        currentPR = upper/bottom;
    }

    private static Question getQuestion(String difficulty, String type){
        List<Question> usableQuestion = new ArrayList<>();
        for(Question question:questionBank){
            if (question.getDifficulty().equals(difficulty) && question.getType().equals(type) && !usedQuestion.contains(question))
                usableQuestion.add(question);
        }

        Random random = new Random();
        int randomPosition = 0;
        if (usableQuestion.size()>1)
             randomPosition = random.nextInt(usableQuestion.size());
        Question randomQuestion = usableQuestion.get(randomPosition);
        usedQuestion.add(randomQuestion);

        return randomQuestion;
    }
}
