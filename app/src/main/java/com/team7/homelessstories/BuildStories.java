package com.team7.homelessstories;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BuildStories {
    private static ArrayList<Story> stories;
    private static final String TAG = BuildStories.class.getName();

    /**
     * Loads and parses JSON stories into Story objects, ordered by story index.
     */
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

    private static ArrayList<Story> readJsonStories(ArrayList<JSONObject> objs) {
        ArrayList<Story> stories = new ArrayList<>();
        for (JSONObject obj : objs) {
            stories.add(readJsonStory(obj));
        }
        Collections.sort(stories);
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
                    Answer a = new Answer(answer.getBoolean("real"), answer.getString("answer"),
                            answer.has("answerText") ? answer.getString("answerText") : "");
                    answers.add(a);
                }

                Decision d = new Decision(decision.getString("decisionText"), answers,
                        decision.has("keyDecisionText") ? decision.getString("keyDecisionText") : "",
                        decision.getString("icon"));
                decisions.add(d);
            }

            Story story = new Story(
                    storyJSON.getInt("index"),
                    storyJSON.getString("name"),
                    storyJSON.getString("image"),
                    storyJSON.getString("type"),
                    storyJSON.getString("preview"),
                    storyJSON.getString("finalText"), decisions);

            return story;
        } catch (JSONException je) {
            Log.e(TAG, je.getMessage());
            return null;
        }
    }

    private static ArrayList<JSONObject> loadJsonStories(Context context) {
        ArrayList<JSONObject> j = new ArrayList();
        String[] list = {};
        try {
            list = context.getAssets().list("");
            for (String name : list) {
                System.out.println(name);
                if (name.endsWith(".json")) {
                    j.add(loadJsonStory(name, context));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

class Story implements Serializable, Comparable {
    private int index;
    private String name, image, type, finalText, preview;
    private ArrayList<Decision> decisions;

    public Story(int index, String name, String image, String type, String preview, String finalText, ArrayList<Decision> decisions) {
        this.index = index;
        this.name = name;
        this.image = image;
        this.type = type;
        this.preview = preview;
        this.finalText = finalText;
        this.decisions = decisions;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public String getPreview() {
        return preview;
    }

    public String getFinalText() {
        return finalText;
    }

    public ArrayList<Decision> getDecisions() {
        return decisions;
    }

    @Override
    public int compareTo(Object story) {
        return index - ((Story) story).getIndex();
    }
}

class Decision implements Serializable {
    private String decisionText;
    private ArrayList<Answer> answers;
    private String keyDecisionText;
    private String icon;

    public Decision(String decisionText, ArrayList<Answer> answers, String keyDecisionText, String icon) {
        this.decisionText = decisionText;
        this.answers = answers;
        this.keyDecisionText = keyDecisionText;
        this.icon = icon;
    }

    public String getIcon() { return icon; }

    public String getKeyDecisionText() {
        return keyDecisionText;
    }

    public String getDecisionText() {
        return decisionText;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }
}

class Answer implements Serializable {
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

    public boolean isReal() {
        return real;
    }
}
