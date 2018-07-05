package e.com.web3j.base;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.Request;

import java.util.Arrays;

public class MyJsonRpc2_0Web3j extends JsonRpc2_0Web3j {

    public MyJsonRpc2_0Web3j(Web3jService web3jService) {
        super(web3jService);
    }
    public Request<?, PersonalNewAccount> personal_newAccount(String password) {
        return new Request<>(
                "personal_newAccount",
                Arrays.asList(password),
                web3jService,
                PersonalNewAccount.class);
    }
    public Request<?, PersonalNewAccount> personal_unlockAccount(String user,String password) {
        return new Request<>(
                "personal_unlockAccount",
                Arrays.asList(user,password),
                web3jService,
                PersonalNewAccount.class);
    }
}
