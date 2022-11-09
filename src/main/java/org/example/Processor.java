package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Processor of HTTP request.
 */
public class Processor extends Thread {
    private final Socket socket;
    private final HttpRequest request;

    public Processor(Socket socket, HttpRequest request) {
        this.socket = socket;
        this.request = request;
    }

    public void process() throws IOException {
        // Print request that we received.
        System.out.println("Got request:");
        System.out.println(request.toString());
        System.out.flush();

        // To send response back to the client.
        PrintWriter output = new PrintWriter(socket.getOutputStream());

        String requestLine = request.getRequestLine();
        String[] requestLineParts = requestLine.split(" ");
        String method = requestLineParts[0];
        String path = requestLineParts[1];

        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html");
        output.println();

        switch (path) {
            case "/":
                output.println("<html><head><title>Hello</title></head><body><h1>Hello World!</h1></body></html>");
                break;
            case "/create/itemid":
                output.println("<html><head><title>/create/itemid</title></head><body><h1>Item created!</h1></body></html>");
                break;
            case "/update/itemid":
                output.println("<html><head><title>/update/itemid</title></head><body><h1>Item updated!</h1></body></html>");
                break;
            case "/exec/params":
                // sleep for random amount of seconds from 1 to 10
                int sleepTime = (int) (Math.random() * 10 + 1);
                try {
                    Thread.sleep(sleepTime * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                output.println("<html><head><title>/exec/params</title></head><body><h1>Heavy operation took " + sleepTime +" seconds to execute!</h1></body></html>");
                break;
        }
        output.flush();

        socket.close();
    }

    @Override
    public void run() {
        System.out.println("Hello, I am worker #" + Thread.currentThread().getId());
        try {
            process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
