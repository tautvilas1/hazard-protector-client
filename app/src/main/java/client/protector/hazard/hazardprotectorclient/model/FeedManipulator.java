package client.protector.hazard.hazardprotectorclient.model;

import java.util.ArrayList;

import client.protector.hazard.hazardprotectorclient.model.Articles.Article;

/**
 * Created by Tautvilas on 29/01/2017.
 */

public class FeedManipulator {
    public ArrayList<Article> articlesList = new ArrayList<Article>();

    public FeedManipulator(ArrayList<Article> articlesList) {
        this.articlesList = articlesList;
    }

}
