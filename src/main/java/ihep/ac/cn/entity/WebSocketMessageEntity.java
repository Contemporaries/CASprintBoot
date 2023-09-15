package ihep.ac.cn.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import ihep.ac.cn.enmus.ResultEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
public class WebSocketMessageEntity<T> {

    private short statusCode;
    private String message;
    private String type;
    private T data;

    public WebSocketMessageEntity(short statusCode, String message, String type, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.type = type;
        this.data = data;
    }

    public WebSocketMessageEntity(short statusCode, String message, String type) {
        this.statusCode = statusCode;
        this.message = message;
        this.type = type;
    }

    public static <T> WebSocketMessageEntity<T> success(String type, T data) {
        return new WebSocketMessageEntity<>(ResultEnum.SUCCESS.value(), ResultEnum.SUCCESS.getReasonPhrase(), type, data);
    }

    public static <T> WebSocketMessageEntity<T> error(String type, T data) {
        return new WebSocketMessageEntity<>(ResultEnum.ERROR.value(), ResultEnum.ERROR.getReasonPhrase(), type, data);
    }

    @SneakyThrows
    public String json() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
