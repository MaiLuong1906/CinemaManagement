/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package ai.goal;

/**
 *
 * @author nguye
 */

public class Goal {

    private String description;

    public Goal(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRevenueAnalysis() {
        return description.toLowerCase().contains("doanh thu");
    }

    public boolean isSeatCoverageAnalysis() {
        return description.toLowerCase().contains("độ phủ")
                || description.toLowerCase().contains("ghế");
    }
}
