package http;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

public class HttpClient {
    private static class LazyHolder {
        static final HttpClient SINGLETON = new HttpClient(new OkHttpClient());
    }

  /**
   * 静态方法获取 HttpClient 单例，使用默认的连接参数
   * @return
   */
  public static HttpClient singleton() {
        return LazyHolder.SINGLETON;
  }

  private int connectTimeout = 3000;

  private int readTimeout = 3000;

  private int writeTimeout = 3000;

  private int maxIdleConnections = 5;

  private int keepAliveDuration = 5 * 60;

  private OkHttpClient client;

  private ObjectMapper objectMapper;

  public HttpClient(OkHttpClient client) {
    this.client = client;
    this.objectMapper = new ObjectMapper();
  }

  protected HttpClient(Optional<ObjectMapper> objectMapper) {
    this.objectMapper = objectMapper.orElseGet(ObjectMapper::new);
  }

  @PostConstruct
  protected void init() {
    ConnectionPool connectionPool = new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.SECONDS);

    client = new OkHttpClient.Builder()
        .followSslRedirects(false)
        .followRedirects(true)
        .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
        .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
        .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
        .connectionPool(connectionPool)
        .build();
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public void setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public void setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
  }

  public int getWriteTimeout() {
    return writeTimeout;
  }

  public void setWriteTimeout(int writeTimeout) {
    this.writeTimeout = writeTimeout;
  }

  public int getMaxIdleConnections() {
    return maxIdleConnections;
  }

  public void setMaxIdleConnections(int maxIdleConnections) {
    this.maxIdleConnections = maxIdleConnections;
  }

  public int getKeepAliveDuration() {
    return keepAliveDuration;
  }

  public void setKeepAliveDuration(int keepAliveDuration) {
    this.keepAliveDuration = keepAliveDuration;
  }
}
