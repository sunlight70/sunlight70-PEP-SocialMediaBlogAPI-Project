package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }
    public Message addMessage(Message message){
        return messageDAO.insertMessage(message);
    }
    public Message deleteMessage(int id){
        return messageDAO.deleteMessage(id);
    }
    public Message getMessage(int id){
        return messageDAO.getMessageById(id);
    }
    public List<Message> getAllMessagesFromUser(int userId) {
      return messageDAO.getAllMessagesByUser(userId);
    }
    
}
