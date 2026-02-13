/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package ai.action;

/**
 *
 * @author nguye
 */

import ai.goal.Goal;
public class DecisionEngine {

    public Action decide(Goal goal) {

        if (goal.isSeatCoverageAnalysis()) {
            return Action.ANALYZE_SEAT_COVERAGE;
        }

        if (goal.isRevenueAnalysis()) {
            return Action.ANALYZE_REVENUE;
        }

        return Action.UNKNOWN;
    }
}
