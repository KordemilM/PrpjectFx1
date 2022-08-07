package com.example.prpjectfx1;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class ForgotPassword {

    @FXML
    private LineChart<?,?> lineChart;



    @FXML
    private void lineChart(){
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data<String, Integer>("Monday",8));
        series.getData().add(new XYChart.Data<String, Integer>("Tuesday",12));
        series.getData().add(new XYChart.Data<String, Integer>("Wednesday",10));
        series.getData().add(new XYChart.Data<String, Integer>("Thursday",15));
        series.getData().add(new XYChart.Data<String, Integer>("Friday",12));
        series.getData().add(new XYChart.Data<String, Integer>("Saturday",8));
        series.getData().add(new XYChart.Data<String, Integer>("Sunday",5));
        lineChart.getData().addAll(series);
    }
}