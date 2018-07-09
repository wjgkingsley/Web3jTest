package e.com.web3j.base;

import android.app.Activity;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class Config {
    static String TAG = "MainActivity";
    final private static String url = "http://192.168.2.11:8545";
    final public static Web3j web3j = new JsonRpc2_0Web3j(new HttpService(url));
//    public static  Web3j getWeb3j = new JsonRpc2_0Admin(new HttpService(url));

    /**
     * 调用文件管理器
     * @param activity
     * @param requestCode
     * @param model
     */
    public static void getLFilePicker(Activity activity,int requestCode,boolean model){
        new LFilePicker()
                .withActivity(activity)
                .withRequestCode(requestCode)
                .withMutilyMode(false)
                .withChooseMode(model)
                .withIconStyle(Constant.ICON_STYLE_GREEN)
                .withStartPath("/storage/emulated/0/Download")
                .start();
    }

    /**
     * 加载文件凭证
     * @param password
     * @param path
     * @return
     */
    public static Credentials getCredential(String password, String path)
            throws IOException, CipherException {
        Credentials credential = null;
        credential  = WalletUtils.loadCredentials(password, path);
        return credential;
    }
    /**
     * 得到可用的nonce
     * @param address
     * @return
     */
    public static BigInteger getNonce(String address){
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = Config.web3j.ethGetTransactionCount(
                    address, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ethGetTransactionCount.getTransactionCount();
    }



}
