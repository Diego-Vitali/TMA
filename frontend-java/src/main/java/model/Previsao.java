package model;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Previsao {
    private final String API_URL = "http://localhost:8000/predict/";

    public String realizarPrevisao(
            float pesoBrutoInput, float metroCubicoInput, float valorMercadoriaInput,
            int quantidadeVolumesInput, String tipoFreteInput, String viaTransporteInput,
            String ufOrigemInput, String ufDestinoInput) {

        String jsonBody = "{" +
                "\"Peso total bruto\": " + pesoBrutoInput + "," +
                "\"Metro cúbico\": " + metroCubicoInput + "," +
                "\"Valor NF\": " + valorMercadoriaInput + "," +
                "\"Volume NF\": " + quantidadeVolumesInput + "," +
                "\"Tipo de frete NF\": \"" + tipoFreteInput + "\"," +
                "\"Via de transporte\": \"" + viaTransporteInput + "\"," +
                "\"UF emitente NF\": \"" + ufOrigemInput + "\"," +
                "\"UF destinatário NF\": \"" + ufDestinoInput + "\"" +
                "}";

        try {
            URL url = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonBody.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            int code = con.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            System.out.println("Status: " + code);
            System.out.println("Resposta: " + response);

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"erro\": \"Falha na conexão com a API\"}";
        }
    }
}
