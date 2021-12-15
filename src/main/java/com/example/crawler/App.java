package com.example.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class App {

    private Set<String> links = new HashSet<>();

    public static final BlockingQueue<String> queue = new ArrayBlockingQueue<>(1024);
    
    private Crawler crawler = new Crawler();

    public void getPageLinks(String URL) {
        // 4. Check if you have already crawled the URLs
        // (we are intentionally not checking for duplicate content in this example)
        if (!links.contains(URL)) {
            try {
                // 4. (i) If not add it to the index
                if (links.add(URL)) {
                    System.out.println(URL);
                }

                List<String> links = crawler.crawl(URL);

                for (String link : links) {
                    getPageLinks(link);
                }
                
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // 1. Pick a URL from the frontier
        new BasicWebCrawler().getPageLinks("http://google.com/");
    }
}
