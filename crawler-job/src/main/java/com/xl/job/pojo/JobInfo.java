package com.xl.job.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class JobInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String CompanyName;

    private String companyAddr;

    private String companyInfo;

    private String jobName;

    private String jobAddr;

    private String jobInfo;

    private Integer salaryMin;

    private Integer salaryMax;

    private String url;

    private String time;

}
