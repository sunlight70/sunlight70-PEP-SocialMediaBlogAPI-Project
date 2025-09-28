package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
     AccountService accountService;
     MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/accounts", this::getAllAccountsHandler);
        app.post("/accounts", this::postAccountHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{id}", this::getMessageHandler);
        app.get("/accounts/{id}/messages", this::getAllMessagesFromUserHandler); 
        app.post("/messages", this::postMessageHandler);
        app.delete("/messages/{id}", this::deleteMessageHandler);
       // app.start(8080);

        return app;
    }
    private void postAccountHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));            
        }else{
            ctx.status(400);
        }
    }
    private void getAllAccountsHandler(Context ctx) {
        List<Account> accounts = accountService.getAllAccounts();
        ctx.json(accounts);
    }
   private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        
       if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
        ctx.status(400).result("");  
        return;
       }
       Message addedMessage = messageService.addMessage(message);
        if(addedMessage!=null){
            ctx.json(mapper.writeValueAsString(addedMessage));
        }else{
            ctx.status(400).result("");
        }
    }
    public void getAllMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }
    private void getAllMessagesFromUserHandler(Context ctx) {
       int userId = Integer.parseInt(ctx.pathParam("id"));
       List<Message> messages = messageService.getAllMessagesFromUser(userId);

        ctx.json(messages); 
   }
    private void getMessageHandler(Context ctx)
    {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Message gotmessage = messageService.getMessage(id);
        if (gotmessage != null) {
            ctx.json(gotmessage);   
        } else {
            ctx.result("");      
        } 
    }
    private void deleteMessageHandler(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Message deleted = messageService.deleteMessage(id);  // note spelling: deletMessage
        if (deleted != null) {
            ctx.json(deleted);   // return deleted message
        } else {
            ctx.result("");      // empty body, still 200
    } 
    }
    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
   
    // private void exampleHandler(Context context) {
     //   context.json("sample text");
    //}

}