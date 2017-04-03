package client.protector.hazard.hazardprotectorclient.controller.Search.Search;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import client.protector.hazard.hazardprotectorclient.model.Articles.Article;

/**
 * Created by Tautvilas on 03/04/2017.
 */

public class FindArticlesById implements Callable<ArrayList<Article>>
{
    private ArrayList<Article> articlesList;
    private ArrayList<String> articleIds;
    private ArrayList<Article> articlesListFiltered = new ArrayList<>();
    public FindArticlesById(ArrayList<Article> articlesList,ArrayList<String> articleIds)
    {
        this.articleIds = articleIds;
        this.articlesList = articlesList;
    }
    @Override
    public ArrayList<Article> call()
    {
        for(Article article : articlesList)
        {
            for(String id : articleIds)
            {
                if(String.valueOf(article.getId()).equals(id))
                {
                    articlesListFiltered.add(article);
                }
            }
        }
        return articlesListFiltered;
    }
}
