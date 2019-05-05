package treeviz.forestfirequiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import info.hoang8f.widget.FButton;


public class PlayAgain extends Activity {

    FButton playAgain;
    TextView wrongAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_again);
       //Initialize
        playAgain = findViewById(R.id.playAgainButton);
        wrongAnswer = findViewById(R.id.wrongAns);

        playAgain.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.brown));

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayAgain.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Setting typefaces for textview and button - this will give stylish fonts on textview and button
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/FFFTusj.ttf");
        playAgain.setTypeface(typeface);
        wrongAnswer.setTypeface(typeface);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
