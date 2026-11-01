package pt.isec.a2022147797.quizappexperimento.websockets

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshake

import java.net.URI

class QuizWebSocketClient(uri: URI) : WebSocketClient(uri) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        println("Connected to server")
    }

    override fun onMessage(message: String?) {
        println("Received message: $message")
        // Ações conforme a mensagem recebida (iniciar o quiz, avançar para a próxima pergunta, etc)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("Closed connection")
    }

    override fun onError(ex: Exception?) {
        println("Error: ${ex?.message}")
    }
}
