import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HTTPServer {
    private Print dialog;

    public void main() throws Exception {
        dialog = new Print();
        dialog.setTrayIcon();
        try {
            // setup the socket address
            InetSocketAddress address = new InetSocketAddress(7655);

            // Initialize the HTTPS server
            HttpServer httpServer = HttpServer.create(address, 0);
            httpServer.createContext("/", new MyHandler());
            httpServer.setExecutor(null); // creates a default executor
            httpServer.start();

            dialog.addLog("Print Server Started on port 7655", "INFO");
            dialog.pack();
            dialog.setVisible(true);

        } catch (Exception exception) {
            dialog.addLog("Failed to create HTTPS server on port 7655 of localhost", "ERROR");
            exception.printStackTrace();

        }
    }

    public class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            dialog.addLog("Serving Request", "DEBUG");
            String response = "This is the response";
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            t.getResponseHeaders().add("Access-Control-Allow-Headers", "Authorization");
            t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-type");
            t.sendResponseHeaders(200, response.length());

            StringBuilder payload = new StringBuilder();


            InputStream is = t.getRequestBody();
            int rByte;
            while ((rByte = is.read()) != -1) {
                payload.append((char) rByte);
            }
            is.close();
            ObjectMapper mapper = new ObjectMapper();

            if (payload.length() > 1) try {

                JsonNode p = mapper.readTree(payload.toString());
                try {
                    dialog.addLog("Printing using printer " + p.get("nameOfPrinter").asText(), "INFO");

                    PrinterConfig pc = new PrinterConfig();

                    pc.setSize(p.get("printerSize").asInt(80));

                    String content;
                    content = pc.getFormattedString(p.get("content").asText());

                    dialog.printUsingPrinter(p.get("nameOfPrinter").asText(), content);

                } catch (RuntimeWorkerException | NullPointerException
                        arg11) {
                    arg11.printStackTrace();
                    dialog.addLog(arg11.toString(), "ERROR");

                }

            } catch (Exception arg) {
                dialog.addLog(arg.toString(), "ERROR");

            }
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }


    }

}
