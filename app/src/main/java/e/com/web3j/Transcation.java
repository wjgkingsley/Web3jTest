package e.com.web3j;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

import e.com.web3j.base.Config;

public class Transcation extends AppCompatActivity implements View.OnClickListener {
    String TAG = "MainActivity";
    final int REQUESTCODE_FROM_ACTIVITY = 2000;
    String path;
    String password;
    String toAddress;
    String value;
    EditText passEdit;
    EditText toEdit;
    EditText valueEdit;
    TextView accountText;
    BigInteger nonce;
    Button signTranscation;
    Credentials credentials;

//    public void putString(Context context, e.com.web3j.base.Transcation transcation) {
//        Intent intent = new Intent(context, Transcation.class);
//        intent.putExtra("sign", transcation);
//        context.startActivity(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcation);
        Button readAccount = findViewById(R.id.read_account_transcation);
        accountText = findViewById(R.id.your_account_transcation);//自己账户
        passEdit = findViewById(R.id.password_transcation);//密码
        toEdit = findViewById(R.id.toAddress_transcation);//对方账户
        valueEdit = findViewById(R.id.value_transcation);//金额
        signTranscation = findViewById(R.id.sign_transcation);
        signTranscation.setOnClickListener(this);
        readAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read_account_transcation:
                password = passEdit.getText().toString();
                Config.getLFilePicker(Transcation.this, REQUESTCODE_FROM_ACTIVITY, true);
                break;
            case R.id.sign_transcation:
                signTranscation();
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUESTCODE_FROM_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    readAccount(data);
                }
                break;
            default:
        }
    }

    /**
     * 读取文件
     * @param data
     */
    private void readAccount(Intent data){

        List<String> list = data.getStringArrayListExtra("paths");
        Toast.makeText(getApplicationContext(), "The selected path is:" + list,
                Toast.LENGTH_SHORT).show();
        path = list.get(0);
        Log.d(TAG, "path: " + path);

        if (path != null) {
            try {
                credentials = Config.getCredential(password, path);
                Log.d(TAG, "address: " + credentials.getAddress());
                accountText.setText(credentials.getAddress().substring(2));
                LinearLayout valuelayout = findViewById(R.id.value_transcation_linearlayout);
                LinearLayout tolayout = findViewById(R.id.toAddress_transcation_linearlayout);
                signTranscation.setVisibility(View.VISIBLE);
                tolayout.setVisibility(View.VISIBLE);
                valuelayout.setVisibility(View.VISIBLE);
                nonce = Config.getNonce(credentials.getAddress());
                Log.d(TAG, "nonce: " + nonce);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (CipherException e) {
                passEdit.setError("密码输入错误");
            }

        }
    }

    /**
     * 签名并且发送交易交易
     */
    private void signTranscation(){
        toAddress = toEdit.getText().toString();
        toAddress = credentials.getAddress();
        Log.d(TAG, "toAddress: " + toAddress);
        if (toAddress == null || "".equals(toAddress)){
            toEdit.setError("打入账户不能为空");
        }
        value = valueEdit.getText().toString();
        if (value == null || "".equals(value)){
            toEdit.setError("打入账户不能为空");
        }
        //TODO
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, new BigInteger("10"), new BigInteger("200000"), toAddress, new BigInteger(value));
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
        Log.d(TAG, "signedMessage: " + signedMessage.length);
        String hexValue = Numeric.toHexString(signedMessage);
        Log.d(TAG, "hexValue: " + hexValue);
        EthSendTransaction ethSendTransaction = null;
        TextView errorText = findViewById(R.id.error_transcation);
        try {
            ethSendTransaction = Config.web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            Log.d(TAG, "transactionHash: " + ethSendTransaction);
            String transactionHash = ethSendTransaction.getTransactionHash();
            if(transactionHash == null){
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("交易发送失败，请重试");
                Log.d(TAG, "transactionHash: " +  ethSendTransaction.getError().getMessage());
            }else{
                Log.d(TAG, "transactionHash: " + transactionHash);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("交易发送失败，请重试");
        } catch (ExecutionException e) {
            e.printStackTrace();
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("交易发送失败，请重试");
        }
    }

}
