/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package ai;

/**
 *
 * @author nguye
 */

import ai.action.Action;
import static ai.action.Action.ANALYZE_REVENUE;
import static ai.action.Action.ANALYZE_SEAT_COVERAGE;
import ai.action.DecisionEngine;
import ai.agent.FillRateEnvironment;
import ai.goal.Goal;
import ai.memory.Memory;

public class Agent {

    private FillRateEnvironment environment;
    private Memory memory;
    private DecisionEngine decisionEngine;
    private LLM llm;

    public Agent(FillRateEnvironment environment,
                     Memory memory,
                     DecisionEngine decisionEngine,
                     LLM llm) {
        this.environment = environment;
        this.memory = memory;
        this.decisionEngine = decisionEngine;
        this.llm = llm;
    }

    public String handle(String userInput) {

        memory.addMessage("user", userInput);

        Goal goal = new Goal(userInput);
        Action action = decisionEngine.decide(goal);

        String result;

        switch (action) {

            case ANALYZE_SEAT_COVERAGE -> {
                double coverage =
                        environment.getCurrentMonthSeatCoverage();

                result = "Độ phủ ghế tháng hiện tại là "
                        + String.format("%.2f%%", coverage);
            }

            case ANALYZE_REVENUE -> {
                result = "Chức năng phân tích doanh thu đang phát triển.";
            }

            default -> {
                result = "Tôi chưa hiểu yêu cầu.";
            }
        }

        memory.addMessage("assistant", result);

        return result;
    }
}
