package client.protector.hazard.hazardprotectorclient.controller.Search.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import client.protector.hazard.hazardprotectorclient.R;

public class LoadImage extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;

    public LoadImage(String url, ImageView imageView)
    {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params)
    {
        try
        {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    protected void onPostExecute(Bitmap result)
    {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
        if(hasImage(imageView) == false)
        {
            imageView.setImageResource(R.drawable.no_image);
        }
//        else
//        {
////            imageView.setlayo
//        }
    }

    private boolean hasImage(@NonNull ImageView view)
    {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable))
        {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
    }

}