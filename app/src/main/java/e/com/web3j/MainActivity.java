package e.com.web3j;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button createButton = findViewById(R.id.create_account_button_main);
        createButton.setOnClickListener(this);
        Button signButton = findViewById(R.id.sign_mainactivity);
        signButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.create_account_button_main:
                intent = new Intent(MainActivity.this, CreateNewAccount.class);
                startActivity(intent);
                break;
            case R.id.sign_mainactivity:
                intent = new Intent(MainActivity.this, Transcation.class);
                startActivity(intent);
                break;
            default:
        }
    }
}
