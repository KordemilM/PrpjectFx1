package com.example.prpjectfx1.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
//lombok
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class PostCom {
    private Integer id;

    private String subject;

    private String content;

    private String userName;
    private int likes;

    private int views;

    private int parent;

    private Timestamp date;

    private String image;

    private boolean isAds;

    private ArrayList<PostCom> children;



}

