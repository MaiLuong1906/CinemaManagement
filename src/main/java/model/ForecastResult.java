package model;

import java.util.List;

public class ForecastResult {
    private List<ForecastDTO> dailyData;
    private String analysis;

    public ForecastResult() {}

    public ForecastResult(List<ForecastDTO> dailyData, String analysis) {
        this.dailyData = dailyData;
        this.analysis = analysis;
    }

    public List<ForecastDTO> getDailyData() {
        return dailyData;
    }

    public void setDailyData(List<ForecastDTO> dailyData) {
        this.dailyData = dailyData;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
}
