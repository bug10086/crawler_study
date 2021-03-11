package com.xl.job.dao;

import com.xl.job.pojo.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDao extends JpaRepository<JobInfo,Long> {
}
