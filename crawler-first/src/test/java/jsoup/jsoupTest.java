package jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class jsoupTest {

    static final String URL = "http://www.itcast.cn/";

    @Test
    public void testUrl(){
        try {
            Document document = Jsoup.parse(new URL(URL), 1000);
            String title = document.getElementsByTag("title").first().text();
            System.out.println(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void domTest(){
        try {
            Document document = Jsoup.parse(new URL(URL), 1000);
//            String title = document.getElementsByTag("title").first().text();
//            Element element = document.getElementById("wrapper");
            Element element = document.getElementsByAttribute("name").first();

            System.out.println(element.text());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelect(){
        try {
            Document document = Jsoup.parse(new URL(URL), 1000);
//            String title = document.getElementsByTag("title").first().text();
            Element element = document.select("span").first();//标签
//            Element element = document.select("#id").first();//ID
//            Element element = document.select(".class").first();//class
//            Element element = document.select("[attr]").first();//attribute
//            Element element = document.select("[attr=valve]").first();//attribute=value
//            Element element = document.select("h3#id]").first();//组合选择器
//            Element element = document.select("li.class_a]").first();//组合选择器

            System.out.println(element.text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
