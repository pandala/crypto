package http;

import okhttp3.OkHttpClient;

public class HttpClient {
    private static class LazyHolder {
        static final HttpClient SINGLETON = new HttpClient(new OkHttpClient());
    }
}
