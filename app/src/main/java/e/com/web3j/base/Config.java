package e.com.web3j.base;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.JsonRpc2_0Admin;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.http.HttpService;

public class Config {

    final private static String url = "http://192.168.2.37:8545";
    final public static Web3j web3j = new JsonRpc2_0Web3j(new HttpService(url));
    public static  Web3j getWeb3j = new JsonRpc2_0Admin(new HttpService(url));


}
