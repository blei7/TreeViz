package treeviz.forestfirequiz;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;


import info.hoang8f.widget.FButton;

public class GameActivity extends AppCompatActivity {
    FButton btnA, btnB, btnC, btnD;
    TextView questionTxt, quizTxt, timeTxt, resultTxt, pointTxt;
    QuestionHelper questionHelper;
    Question currQuestion;
    List<Question> list;
    int id = 0;
    int time = 20;
    int point = 0;
    CountDownTimer countDown;
    Typeface sType, fType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        //initialize variables
        questionTxt = findViewById(R.id.quizQuestion);
        btnA = findViewById(R.id.buttonA);
        btnB = findViewById(R.id.buttonB);
        btnC = findViewById(R.id.buttonC);
        btnD = findViewById(R.id.buttonD);
        quizTxt = findViewById(R.id.quizText);
        timeTxt = findViewById(R.id.timeText);
        resultTxt = findViewById(R.id.resultText);
        pointTxt = findViewById(R.id.pointText);

        //set fonts
        sType = Typeface.createFromAsset(getAssets(), "fonts/Sansation.ttf");
        fType = Typeface.createFromAsset(getAssets(), "fonts/FFFTusj.ttf");
        quizTxt.setTypeface(fType);
        questionTxt.setTypeface(sType);
        btnA.setTypeface(sType);
        btnB.setTypeface(sType);
        btnC.setTypeface(sType);
        btnD.setTypeface(sType);
        timeTxt.setTypeface(sType);
        resultTxt.setTypeface(fType);
        pointTxt.setTypeface(sType);

        resetColor();
        //initialize helper
        questionHelper = new QuestionHelper(this);
        questionHelper.getWritableDatabase();

        //check of questions are already added to table
        if (questionHelper.getAllQuestions().size() == 0) {
            questionHelper.questionsList();
        }

        //gets a list of questions
        list = questionHelper.getAllQuestions();

        //randomly order the questions
        Collections.shuffle(list);

        //get question, choices, and answer of that id
        currQuestion = list.get(id);

        //countDown
        countDown = new CountDownTimer(20000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeTxt.setText(time + "\"");
                time -= 1;

                //times up
                if (time == -1) {
                    resultTxt.setText(getString(R.string.timeup));
                    disableBtns();
                }
            }

            public void onFinish() {
                outOfTime();
            }
        }.start();
        updateQueAndChoices();
    }

    //update question/choices, timer, and points
    public void updateQueAndChoices() {
        questionTxt.setText(currQuestion.getQuestion());
        btnA.setText(currQuestion.getA());
        btnB.setText(currQuestion.getB());
        btnC.setText(currQuestion.getC());
        btnD.setText(currQuestion.getD());

        time = 20;

        //restart timer
        countDown.cancel();
        countDown.start();

        pointTxt.setText(String.valueOf(point));
        point++;
    }


    public void buttonA(View view) {
        //answer correct
        if (currQuestion.getA().equals(currQuestion.getAns())) {
            btnA.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            //did not finish all questions
            if (id < list.size() - 1) {
                disableBtns();
                showCorrect();
            }
            //finished all questions
            else {
                quizWon();
            }
        }
        //Wrong answer
        else {
            quizLostAndReplay();
        }
    }

    public void buttonB(View view) {
         if (currQuestion.getB().equals(currQuestion.getAns())) {
            btnB.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            if (id < list.size() - 1) {
                disableBtns();
                showCorrect();
            } else {
                quizWon();
            }
        } else {
            quizLostAndReplay();
        }
    }

    public void buttonC(View view) {
        if (currQuestion.getC().equals(currQuestion.getAns())) {
            btnC.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            if (id < list.size() - 1) {
                disableBtns();
                showCorrect();
            } else {
                quizWon();
            }
        } else {

            quizLostAndReplay();
        }
    }

    public void buttonD(View view) {
        if (currQuestion.getD().equals(currQuestion.getAns())) {
            btnD.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.lightGreen));
            if (id < list.size() - 1) {
                disableBtns();
                showCorrect();
            } else {
                quizWon();
            }
        } else {
            quizLostAndReplay();
        }
    }

    //This method will navigate from current activity to WinActivity
    public void quizWon() {
        Intent intent = new Intent(this, WinActivity.class);
        startActivity(intent);
        finish();
    }

    //Handles event when game is lost, directs to play again
    public void quizLostAndReplay() {
        Intent intent = new Intent(this, PlayAgain.class);
        startActivity(intent);
        finish();
    }

    //Handles event when time is up
    public void outOfTime() {
        Intent intent = new Intent(this, outOfTime.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        countDown.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        countDown.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDown.cancel();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
    }

    //Show answer correct dialogue
    public void showCorrect() {
        final Dialog dialogCorrectAnswer = new Dialog(GameActivity.this);
        dialogCorrectAnswer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialogCorrectAnswer.getWindow() != null) {
            ColorDrawable drawable = new ColorDrawable(Color.TRANSPARENT);
            dialogCorrectAnswer.getWindow().setBackgroundDrawable(drawable);
        }
        dialogCorrectAnswer.setContentView(R.layout.correct_activity);
        dialogCorrectAnswer.setCancelable(false);
        dialogCorrectAnswer.show();

        onPause();

        TextView correctTxt = dialogCorrectAnswer.findViewById(R.id.correctText);
        FButton btnNxt = dialogCorrectAnswer.findViewById(R.id.dialogNext);

        correctTxt.setTypeface(fType);
        btnNxt.setTypeface(fType);

        //set up the next question
        btnNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCorrectAnswer.dismiss();
                id++;
                currQuestion = list.get(id);
                updateQueAndChoices();
                resetColor();
                enableBtns();
            }
        });
    }


    //Set all button color to brown
    public void resetColor() {
        btnA.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.brown));
        btnB.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.brown));
        btnC.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.brown));
        btnD.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.brown));
    }

    //Enable all buttons
    public void disableBtns() {
        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnC.setEnabled(false);
        btnD.setEnabled(false);
    }

    //Disable all buttoms
    public void enableBtns() {
        btnA.setEnabled(true);
        btnB.setEnabled(true);
        btnC.setEnabled(true);
        btnD.setEnabled(true);
    }


}
