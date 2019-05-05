package treeviz.forestfirequiz;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import info.hoang8f.widget.FButton;

public class MainScreen extends AppCompatActivity {
    FButton play,quit;
    TextView quizName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        //the below method will initialize views
        initViews();

        play.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.brown));
        quit.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.brown));
        //PlayGame button - it will take you to the GameActivity
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Quit button - This will quit the game
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {
        //initialize views here
        play = findViewById(R.id.playGame);
        quit = findViewById(R.id.quit);
        quizName = findViewById(R.id.fQ);

        Typeface ff = Typeface.createFromAsset(getAssets(), "fonts/FFFTusj.ttf");
        Typeface sf = Typeface.createFromAsset(getAssets(),"fonts/Sansation.ttf");
        play.setTypeface(sf);
        quit.setTypeface(sf);
        quizName.setTypeface(ff);
    }
}
