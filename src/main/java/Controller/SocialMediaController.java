package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.nullable;

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
        app.post("/login",this::loginHandler);
        app.post("/register", this::registerHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{id}", this::getMessageHandler);
        app.get("/accounts/{id}/messages", this::getAllMessagesFromUserHandler); 
        app.post("/messages", this::postMessageHandler);
        app.delete("/messages/{id}", this::deleteMessageHandler);
        app.put("/messages/{id}", this::updateMessageHandler);
        app.patch("/messages/{id}", this::patchMessageHandler);
       // app.start(8080);

        return app;
    }
    private void loginHandler(Context ctx)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account credentials = mapper.readValue(ctx.body(), Account.class);

        // Call service to verify login
        Account loggedIn = accountService.login(credentials.getUsername(), credentials.getPassword());

        if (loggedIn != null) {
            // return Account as JSON (including id, username, password)
            ctx.json(loggedIn);
        } else {
            // invalid username or password
            ctx.status(401).result("");  
        }  
   
    }
    private void registerHandler(Context ctx)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account register = accountService.registerAccount(account.username, account.password);
        if(register != null)
            ctx.json(register);
        else   
            ctx.status(400).result("");     

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
        Message deleted = messageService.deleteMessage(id);  
        if (deleted != null) {
            ctx.json(deleted);   // return deleted message
        } else {
            ctx.result("");      // empty body, still 200
        } 
    }
    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        ObjectMapper mapper = new ObjectMapper();

        JsonNode bodyNode = mapper.readTree(ctx.body());
        if (!bodyNode.has("message_text")) {
            ctx.status(400).result("message_text is required");
            return;
        }

        String newText = bodyNode.get("message_text").asText();
        if (newText == null || newText.trim().isEmpty() || newText.length() > 255) {
            ctx.status(400).result(""); 
            return;
        }       
        Message updated = messageService.updateMessage(id, newText);
        if (updated == null) {
            ctx.status(400).result("");
            return;
        }            
        ctx.status(200).json(updated);     
      
    }
    private void patchMessageHandler(Context ctx) throws JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        ObjectMapper mapper = new ObjectMapper();

        // Parse request body
        JsonNode bodyNode = mapper.readTree(ctx.body());
        if (!bodyNode.has("message_text")) {
            ctx.status(400).result("");
            return;
        }

        String newText = bodyNode.get("message_text").asText();
        if (newText == null || newText.trim().isEmpty() || newText.length() > 255) {
            ctx.status(400).result(""); 
            return;
        }     
            Message updated = messageService.updateMessage(id, newText);

            if (updated == null) {
                // Message ID does not exist
                ctx.status(400).result("");
                return;
            }
            // Return full updated message
            ctx.json(updated);
    }

}