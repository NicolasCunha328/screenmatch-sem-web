package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT { // classe criada para interagir com a OpenAI do ChatGPT para realização de tradução de texto
    public static String obterTraducao(String texto) { // metodo chamado para traduzir strings
        OpenAiService service = new OpenAiService("cole aqui sua chave da OpenAI"); // instância para colocar a chave da OpenAI

        CompletionRequest requisicao = CompletionRequest.builder() // monta uma requisição
                .model("gpt-3.5-turbo-instruct") // modelo
                .prompt("traduza para o português o texto: " + texto) // prompt de tradução
                .maxTokens(1000) // número máximo de tokens
                .temperature(0.7) // temperatura
                .build();

        var resposta = service.createCompletion(requisicao); // envia a requisição para a API do OpenAI
        return resposta.getChoices().get(0).getText(); // retorna o texto traduzido
    }
}