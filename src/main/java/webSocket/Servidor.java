package webSocket;

import java.io.IOException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/socket")
public class Servidor {

    public void onOpen(Session session) {
        System.out.printf("Session abierta, id: %s%n", session.getId());
        try {
            session.getBasicRemote().sendText("Hola, conexion satisfactoria");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @OnMessage
    public void onMessage(String mensaje, Session session) {
        System.out.printf("Session abierta, id: %s%n", session.getId());
        System.out.println(mensaje);
        try {
            session.getBasicRemote().sendText("hemos recivido tu mensaje");
        } catch (Exception e) {
        }
    }

}
