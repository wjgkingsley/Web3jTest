package e.com.web3j;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button createButton = findViewById(R.id.create_account_start);
        createButton.setOnClickListener(this);
        Button signButton = findViewById(R.id.sign_start);
        signButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.create_account_start) {
            Intent intent = new Intent(StartActivity.this, CreateNewAccount.class);
            startActivity(intent);

        } else if (i == R.id.sign_start) {
            Intent intent = new Intent(StartActivity.this, Transcation.class);
            startActivity(intent);

        } else {
        }
    }
}

