package ihep.ac.cn.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import ihep.ac.cn.enmus.ResultEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
public class ResultEntity<T> {

    private short statusCode;
    private String message;
    private String type;
    private T data;

    public ResultEntity(short statusCode, String message, String type, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.type = type;
        this.data = data;
    }

    public ResultEntity(short statusCode, String message, String type) {
        this.statusCode = statusCode;
        this.message = message;
        this.type = type;
    }

    public static <T> ResultEntity<T> success(String type, T data) {
        return new ResultEntity<>(ResultEnum.SUCCESS.value(), ResultEnum.SUCCESS.getReasonPhrase(), type, data);
    }

    public static <T> ResultEntity<T> error(String type, T data) {
        return new ResultEntity<>(ResultEnum.ERROR.value(), ResultEnum.ERROR.getReasonPhrase(), type, data);
    }

    @SneakyThrows
    public String json() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
