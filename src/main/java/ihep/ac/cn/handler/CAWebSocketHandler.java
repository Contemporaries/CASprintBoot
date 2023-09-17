package ihep.ac.cn.handler;

import ihep.ac.cn.entity.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;

@Slf4j
@Component
public class CAWebSocketHandler implements WebSocketHandler {


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WS: " + session.getId() + " connected");
        sendMessage(session, "success!");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("WS: " + session.getId() + " received a message ==> " + message.getPayload());
        sendMessage(session, ResultEntity.success("TEST", new double[]{1.0, 2.0, 3.0}).json());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("WS: " + session.getId() + " occurred a error ==> " + exception.getMessage());
        sendMessage(session, ResultEntity.error("Error", "Occurred error").json());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("WS: " + session.getId() + " connection was closed; reason ==>" + closeStatus.getReason() + " cod ==> " + closeStatus.getCode());
        session.close();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessage(WebSocketSession session, String message) throws IOException {
        log.info("WS: sent a message to ==> " + session.getId());
        TextMessage textMessage = new TextMessage(message);
        session.sendMessage(textMessage);
    }

}
