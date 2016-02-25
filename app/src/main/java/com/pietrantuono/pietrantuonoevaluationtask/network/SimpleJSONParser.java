package com.pietrantuono.pietrantuonoevaluationtask.network;

import com.pietrantuono.pietrantuonoevaluationtask.pojos.Item;
import com.pietrantuono.pietrantuonoevaluationtask.pojos.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Maurizio Pietrantuono, maurizio.pietrantuono@gmail.com.
 */
public class SimpleJSONParser {

    public static ArrayList<ListItem> parseItemsList(InputStream inputStream, int listRowID) {
        ArrayList<ListItem> result= new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content=stringBuilder.toString();
        if(content==null || content.length()<=0)return result;
        JSONObject jsonObject=null;
        try {
            jsonObject=new JSONObject(content);
        } catch (JSONException e) {
            e.printStackTrace();
            return result;
        }
        if(jsonObject==null) return result;
        JSONArray jsonArray=null;
        try {
            jsonArray=jsonObject.getJSONArray(ListItem.ITEMS_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
            return result;
        }
        if (jsonArray==null || jsonArray.length()<=0)return result;
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonItem=null;
            try {
                jsonItem=jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            if(jsonItem==null)continue;
            ListItem listItem= new ListItem();
            long id = 0;
            String title=null;
            String subtitle=null;
            String date=null;
            try {id=jsonItem.getLong(ListItem.ID_KEY);} catch (JSONException ignored) {};
            try {title=jsonItem.getString(ListItem.TITLE_KEY);} catch (JSONException ignored) {};
            try {subtitle=jsonItem.getString(ListItem.SUBTITLE_KEY);} catch (JSONException ignored) {};
            try {date=jsonItem.getString(ListItem.DATE_KEY);} catch (JSONException ignored) {};

            if(id>0l)listItem.setId(id);
            if(title!=null)listItem.setTitle(title);
            if(subtitle!=null)listItem.setSubtitle(subtitle);
            if(date!=null)listItem.setDate(date);
            listItem.setContentListRowID(listRowID);
            result.add(listItem);
        }
        return result;

    }

    public static Item parseSingleItem(InputStream inputStream) {
        Item result= null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content=stringBuilder.toString();
        if(content==null || content.length()<=0)return result;
        JSONObject jsonObject=null;
        try {
            jsonObject=new JSONObject(content);
        } catch (JSONException e) {
            e.printStackTrace();
            return result;
        }
        if(jsonObject==null) return result;
        JSONObject jsonItem=null;
        try {
            jsonItem=jsonObject.getJSONObject(Item.ITEM_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
            return result;
        }
        if (jsonItem==null || jsonItem.length()<=0)return result;
            result= new Item();
            long id = 0;
            String title=null;
            String subtitle=null;
            String date=null;
            String body=null;
            try {id=jsonItem.getLong(Item.ID_KEY);} catch (JSONException ignored) {};
            try {title=jsonItem.getString(Item.TITLE_KEY);} catch (JSONException ignored) {};
            try {subtitle=jsonItem.getString(Item.SUBTITLE_KEY);} catch (JSONException ignored) {};
            try {date=jsonItem.getString(Item.DATE_KEY);} catch (JSONException ignored) {};
            try {body=jsonItem.getString(Item.BODY_KEY);} catch (JSONException ignored) {};
            if(id>0l)result.setId(id);
            if(title!=null)result.setTitle(title);
            if(subtitle!=null)result.setSubtitle(subtitle);
            if(date!=null)result.setDate(date);
            if(body!=null)result.setBody(body);

        return result;

    }
}
