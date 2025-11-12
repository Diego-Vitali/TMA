package model;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Previsao {

    private final String API_URL = "http://localhost:8000/predict/";
    public String realizarPrevisao(
            float pesoBrutoInput, float metroCubicoInput, float valorMercadoriaInput,
            int quantidadeVolumesInput, String tipoFreteInput, String viaTransporteInput, String ufOrigemInput,
            String ufDestinoInput) {

        HttpClient client = HttpClient.newHttpClient();

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

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body(); 
            } else {
                System.err.println("Erro ao chamar API: " + response.statusCode());
                return "{\"erro\": \"API retornou status " + response.statusCode() + "\"}";
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Erro de conexão: " + e.getMessage());
            e.printStackTrace();
            return "{\"erro\": \"Falha na conexão com a API\"}";
        }
    }
}