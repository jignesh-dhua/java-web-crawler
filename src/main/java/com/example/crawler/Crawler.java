package com.example.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler implements Runnable{

    protected BlockingQueue<String> queue = null;

    public Crawler(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public List<String> crawl(String url) throws IOException {

        List<String> pageLinks = new ArrayList<>();

        // 1. Fetch the HTML code
        Document document = Jsoup.connect(url).get();
        // 2. Parse the HTML to extract links to other URLs
        Elements linksOnPage = document.select("a[href]");

        // 3. Add each url in List.
        for (Element page : linksOnPage) {
            pageLinks.add(page.attr("abs:href"));
        }
        return pageLinks;
    }

    @Override
    public void run() {
       
        try {
            String url = queue.take();

            List<String> links= crawl(url);

            for (String link : links) {
                queue.add(link);
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        
    }
}
