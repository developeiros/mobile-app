package email;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EmailHelper
{
    private final String receiver;
    private Context context;
    private static final String url = "https://developeiros-api.herokuapp.com/send_email";

    public EmailHelper(Context context, String receiver)
    {
        this.context = context;
        this.receiver = receiver;
    }

    public synchronized void sendMail(String subject, String content)
    {
        Thread thread = new Thread(() -> withVolley(subject, content));

        thread.start();
    }

    private void withVolley(String subject, String content)
    {
        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject json = new JSONObject();
        try {
            json.put("to", this.receiver);
            json.put("subject", subject);
            json.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
            com.android.volley.Request.Method.POST,
            url,
            json,
            response -> {},
            error -> System.out.println(error.getMessage()));

        queue.add(request);
    }
}