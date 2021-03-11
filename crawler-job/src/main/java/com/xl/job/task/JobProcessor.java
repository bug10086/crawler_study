package com.xl.job.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xl.job.pojo.JobInfo;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JobProcessor implements PageProcessor {

    @Autowired
    private SpringDataPipeline springDataPipeline;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Integer pageNum = 1;

    private String url = "https://search.51job.com/list/000000,000000,0000,00,9,99,java,2,"+pageNum+".html?lang=c&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&ord_field=0&dibiaoid=0&line=&welfare=";

    @SneakyThrows
    @Override
    public void process(Page page) {
        //解析页面，获取url地址
        String script = page.getHtml().css("script").regex("window.__SEARCH_RESULT__.*").toString();
        List<String> job_hrefs = new ArrayList<>();
        if(script != null) {
            script = script.substring(27, script.length() - 9);
            job_hrefs = MAPPER.readTree(script).findValue("engine_search_result").findValuesAsText("job_href");
        }

        if(job_hrefs.size() == 0){
            //当前为详情页
            this.saveJobInfo(page);
        }else {
            for (String job_href : job_hrefs) {
                page.addTargetRequest(job_href);
            }
        }
        if(pageNum < 2){
            pageNum++;
        }
        page.addTargetRequest(url);
    }

    private void saveJobInfo(Page page) {
        //创建招聘详情对象
        JobInfo jobInfo  = new JobInfo();
        //解析页面
        Html html = page.getHtml();
        //获取数据，封装到对象中
        jobInfo.setCompanyName(html.css("p.cname > a","text").toString());
        String tmp = html.css("p.ltype","title").toString();
//        String[] strings = tmp.split("  |  ");
        jobInfo.setCompanyAddr(tmp);
        jobInfo.setCompanyInfo(Jsoup.parse(html.css("div.tmsg").toString()).text());
        jobInfo.setJobName(html.css("div.in > div.cn > h1","text").toString());
        jobInfo.setJobAddr(html.css("div.bmsg > p.fp","text").toString());
        jobInfo.setJobInfo(Jsoup.parse(html.css("div.job_msg").toString()).text());
        jobInfo.setUrl(page.getUrl().toString());
        //获取薪资
        Integer[] salary = MathSalary.getSalary(html.css("div.cn strong", "text").toString());
        jobInfo.setSalaryMin(salary[0]);
        jobInfo.setSalaryMax(salary[1]);
        //获取发布时间
        String time = html.css("p.ltype","title").regex("\\d\\d-\\d\\d发布").toString();
        jobInfo.setTime(time.substring(0,time.length()-2));
        //把结果保存起来
        page.putField("jobInfo",jobInfo);
    }

    private Site site = Site.me()
            .setCharset("gbk")
            .setRetrySleepTime(3000)
            .setTimeOut(10*1000)
            .setRetryTimes(3)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
    @Override
    public Site getSite() {
        return site;
    }

    @Scheduled(initialDelay = 1000,fixedDelay = 100*1000)
    public void process() {
        Spider.create(new JobProcessor())
                .addUrl(url)
                .thread(5)
                .addPipeline(this.springDataPipeline)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .run();
    }
}
