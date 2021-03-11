package com.xl.jd.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xl.jd.pojo.Item;
import com.xl.jd.service.ItemService;
import com.xl.jd.utils.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ItemTask {

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private ItemService itemService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Scheduled(fixedDelay = 100*1000)
    public void itemTask() throws Exception{
//        String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=%E6%89%8B%E6%9C%BA&cid2=653&cid3=655&s=113&click=0&page=";
        String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&pvid=b894f37dc6db412e9460fac65b90e33b&page=";
        for(int i = 1; i < 5; i=i+2){
            String html = httpUtils.doGetHtml(url + i);
            this.parse(html);
        }
        System.out.println("手机数据抓取完啦！！");
    }

    private void parse(String html) throws Exception {
        Document document = Jsoup.parse(html);

        Elements spuEles = document.select("div#J_goodsList > ul > li");
        for (Element spuEle : spuEles) {
            String attr = spuEle.attr("data-spu");
            long spu = Long.parseLong(attr.equals("")?"0":attr);

            Elements skuEles = spuEle.select("li.ps-item");
            for (Element skuEle : skuEles) {
                long sku = Long.parseLong(skuEle.select("[data-sku]").attr("data-sku"));

                Item item = new Item();
                item.setSku(sku);
                List<Item> list = this.itemService.findAll(item);
                if(list.size() > 0){
                    continue;
                }
                item.setSpu(spu);

                String itemUrl = "https://item.jd.com/"+sku+".html";
                item.setUrl(itemUrl);

                String priceJson = this.httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_" + sku);
                double price = MAPPER.readTree(priceJson).get(0).get("p").asDouble();
                item.setPrice(price);

                String itemInfo = this.httpUtils.doGetHtml(itemUrl);
                String title = Jsoup.parse(itemInfo).select("div.sku-name").first().text();
                item.setTitle(title);

                String imgUrl = "https:" + skuEle.select("img[data-sku]").first().attr("data-lazy-img");
                imgUrl = imgUrl.replace("/n7/","/n1/");
                // 下载图片
                this.httpUtils.doGetImage(imgUrl);
                item.setImg(imgUrl);

                item.setCreated(new Date());
                item.setUpdated(item.getCreated());

                this.itemService.saveItem(item);
            }
        }

    }
}
