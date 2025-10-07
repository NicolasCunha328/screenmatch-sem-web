package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.model.Categoria;

public record SerieDTO(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       String atores,
                       Categoria genero,
                       String sinopse,
                       String poster) {
}