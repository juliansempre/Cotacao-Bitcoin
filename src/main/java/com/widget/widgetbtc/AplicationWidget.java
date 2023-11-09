package com.widget.widgetbtc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class AplicationWidget extends Frame {
    private Label priceLabel;

    public AplicationWidget() {
        setTitle("Bitcoin Price Widget");
        setSize(300, 100);
        setLayout(null);
        setAlwaysOnTop(true); // Faz a janela sempre ficar no topo de outras janelas
        setBackground(Color.BLACK); // Define o fundo como preto

        priceLabel = new Label();
        priceLabel.setBounds(50, 30, 200, 30);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setForeground(new Color(0x00FF00)); // Define a cor da letra como verde (#00FF00)
        add(priceLabel);

        // Inicia uma nova thread para atualizar o preço a cada segundo
        Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    updateBitcoinPrice();
                    try {
                        Thread.sleep(1000); // Aguarda 1 segundo antes de atualizar novamente
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        updateThread.start();
    }

    private void updateBitcoinPrice() {
        String apiUrl = "https://api.coindesk.com/v1/bpi/currentprice/BTC.json";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            JSONObject bpi = json.getJSONObject("bpi");
            JSONObject usd = bpi.getJSONObject("USD");
            String price = usd.getString("rate");
            priceLabel.setText("Price: $" + price);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AplicationWidget widget = new AplicationWidget();
        widget.setVisible(true);
    }
}
