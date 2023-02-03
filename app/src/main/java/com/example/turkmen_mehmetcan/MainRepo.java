package com.example.turkmen_mehmetcan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MainRepo {
    public void getCategoriesData(ExecutorService srv, Handler uiHandler) {
        srv.execute(() -> {
            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/getallnewscategories");
                HttpURLConnection conn =(HttpURLConnection)url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while((line=reader.readLine())!=null){
                    buffer.append(line);
                }

                JSONObject currentObject = new JSONObject(buffer.toString());
                JSONArray currentArray = new JSONArray(currentObject.getJSONArray("items").toString());
                List<String> data = new ArrayList<>();

                for (int i = 0; i < currentArray.length(); i++) {
                    JSONObject current = currentArray.getJSONObject(i);
                    data.add(current.getString("name"));
                }

                Message msg = new Message();
                msg.obj = data;
                uiHandler.sendMessage(msg);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void getNewsDataByCategory(ExecutorService srv, Handler uiHandler, int id) {
        srv.execute(() -> {
            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/getbycategoryid/" + String.valueOf(id));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                JSONObject currentObject = new JSONObject(buffer.toString());
                JSONArray currentArray = new JSONArray(currentObject.getJSONArray("items").toString());

                List<News> newsArrayList = new ArrayList<News>();

                for(int i = 0; i < currentArray.length(); i++) {
                    JSONObject current = currentArray.getJSONObject(i);

                    News news = new News(current.getInt("id"), current.getString("title"), current.getString("text"),
                            current.getString("date"), current.getString("image"), current.getString("categoryName"));

                    newsArrayList.add(news);
                }

                Message msg = new Message();
                msg.obj = newsArrayList;
                uiHandler.sendMessage(msg);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void getNewsById(ExecutorService srv, Handler uiHandler, int id) {
        srv.execute(() -> {
            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/getnewsbyid/" + id);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                JSONObject currentObject = new JSONObject(buffer.toString());
                JSONArray currentArray = new JSONArray(currentObject.getJSONArray("items").toString());

                JSONObject current = currentArray.getJSONObject(0);

                News news = new News(current.getInt("id"), current.getString("title"), current.getString("text"),
                        current.getString("date"), current.getString("image"), current.getString("categoryName"));

                Message msg = new Message();
                msg.obj = news;
                uiHandler.sendMessage(msg);

             } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void getCommentsByNewsId(ExecutorService srv, Handler uiHandler, int id) {
        srv.execute(() -> {
            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/getcommentsbynewsid/" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String line = "";

                while((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                JSONObject currentObject = new JSONObject(buffer.toString());
                JSONArray currentArray = new JSONArray(currentObject.getJSONArray("items").toString());

                List<Comment> data = new ArrayList<Comment>();

                for(int i = 0; i < currentArray.length(); i++) {
                    JSONObject current = currentArray.getJSONObject(i);
                    Comment comment = new Comment(current.getInt("id"), current.getInt("news_id")
                            , current.getString("name"), current.getString("text"));

                    data.add(comment);
                }

                Message msg = new Message();
                msg.obj = data;
                uiHandler.sendMessage(msg);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void postCommentByNewsId(ExecutorService srv, Handler uiHandler, int id, String name, String message) {
        srv.execute(() -> {
            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/savecomment");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject outputData = new JSONObject();
                outputData.put("name", name);
                outputData.put("text", message);
                outputData.put("news_id", id);

                BufferedOutputStream writer = new BufferedOutputStream(conn.getOutputStream());
                writer.write(outputData.toString().getBytes(StandardCharsets.UTF_8));
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String line = "";

                while((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                JSONObject retVal = new JSONObject(buffer.toString());
                conn.disconnect();

                int retValue = retVal.getInt("serviceMessageCode");

                Message msg = new Message();
                msg.obj = retValue;
                uiHandler.sendMessage(msg);

             } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void downloadImage(ExecutorService srv, Handler uiHandler,String path){
        srv.execute(()->{
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                Bitmap bitmap =  BitmapFactory.decodeStream(conn.getInputStream());

                Message msg = new Message();
                msg.obj = bitmap;
                uiHandler.sendMessage(msg);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
