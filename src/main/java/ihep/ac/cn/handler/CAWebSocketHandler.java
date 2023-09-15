package ihep.ac.cn.handler;

import ihep.ac.cn.entity.WebSocketMessageEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;

@Component
public class CAWebSocketHandler implements WebSocketHandler {


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sendMessage(session, "success!");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        sendMessage(session, WebSocketMessageEntity.error("TEST", new double[]{1.0, 2.0, 3.0}).json());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        sendMessage(session, WebSocketMessageEntity.error("Error", "Occurred error").json());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        session.close();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessage(WebSocketSession session, String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        session.sendMessage(textMessage);
    }

}
