package com.team7.homelessstories;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class BuildStories {
    private static ArrayList<Story> stories;
    private static final String TAG = BuildStories.class.getName();

    public static ArrayList<Story> getStories(Context ctx) throws JSONException {
        if (stories == null) {
            ArrayList<JSONObject> obj = loadJsonStories(ctx);
            if (obj != null) {
                stories = readJsonStories(obj);
                return stories;
            }
            throw new JSONException("Failed to load decisions from JSON");
        } else {
            return stories;
        }
    }

    private static ArrayList<Story> readJsonStories(ArrayList<JSONObject> objs){
        ArrayList<Story> stories = new ArrayList<>();
        for(JSONObject obj : objs){
            stories.add(readJsonStory(obj));
        }
        return stories;
    }

    private static Story readJsonStory(JSONObject storyJSON) {
        try {
            // Decisions
            JSONArray decisionsJSON = storyJSON.getJSONArray("decisions");
            ArrayList<Decision> decisions = new ArrayList<>();
            for (int j = 0; j < decisionsJSON.length(); j++) {
                JSONObject decision = decisionsJSON.getJSONObject(j);

                // Answers for each decision
                ArrayList<Answer> answers = new ArrayList<>();
                JSONArray answersJSON = decision.getJSONArray("answers");
                for (int k = 0; k < answersJSON.length(); k++) {
                    JSONObject answer = answersJSON.getJSONObject(k);
                    Answer a = new Answer(answer.getBoolean("real"), answer.getString("answer"), answer.getString("answerText"));
                    answers.add(a);
                }

                Decision d = new Decision(decision.getString("decisionText"), answers);
                decisions.add(d);
            }
            Story story = new Story(storyJSON.getString("name"), storyJSON.getString("type"), decisions);
            return story;
        } catch (JSONException je) {
            Log.e(TAG, je.getMessage());
            return null;
        }
    }

    private static ArrayList<JSONObject> loadJsonStories(Context context){
        ArrayList<JSONObject> j = new ArrayList();
        String[] list = {};
        try{
            list = context.getAssets().list("");
            for(String name : list){
                if(name.endsWith(".json")){
                    j.add(loadJsonStory(name, context));
                }
            }
        } catch (IOException e){

        }
        return j;
    }

    private static JSONObject loadJsonStory(String fname, Context context) {
        JSONObject obj;
        try {
            InputStream is = context.getAssets().open(fname);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
//            json = new String(buffer, "UTF-8");
            obj = new JSONObject(new String(buffer, "UTF-8"));
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        } catch (JSONException je) {
            Log.e(TAG, je.getMessage());
            return null;
        }
        return obj;
    }
}

class Story {
    private String name, type;
    private ArrayList<Decision> decisions;

    public Story(String name, String type, ArrayList<Decision> decisions) {
        this.name = name;
        this.type = type;
        this.decisions = decisions;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Decision> getDecisions() {
        return decisions;
    }
}

class Decision {
    private String decisionText;
    private ArrayList<Answer> answers;

    public Decision(String decisionText, ArrayList<Answer> answers) {
        this.decisionText = decisionText;
        this.answers = answers;
    }

    public String getDecisionText() {
        return decisionText;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }
}

class Answer {
    private boolean real;
    private String answer;
    private String answerText;

    public Answer(boolean real, String answer, String answerText) {
        this.real = real;
        this.answer = answer;
        // Maybe not necessary if real answer?
        this.answerText = answerText;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAnswerText() {
        return answerText;
    }

    public boolean getReal() {
        return real;
    }
}