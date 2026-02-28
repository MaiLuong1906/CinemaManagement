/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai.memory;

import ai.Message;
import java.util.ArrayList;
import java.util.List;

public class Memory {

    private List<Message> conversation = new ArrayList<>();

    public void addMessage(String role, String content) {
        conversation.add(new Message(role, content));
    }

    public List<Message> getConversation() {
        return conversation;
    }

    public void clear() {
        conversation.clear();
    }
}
