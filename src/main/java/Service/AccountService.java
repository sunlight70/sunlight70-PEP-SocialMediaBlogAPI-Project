package Service;
import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }
    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }
    public Account addAccount(Account account){
        return accountDAO.insertAccount(account);
    }
    
}
