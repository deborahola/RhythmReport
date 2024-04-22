package com.example.wrapped;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SummaryActivity extends AppCompatActivity {

    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        String bgUrl = "https://img.freepik.com/free-vector/gradient-galaxy-background_23-2148983655.jpg"; // Replace with your image URL



        String[] topArtists = getIntent().getStringArrayExtra("topArtists");
        String[] topArtistPics = getIntent().getStringArrayExtra("topArtistPics");
        String[] recArtists = getIntent().getStringArrayExtra("recArtists");
        String[] topGenres = getIntent().getStringArrayExtra("topGenres");
        String[] topTracks = getIntent().getStringArrayExtra("topTracks");
        String[] trackURIs = getIntent().getStringArrayExtra("trackURIs");

        mAccessToken = getIntent().getStringExtra("token");

        playTrack(trackURIs[0]);

        TextView sumArtist1 = findViewById(R.id.sumArtist1);
        TextView sumArtist2 = findViewById(R.id.sumArtist2);
        TextView sumArtist3 = findViewById(R.id.sumArtist3);
        TextView sumArtist4 = findViewById(R.id.sumArtist4);
        TextView sumArtist5 = findViewById(R.id.sumArtist5);
        Button shareBtn = findViewById(R.id.shareBtn);
        Button homeBtn = findViewById(R.id.homeBtn);
        TextView sumTrack1 = findViewById(R.id.sumTrack1);
        TextView sumTrack2 = findViewById(R.id.sumTrack2);
        TextView sumTrack3 = findViewById(R.id.sumTrack3);
        TextView sumTrack4 = findViewById(R.id.sumTrack4);
        TextView sumTrack5 = findViewById(R.id.sumTrack5);
        TextView sumGenre1 = findViewById(R.id.sumGenre1);
        File imagePath = new File(getExternalFilesDir(null), "screenshot.png");
        SignedInUser.addWrapped(imagePath);

        ImageView firstPic = findViewById(R.id.firstPic);
        String imageUrl = topArtistPics[0];
        Picasso.get().load(imageUrl).into(firstPic);

        // desired max length 4 strings "topArtists[index]", "topTracks[index]", "topGenres[index]"
        int maxLength = 13;

        // set artist text with truncation if necessary
        sumArtist1.setText("  1. " + shortenText(topArtists[0], maxLength));
        sumArtist2.setText("  2. " + shortenText(topArtists[1], maxLength));
        sumArtist3.setText("  3. " + shortenText(topArtists[2], maxLength));
        sumArtist4.setText("  4. " + shortenText(topArtists[3], maxLength));
        sumArtist5.setText("  5. " + shortenText(topArtists[4], maxLength));

        // set track text with truncation if necessary
        sumTrack1.setText("  1. " + shortenText(topTracks[0], maxLength));
        sumTrack2.setText("  2. " + shortenText(topTracks[1], maxLength));
        sumTrack3.setText("  3. " + shortenText(topTracks[2], maxLength));
        sumTrack4.setText("  4. " + shortenText(topTracks[3], maxLength));
        sumTrack5.setText("  5. " + shortenText(topTracks[4], maxLength));

        // set genre text with truncation if necessary
        sumGenre1.setText("  " + shortenText(topGenres[0], maxLength));

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Capture the screen as a Bitmap
                View rootView = getWindow().getDecorView().getRootView();
                rootView.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
                rootView.setDrawingCacheEnabled(false);

// Save the Bitmap to a file
                try {
                    FileOutputStream fos = new FileOutputStream(imagePath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    Log.e("Error saving image", e.getMessage(), e);
                }

// Share the image file
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                Uri uri = FileProvider.getUriForFile(SummaryActivity.this, getPackageName() + ".fileprovider", imagePath);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent, "Share screenshot"));


            }
        });


        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TempActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // Helper function to shorten text with "..."
    public static String shortenText(String text, int maxLength) {
        if (text.length() > maxLength) {
            return text.substring(0, maxLength - 3) + "...";
        } else {
            return text;
        }
    }

    public void playTrack(String trackURI) {

        if (trackURI == null) {
            Toast.makeText(this, "Track URI missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        String playUrl = "https://api.spotify.com/v1/me/player/play";

        JSONObject payload = new JSONObject();
        try {
            JSONArray uris = new JSONArray();
            uris.put(trackURI);
            payload.put("uris", uris);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(payload.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(playUrl)
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Spotify", "Error playing track", e);
                runOnUiThread(() -> Toast.makeText(SummaryActivity.this, "Failed to play track", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("Spotify", "Failed to command Spotify: " + response);
                    // Log response body for debugging
                    Log.e("Spotify", "Response body: " + response.body().string());
                    runOnUiThread(() -> Toast.makeText(SummaryActivity.this, "Failed to command Spotify", Toast.LENGTH_SHORT).show());
                } else {
                    Log.d("Spotify", "Successfully played track");
                    // Log response body for debugging
                    Log.d("Spotify", "Response body: " + response.body().string());
                }
            }
        });
    }
}





//package com.example.wrapped;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//public class SummaryActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_summary);
//    }
//}