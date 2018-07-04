package e.com.web3j;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import e.com.web3j.base.Config;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button createAccount = findViewById(R.id.create_account);
        editText = findViewById(R.id.create_account_password);
        createAccount.setOnClickListener(this);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_account:
                String password = editText.getText().toString().trim();
                if (password == null || "".equals(password)){
                    editText.setError("请输入密码");
//                    Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,password,Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
