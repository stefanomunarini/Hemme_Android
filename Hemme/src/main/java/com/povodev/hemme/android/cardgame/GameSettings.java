package com.povodev.hemme.android.cardgame;

import com.povodev.hemme.android.bean.Result;

/**
 * Created by Stefano on 15/04/14.
 */
public class GameSettings {

    private Result result;
    private int size;

    public GameSettings(){
        result = new Result();
    }

    private final static String gradeEasy = "Facile (4)";
    private final static String gradeEasyMedium = "Facile (6)";
    private final static String gradeMedium = "Medio (8)";
    private final static String gradeMediumHard = "Medio (10)";
    private final static String gradeHard = "Difficile (12)";

    // Get the size of the game-map for the choosen difficulty
    public void setSize(int difficulty) {
        if (difficulty==1){
            setGrade(gradeEasy);
            size = 4;
        } else if (difficulty==2){
            setGrade(gradeEasyMedium);
            size = 6;
        } else if (difficulty==3){
            setGrade(gradeMedium);
            size = 8;
        } else if (difficulty==4){
            setGrade(gradeMediumHard);
            size = 10;
        } else {
            setGrade(gradeHard);
            size = 12;
        }
    }

    /*
     * Set grade for the bean Result
     */
    private void setGrade(String grade) {
        result.setGrade(grade);
    }

    /*
     * Set timing for the bean Result
     */
    public void setTiming(GameTimer gameTimer) {
        result.setTime(gameTimer.getTiming());
    }

    public int getSize(){
        return size;
    }

    public Result getResult(){
        return result;
    }

}
