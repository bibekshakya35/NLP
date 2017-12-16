package scrape.google.customsearch;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author bibek on 11/27/17
 * @project compare&search
 */
public class Meta {
    private String json;
    private Queries queries;

    public Meta(String json) {
        setJson(json);
        setQueries();
    }

    private String getJson() {
        return json;
    }

    private void setJson(String json) {
        this.json = json;
    }

    private JsonObject getJsonObject() {
        return new JsonParser().parse(getJson()).getAsJsonObject();
    }

    private void setQueries() {
        queries = new Gson().fromJson(getJsonObject().get("queries"), Queries.class);
    }

    public Queries getQueries() {
        return queries;
    }
}
