package appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpAppender extends AppenderBase<ILoggingEvent> {
    private String url;
    private String bearerToken;

    @Override
    protected void append(ILoggingEvent event) {
        try {
            JSONObject json = new JSONObject();
            json.put("timestamp", event.getTimeStamp());
            json.put("threadName", event.getThreadName());
            json.put("level", event.getLevel().toString());
            json.put("loggerName", event.getLoggerName());
            json.put("message", event.getFormattedMessage());

            sendLog(json.toString());
        } catch (Exception e) {
            addError("Failed to send log", e);
        }
    }

    private void sendLog(String jsonLog) throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + this.bearerToken);
        try {
            OutputStream os = connection.getOutputStream();
            byte[] input = jsonLog.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        } catch (IOException e) {
            throw new IOException("Failed to send log", e);
        }
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to send log: " + responseCode);
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

}