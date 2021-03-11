package com.xl.job.service;

import com.xl.job.dao.JobDao;
import com.xl.job.pojo.JobInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobDao jobDao;

    @Transactional
    public void save(JobInfo jobInfo){
        JobInfo param = new JobInfo();
        param.setUrl(jobInfo.getUrl());
        param.setTime(jobInfo.getTime());
        List<JobInfo> all = this.findAll(param);
        if(all.size() == 0){
            this.jobDao.saveAndFlush(jobInfo);
        }
    }

    public List<JobInfo> findAll(JobInfo jobInfo){
        Example example = Example.of(jobInfo);

        return this.jobDao.findAll(example);
    }
}
