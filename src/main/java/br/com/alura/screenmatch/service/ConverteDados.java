package br.com.alura.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados { // implementa a interface IConverteDados e é responsável por converter dados JSON em objetos Java usando a biblioteca Jackson (ObjectMapper)

    private final ObjectMapper mapper = new ObjectMapper(); // objeto usado para desserialização do JSON

    @Override
    public <T> T obterDados(String json, Class<T> classe) { // metodo da interface IConverteDados
        try {
            return mapper.readValue(json, classe); // converte a string JSON para um objeto da classe especificada
        } catch (JsonProcessingException e) { // trata JsonProcessingException que pode ocorrer durante a conversão
            throw new RuntimeException(e);
        }
    }
}
