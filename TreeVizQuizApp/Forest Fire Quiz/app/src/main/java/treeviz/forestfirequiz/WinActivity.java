package treeviz.forestfirequiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import info.hoang8f.widget.FButton;

public class WinActivity extends Activity {
    FButton playAgain;
    TextView winText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_activity);

        playAgain = findViewById(R.id.playAgainButton);
        winText = findViewById(R.id.winText);

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WinActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/FFFTusj.ttf");
        Typeface sf = Typeface.createFromAsset(getAssets(), "fonts/Sansation.ttf");
        winText.setTypeface(tf);
        playAgain.setTypeface(sf);

        playAgain.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.gold));

    }


}
