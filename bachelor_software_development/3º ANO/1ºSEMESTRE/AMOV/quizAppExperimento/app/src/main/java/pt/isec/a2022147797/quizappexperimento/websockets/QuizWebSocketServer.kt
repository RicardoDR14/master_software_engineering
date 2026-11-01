package pt.isec.a2022147797.quizappexperimento.websockets

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

class QuizWebSocketServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {

    private val connectedClients = mutableListOf<WebSocket>()

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        println("New connection: ${conn?.remoteSocketAddress}")
        conn?.send("Connected to Quiz Server")
        connectedClients.add(conn!!)
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String, remote: Boolean) {
        println("Closed connection: ${conn?.remoteSocketAddress}")
        connectedClients.remove(conn)
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        println("Message from ${conn?.remoteSocketAddress}: $message")
        // Broadcast message to all connected clients
        for (client in connectedClients) {
            if (client != conn) {
                client.send(message)
            }
        }
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        println("Error: ${ex?.message}")
    }

    override fun onStart() {
        println("Server started successfully")
    }
}
