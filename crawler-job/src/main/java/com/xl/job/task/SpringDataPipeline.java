package com.xl.job.task;

import com.xl.job.pojo.JobInfo;
import com.xl.job.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SpringDataPipeline  implements Pipeline {

    @Autowired
    private JobService jobService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取封装好的招聘详情对象
        JobInfo jobInfo = resultItems.get("jobInfo");
        //判断数据是否不为空
        if (jobInfo != null) {
            //如果不为空把数据保存到数据库中
            this.jobService.save(jobInfo);
        }
    }
}