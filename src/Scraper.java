import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.*;

public class Scraper {

    public Recipe scrape (String url) throws IOException {
        String returnString = "";
        String difficulty = "none";
        Document doc = Jsoup.connect(url).get();
        Elements el = doc.select("div.ingredients ul li");
        String[] author = doc.select("span.recipe-author a").text().split(" ");
        String author_first = author[0];
        String author_last = author[1];
        String title = doc.title();
        ArrayList<String> ingredients = new ArrayList<>();
        Integer time = 0;
        String[] yieldArr = doc.select("div.recipe-meta-item-body").get(2).text().split(" : ");
        String yield = yieldArr[1];
        for (Element text: el) {
            ingredients.add(text.text());
        }
        ArrayList<String> steps = new ArrayList<>();
        Elements stepList = doc.select("div.step p");
        //make sure text isn't empty
        for (Element step: stepList) {
            if (!step.ownText().isEmpty()) steps.add(step.text());
        }
        Recipe recipe = new Recipe(author_first, author_last, title, ingredients, steps, difficulty, time, time, time, yield);
        return recipe;
    }

}
