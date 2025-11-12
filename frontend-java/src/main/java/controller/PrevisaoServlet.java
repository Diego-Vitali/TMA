package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Previsao;

@WebServlet("/previsaoServlet")
public class PrevisaoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            float peso = Float.parseFloat(request.getParameter("pesoBrutoInput"));
            float metroCubico = Float.parseFloat(request.getParameter("metroCubicoInput"));
            float valor = Float.parseFloat(request.getParameter("valorMercadoriaInput"));
            int volumes = Integer.parseInt(request.getParameter("quantidadeVolumesInput"));
            String tipoFrete = request.getParameter("tipoFreteInput");
            String via = request.getParameter("viaTransporteInput");
            String ufOrigem = request.getParameter("ufOrigemInput");
            String ufDestino = request.getParameter("ufDestinoInput");

            Previsao previsaoClient = new Previsao();
            String respostaJsonDaAPI = previsaoClient.realizarPrevisao(
                    peso, metroCubico, valor, volumes, tipoFrete, via, ufOrigem, ufDestino);

            System.out.println("Resposta da API: " + respostaJsonDaAPI);

            String transitTime = "0";

            if (respostaJsonDaAPI != null && respostaJsonDaAPI.contains("predicted_transit_time")) {
                int startIndex = respostaJsonDaAPI.indexOf("predicted_transit_time") + 24;
                int endIndex = respostaJsonDaAPI.indexOf("}", startIndex);
                if (endIndex == -1) endIndex = respostaJsonDaAPI.length();
                transitTime = respostaJsonDaAPI.substring(startIndex, endIndex).replaceAll("[^0-9\\.\\-]", "");
            }

            String respostaFinal = "{\"transitTimeOutput\": " + transitTime + "}";

            response.getWriter().write(respostaFinal);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Erro ao processar previsão\"}");
        }
    }
}
