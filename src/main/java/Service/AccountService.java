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
    public Account login(String username, String password){
            
        Account existing  = accountDAO.getAccountByUserName(username);
        if (existing != null && existing.getPassword().equals(password)) {
          return existing; 
        }
       return null;    
    }
    public Account registerAccount(String username, String password){
        if (username == null || username.isBlank()) {
            return null;
        }
        if (password == null || password.length() < 4) {
            return null;
        }
        if (accountDAO.getAccountByUserName(username) != null) {
            return null; 
        }
        return addAccount(new Account(username, password));
 }
}


    

