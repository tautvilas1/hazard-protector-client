package client.protector.hazard.hazardprotectorclient.controller.Search.Search;


import java.util.ArrayList;
import java.util.concurrent.Callable;

import client.protector.hazard.hazardprotectorclient.model.Articles.Article;


/**
 * Created by Tautvilas Simkus on 29/12/2016.
 */

/*
This class has the responsibility of finding
articles using particular keywords
 */

public class Finder implements Callable<ArrayList> {
    private String[] keywords;
    private ArrayList<Article> articlesList;
    public ArrayList<Article> articlesListFiltered = new ArrayList<Article>();


    public Finder(String[] keywords, ArrayList<Article> articlesList) {
        this.keywords = keywords;
        this.articlesList = articlesList;
    }

    @Override
    public ArrayList call() throws Exception
    {
        for(Article article: articlesList)
        {
            for(String keyword : keywords)
            {
                if(article.toString().toLowerCase().contains(keyword.toLowerCase()))
                {
                    articlesListFiltered.add(article);
                    break;
                }
            }
        }
        return articlesListFiltered;
    }
}
