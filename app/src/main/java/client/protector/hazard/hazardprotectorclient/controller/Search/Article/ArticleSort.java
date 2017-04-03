package client.protector.hazard.hazardprotectorclient.controller.Search.Article;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.model.Articles.Article;
import client.protector.hazard.hazardprotectorclient.model.User.User;

/**
 * Created by Tautvilas on 18/03/2017.
 */

public class ArticleSort
{
    private User user = App.user;
    
    public ArticleSort()
    {

    }

    public ArrayList<Article> sortByDate(ArrayList<Article> articlesList)
    {
        Collections.sort(articlesList, new Comparator<Article>()
        {
            @Override
            public int compare(Article o1, Article o2)
            {
                return o1.getDateAdded().compareTo(o2.getDateAdded());
            }
        });
        return articlesList;
    }

    public ArrayList<Article> sortByPreferences(ArrayList<Article> articlesList)
    {
        ArrayList<Article> refinedList = new ArrayList<Article>();
        ArrayList<String> preferences = user.getPreferences();
        System.out.println(preferences.toString());

        for(Article article : articlesList)
        {
            boolean articleAdded = false;
            for(String keyword : preferences)
            {
                if(article.toString().contains(keyword))
                {
                    refinedList.add(article);
                    articleAdded = true;
                    break;
                }
            }

            if(articleAdded)
            {
                continue;
            }
        }
        return refinedList;
    }
}
