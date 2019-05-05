package treeviz.forestfirequiz;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import info.hoang8f.widget.FButton;

public class outOfTime extends AppCompatActivity {
    FButton playAgain;
    TextView timeUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_of_time);
        playAgain = findViewById(R.id.playAgainButton);
        timeUpText = findViewById(R.id.timeUpText);

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(outOfTime.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/FFFTusj.ttf");
        Typeface sf = Typeface.createFromAsset(getAssets(), "fonts/Sansation.ttf");

        timeUpText.setTypeface(tf);
        playAgain.setTypeface(sf);
        playAgain.setButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.brown));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
