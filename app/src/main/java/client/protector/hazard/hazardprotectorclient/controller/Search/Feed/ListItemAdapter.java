package client.protector.hazard.hazardprotectorclient.controller.Search.Feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Image.LoadImage;
import client.protector.hazard.hazardprotectorclient.model.Articles.Article;

/**
 * Created by Tautvilas on 12/03/2017.
 */

public class ListItemAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Article> articlesList;
    private static LayoutInflater inflater = null;

    public ListItemAdapter(Context context, ArrayList<Article> articlesList)
    {
        this.articlesList = articlesList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount()
    {
        return articlesList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public class Holder
    {
        TextView description,title;
        ImageView image;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.article_list_item, null);

        holder.image =(ImageView) rowView.findViewById(R.id.image);
        holder.title =(TextView) rowView.findViewById(R.id.title);

        holder.title.setText(articlesList.get(position).getTitle());
        if(articlesList.get(position).getThumbnail() != "")
        {
            LoadImage loadImage = new LoadImage(articlesList.get(position).getThumbnail(),holder.image);
            loadImage.execute();
        }
        else
        {
            holder.image.setImageResource(R.drawable.no_image);
        }
        rowView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+articlesList.get(position).getTitle(), Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }
}
