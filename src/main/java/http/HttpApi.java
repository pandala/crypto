package http;


import java.io.IOException;
import java.util.logging.Logger;

public class HttpApi<T> {
  /**
   * 将返回的 String 转换为需要的类型 {@Link T}
   * @param <T>
   */
  @FunctionalInterface
  public interface Convert<T> {
    T apply(String T) throws IOException;
  }

  protected static final Logger = LoggerFactory.get

}
