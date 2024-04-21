package com.example.wrapped;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecArtistsActivity extends AppCompatActivity {

    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_artists);

        // Animated gradient background that can change from one color to another
        LinearLayout cardRecArtist1 = findViewById(R.id.cardRecArtist1);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) cardRecArtist1.getBackground();
        animationDrawable1.setEnterFadeDuration(1000);
        animationDrawable1.setExitFadeDuration(1000);
        animationDrawable1.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardRecArtist2 = findViewById(R.id.cardRecArtist2);
        AnimationDrawable animationDrawable2 = (AnimationDrawable) cardRecArtist2.getBackground();
        animationDrawable2.setEnterFadeDuration(1000);
        animationDrawable2.setExitFadeDuration(1000);
        animationDrawable2.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardRecArtist3 = findViewById(R.id.cardRecArtist3);
        AnimationDrawable animationDrawable3 = (AnimationDrawable) cardRecArtist3.getBackground();
        animationDrawable3.setEnterFadeDuration(1000);
        animationDrawable3.setExitFadeDuration(1000);
        animationDrawable3.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardRecArtist4 = findViewById(R.id.cardRecArtist4);
        AnimationDrawable animationDrawable4 = (AnimationDrawable) cardRecArtist4.getBackground();
        animationDrawable4.setEnterFadeDuration(1000);
        animationDrawable4.setExitFadeDuration(1000);
        animationDrawable4.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardRecArtist5 = findViewById(R.id.cardRecArtist5);
        AnimationDrawable animationDrawable5 = (AnimationDrawable) cardRecArtist5.getBackground();
        animationDrawable5.setEnterFadeDuration(1000);
        animationDrawable5.setExitFadeDuration(1000);
        animationDrawable5.start();


        String[] topArtists = getIntent().getStringArrayExtra("topArtists");
        String[] topArtistPics = getIntent().getStringArrayExtra("topArtistPics");
        String[] recArtists = getIntent().getStringArrayExtra("recArtists");
        String[] topGenres = getIntent().getStringArrayExtra("topGenres");
        String[] topTracks = getIntent().getStringArrayExtra("topTracks");
        String[] trackURIs = getIntent().getStringArrayExtra("trackURIs");

        mAccessToken = getIntent().getStringExtra("token");

        playTrack(trackURIs[3]);

        //TextView recArtistsTextView = findViewById(R.id.rec_artists_text_view);
        Button toGenreBtn = findViewById(R.id.to_genre_btn);

        toGenreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecArtistsActivity.this, TopGenresActivity.class);
                intent.putExtra("topArtists", topArtists);
                intent.putExtra("topArtistPics", topArtistPics);
                intent.putExtra("recArtists", recArtists);
                intent.putExtra("topGenres", topGenres);
                intent.putExtra("topTracks", topTracks);
                intent.putExtra("trackURIs",trackURIs);
                intent.putExtra("token",mAccessToken);
                startActivity(intent);
            }
        });

        TextView recArtist1 = findViewById(R.id.recArtist1);
        TextView recArtist2 = findViewById(R.id.recArtist2);
        TextView recArtist3 = findViewById(R.id.recArtist3);
        TextView recArtist4 = findViewById(R.id.recArtist4);
        TextView recArtist5 = findViewById(R.id.recArtist5);

        if (recArtists != null) {
            String joinedRecArtists = TextUtils.join(", ", recArtists);
            //recArtistsTextView.setText(joinedRecArtists);

            String[] words = joinedRecArtists.split(", ");

            recArtist1.setText("#1  " + words[0]);
            recArtist2.setText("#2  " + words[1]);
            recArtist3.setText("#3  " + words[2]);
            recArtist4.setText("#4  " + words[3]);
            recArtist5.setText("#5  " + words[4]);

        } else {
            String ans="No recommended artists data available";
            //recArtistsTextView.setText(ans);

            recArtist1.setText(ans);
            recArtist2.setText(ans);
            recArtist3.setText(ans);
            recArtist4.setText(ans);
            recArtist5.setText(ans);

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
                runOnUiThread(() -> Toast.makeText(RecArtistsActivity.this, "Failed to play track", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("Spotify", "Failed to command Spotify: " + response);
                    // Log response body for debugging
                    Log.e("Spotify", "Response body: " + response.body().string());
                    runOnUiThread(() -> Toast.makeText(RecArtistsActivity.this, "Failed to command Spotify", Toast.LENGTH_SHORT).show());
                } else {
                    Log.d("Spotify", "Successfully played track");
                    // Log response body for debugging
                    Log.d("Spotify", "Response body: " + response.body().string());
                }
            }
        });
    }
}