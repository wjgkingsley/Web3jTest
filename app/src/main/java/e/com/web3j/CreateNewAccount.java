package e.com.web3j;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class CreateNewAccount extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private TextView loadTextView;
    private String path = "/data/data/e.com.web3j/files";
    private String realPath = null;
    private String fileName = null;
    final int REQUESTCODE_FROM_ACTIVITY = 1000;
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button createAccount = findViewById(R.id.create_account);
        editText = findViewById(R.id.create_account_password);
        loadTextView = findViewById(R.id.load_account_text_view);
        Button loadAccount = findViewById(R.id.load_account_button);
        createAccount.setOnClickListener(this);
        loadAccount.setOnClickListener(this);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_account:
                String password = editText.getText().toString().trim();
                createNewAccount(path, password);
                break;
            case R.id.load_account_button:
                new LFilePicker()
                        .withActivity(CreateNewAccount.this)
                        .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                        .withMutilyMode(false)
                        .withChooseMode(false)
                        .withIconStyle(Constant.ICON_STYLE_GREEN)
                        .withStartPath("/storage/emulated/0/Download")
                        .start();
                break;
            default:
        }
    }

    /**
     * 创建文件
     *
     * @param path
     * @param password
     */
    private void createNewAccount(String path, String password) {

        if (password == null || "".equals(password)) {
            editText.setError("请输入密码");
        } else if (password.length() < 6) {
            editText.setError("密码长度不能少于6位");
        } else {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                fileName = WalletUtils.generateLightNewWalletFile(password, file);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "账户创建失败", Toast.LENGTH_SHORT).show();
            }
            if (fileName != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                Credentials credentials = null;
                try {
                    credentials = WalletUtils.loadCredentials(password, path + "/" + fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "账户创建失败", Toast.LENGTH_SHORT).show();
                }
                Log.d("", "onClick: " + credentials.getAddress());
                editText.clearFocus();
                LinearLayout linearLayout = findViewById(R.id.load_account_linearlayout);
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "账户创建失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 从pathName复制到realPath
     *
     * @param pathName
     * @param realPath
     */
    @SuppressLint("ResourceAsColor")
    private void loadNewAccount(String pathName, String realPath) {
        Log.d(TAG, "onClick: =========================" + path + "/" + fileName);
        Log.d(TAG, "onClick: =========================" + realPath);
        String[] strings = fileName.split("--");
        String jsonName = strings[strings.length - 1];
        Log.d(TAG, "onClick: " + jsonName);
        String json = load(pathName);
        save(json, realPath, jsonName);
        loadTextView.setText("文件下载成功");
        loadTextView.setTextColor(R.color.colorRed);
    }

    /**
     *
     * @param json
     * @param realPath
     * @param jsonName
     */
    private void save(String json, String realPath, String jsonName) {
        File file = new File(realPath + "/" +jsonName);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String load(String pathName) {
        BufferedReader reader = null;
        StringBuilder builder = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pathName))));
            builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                Log.d(TAG, "load: " + line);
                builder.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUESTCODE_FROM_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    realPath = data.getStringExtra("path");
                    Toast.makeText(getApplicationContext(), "The selected path is:" + realPath, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: =========================" + realPath);
                    if (realPath != null) {
                        loadNewAccount(path + "/" + fileName, realPath);
                    } else {
                        Toast.makeText(this, "文件夹选择失败", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
        }

    }


}
