<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menu</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
          rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
          
</head>
<body>

    <%@ include file="includes/navbar.jsp" %>
    
    <div class="container mt-4">
        <h1 class="text-primary">Página Inicial</h1>
    </div>
    <h2 class="text-secondary">Calcular Previsão de Frete</h2>
    
    Peso Bruto: <input type="text" id="pesoBrutoInput" value="150.5"><br/>
    UF Origem: <input type="text" id="ufOrigemInput" value="SP"><br/>
    UF Destino: <input type="text" id="ufDestinoInput" value="RJ"><br/>
    Metro Cubico: <input type="text" id="metroCubicoInput" value="10.0"><br/>
    Valor Declarado (em R$): <input type="text" id="valorMercadoriaInput" value="100.00"><br/>
    Quantidade de Volumes: <input type="text" id="quantidadeVolumesInput" value="1"><br/>
    Tipo de Frete: <input type="text" id="tipoFreteInput" value="1"><br/>    
	Via de Transporte: 
	<select id="viaTransporteInput" name="viaTransporteInput">
	    <option value="RODOVIÁRIO">RODOVIÁRIO</option>
	    <option value="AÉREO">AÉREO</option>
	</select>
	<br/>
	
    <button id="btnPrever">Realizar Previsão</button>
    
    <hr>
    <h3>Resultado: <span id="resultadoSpan"></span></h3>

    <script>
    $(document).ready(function() {
        
        $("#btnPrever").click(function() {
            $("#resultadoSpan").text("Calculando...");

            var peso = $("#pesoBrutoInput").val();
            var ufOrigem = $("#ufOrigemInput").val();
            var ufDestino = $("#ufDestinoInput").val();
            var metroCubico = $("#metroCubicoInput").val();
            var valorMercadoria = $("#valorMercadoriaInput").val();
            var quantidadeVolumes = $("#quantidadeVolumesInput").val();
            var tipoFrete = $("#tipoFreteInput").val();
            var viaTransporte = $("#viaTransporteInput").val();

            $.ajax({
                url: "${pageContext.request.contextPath}/previsaoServlet", 
                type: "POST",           
                data: {                 
                    pesoBrutoInput: peso,
                    ufOrigemInput: ufOrigem,
                    ufDestinoInput: ufDestino,
                    
                    metroCubicoInput: metroCubico,
                    valorMercadoriaInput: valorMercadoria,
                    quantidadeVolumesInput: quantidadeVolumes,
                    tipoFreteInput: tipoFrete,
                    viaTransporteInput: viaTransporte,
                },
                
                success: function(responseJson) {
                    let dias = Number(responseJson.transitTimeOutput);
                    let diasArredondado = Math.round(dias);
                    $("#resultadoSpan").text(diasArredondado + " dias");
                },
                
                error: function(xhr, status, error) {
                    $("#resultadoSpan").text("Erro ao calcular.");
                }
            });
        });
    });
    </script>

</body>
</html>