package client.protector.hazard.hazardprotectorclient.controller.Search.Article;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import client.protector.hazard.hazardprotectorclient.model.Articles.Article;

/**
 * Created by Tautvilas on 18/03/2017.
 */

public class ArticleSort
{
    private ArrayList<Article> articlesList;
    public ArticleSort(ArrayList<Article> articlesList)
    {
        this.articlesList = articlesList;
    }

    public ArrayList<Article> sortByDate()
    {
        Collections.sort(articlesList, new Comparator<Article>()
        {
            @Override
            public int compare(Article o1, Article o2)
            {
                return o1.getPublishDate().compareTo(o2.getPublishDate());
            }
        });
        return articlesList;
    }
}
