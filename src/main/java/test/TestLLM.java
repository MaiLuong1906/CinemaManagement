package test;

import ai.LLM;
import ai.Message;
import java.util.List;

public class TestLLM {
    public static void main(String[] args) {
        try {
            LLM llm = new LLM();
            String response = llm.generate(List.of(
                new Message("user", "Say hello")
            ));
            System.out.println("LLM Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
