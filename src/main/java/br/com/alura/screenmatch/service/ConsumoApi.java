package br.com.alura.screenmatch.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi { // realiza requisições HTTP para consumir dados de APIs externas.

    public String obterDados(String endereco) { // recebe uma URL
        HttpClient client = HttpClient.newHttpClient(); // criação do cliente HTTP
        HttpRequest request = HttpRequest.newBuilder() // construção de uma requisição com a URI fornecida
                .uri(URI.create(endereco))
                .build();
        HttpResponse<String> response = null; // envia e extrai a requisição como uma String (JSON)
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } // metodo try catch funciona para tratar exceções

        String json = response.body();
        return json;
    }

}
