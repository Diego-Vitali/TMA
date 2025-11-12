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
            peso, metroCubico, valor, volumes, tipoFrete, via,
            ufOrigem, ufDestino
        );
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(respostaJsonDaAPI);
    }
}