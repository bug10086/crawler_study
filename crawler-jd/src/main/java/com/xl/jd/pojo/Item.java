package com.xl.jd.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "jd_item")
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long spu;

    private Long sku;

    private String title;

    private Double price;

    private String img;

    private String url;

    private Date created;

    private Date updated;


}
