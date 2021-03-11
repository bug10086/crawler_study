package com.xl.webMagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;

public class testProcess implements PageProcessor {
    public void process(Page page) {
        page.putField("div1",page.getHtml().css("div.w > ul > li.fore4").all());
        page.putField("div2",page.getHtml().css("div.w > ul > li").regex(".*我的.*").all());

        page.addTargetRequests(page.getHtml().css("div.w").links().regex(".*com").all());
    }

    private Site site = Site.me()
            .setCharset("utf8")
            .setRetrySleepTime(3000)
            .setSleepTime(3)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider spider = Spider.create(new testProcess())
                .addUrl("https://kuaibao.jd.com/")
                .addPipeline(new FilePipeline(""))
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))//设置布隆过滤去重
                .thread(5);

        Scheduler scheduler = spider.getScheduler();

        spider.run();
    }
}
