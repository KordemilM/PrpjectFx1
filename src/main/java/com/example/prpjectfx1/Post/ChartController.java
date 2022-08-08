package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.entity.PostCom;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.sql.SQLException;
import java.sql.Date;
import java.util.LinkedHashMap;

public class ChartController {
    @FXML
    private LineChart<Date, Number> Chart;

    public void chartLikes(PostCom postCom) throws SQLException, ClassNotFoundException {
        LinkedHashMap<java.sql.Date, Integer> likes = AppContext.getPostComRepos().getLikes(postCom, AppContext.getConnection());
        XYChart.Series<Date, Number> series = new XYChart.Series<>();
        for(Date date : likes.keySet()){
            series.getData().add(new XYChart.Data<>(date, likes.get(date)));
        }
        Chart.getData().add(series);
    }

    public void chartViews(PostCom postCom) throws SQLException, ClassNotFoundException {
        LinkedHashMap<java.sql.Date, Integer> views = AppContext.getPostComRepos().getViews(postCom, AppContext.getConnection());
        XYChart.Series<Date, Number> series = new XYChart.Series<>();
        for(Date date : views.keySet()){
            series.getData().add(new XYChart.Data<>(date, views.get(date)));
        }
        Chart.getData().add(series);
    }

}

