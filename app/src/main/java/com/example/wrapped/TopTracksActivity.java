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

public class TopTracksActivity extends AppCompatActivity {

    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);


        // Animated gradient background that can change from one color to another
        LinearLayout cardTopTrack1 = findViewById(R.id.cardTopTrack1);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) cardTopTrack1.getBackground();
        animationDrawable1.setEnterFadeDuration(1000);
        animationDrawable1.setExitFadeDuration(1000);
        animationDrawable1.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardTopTrack2 = findViewById(R.id.cardTopTrack2);
        AnimationDrawable animationDrawable2 = (AnimationDrawable) cardTopTrack2.getBackground();
        animationDrawable2.setEnterFadeDuration(1000);
        animationDrawable2.setExitFadeDuration(1000);
        animationDrawable2.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardTopTrack3 = findViewById(R.id.cardTopTrack3);
        AnimationDrawable animationDrawable3 = (AnimationDrawable) cardTopTrack3.getBackground();
        animationDrawable3.setEnterFadeDuration(1000);
        animationDrawable3.setExitFadeDuration(1000);
        animationDrawable3.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardTopTrack4 = findViewById(R.id.cardTopTrack4);
        AnimationDrawable animationDrawable4 = (AnimationDrawable) cardTopTrack4.getBackground();
        animationDrawable4.setEnterFadeDuration(1000);
        animationDrawable4.setExitFadeDuration(1000);
        animationDrawable4.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardTopTrack5 = findViewById(R.id.cardTopTrack5);
        AnimationDrawable animationDrawable5 = (AnimationDrawable) cardTopTrack5.getBackground();
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

        playTrack(trackURIs[1]);

        //TextView topTracksTextView = findViewById(R.id.top_tracks_text_view);
        Button toSummaryBtn = findViewById(R.id.to_summary_btn);

        toSummaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopTracksActivity.this, SummaryActivity.class);
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

        TextView topTrack1 = findViewById(R.id.topTrack1);
        TextView topTrack2 = findViewById(R.id.topTrack2);
        TextView topTrack3 = findViewById(R.id.topTrack3);
        TextView topTrack4 = findViewById(R.id.topTrack4);
        TextView topTrack5 = findViewById(R.id.topTrack5);

        if (topTracks != null) {
            String joinedTopTracks = TextUtils.join(", ", topTracks);
            //topTracksTextView.setText(joinedTopTracks);

            String[] words = joinedTopTracks.split(", ");

            topTrack1.setText("#1  " + words[0]);
            topTrack2.setText("#2  " + words[1]);
            topTrack3.setText("#3  " + words[2]);
            topTrack4.setText("#4  " + words[3]);
            topTrack5.setText("#5  " + words[4]);

        } else {
            String ans="No top songs data available";
            //topTracksTextView.setText(ans);

            topTrack1.setText(ans);
            topTrack2.setText(ans);
            topTrack3.setText(ans);
            topTrack4.setText(ans);
            topTrack5.setText(ans);
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
                runOnUiThread(() -> Toast.makeText(TopTracksActivity.this, "Failed to play track", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("Spotify", "Failed to command Spotify: " + response);
                    // Log response body for debugging
                    Log.e("Spotify", "Response body: " + response.body().string());
                    runOnUiThread(() -> Toast.makeText(TopTracksActivity.this, "Failed to command Spotify", Toast.LENGTH_SHORT).show());
                } else {
                    Log.d("Spotify", "Successfully played track");
                    // Log response body for debugging
                    Log.d("Spotify", "Response body: " + response.body().string());
                }
            }
        });
    }
}