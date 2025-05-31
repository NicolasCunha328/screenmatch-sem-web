package br.com.alura.screenmatch.service;

public interface IConverteDados { // define uma interface para o serviço de conversão de dados
        <T> T obterDados(String json, Class<T> classe);
        /*
            declara um metodo generico que aceita uma string JSON e uma classe de tipo (T), retornando um objeto do tipo (T)
            garante que diferentes implementações de conversão de dados sigam o mesmo contrato
         */
}
