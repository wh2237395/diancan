package com.cslg.websocket;


//@Component
//@ServerEndpoint("/webSocket")
//@Slf4j
public class WebSocket {

//    private Session session;
//
//    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();
//
//    @OnOpen
//    public void onOpen(Session session) {
//        this.session = session;
//        webSocketSet.add(this);
//        log.info("【websocket消息】有新的连接, 总数:{}", webSocketSet.size());
//    }
//
//    @OnClose
//    public void onClose() {
//        webSocketSet.remove(this);
//        log.info("【websocket消息】连接断开, 总数:{}", webSocketSet.size());
//    }
//
//    @OnMessage
//    public void onMessage(String message) {
//        log.info("【websocket消息】收到客户端发来的消息:{}", message);
//    }
//
//    public void sendMessage(String message) {
//        for (WebSocket webSocket : webSocketSet) {
//            log.info("【websocket消息】广播消息, message={}", message);
//            try {
//                webSocket.session.getBasicRemote().sendText(message);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
