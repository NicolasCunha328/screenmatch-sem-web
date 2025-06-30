package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal { // contém a lógica principal da aplicação, gerenciando o menu de interação com o usuário e orquestrando as operações de busca de séries e episódios

    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi(); // Utiliza ConsumoApi para requisições à API do OMDB e ConverteDados para processar respostas JSON.
    private final ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=13e18b3";
    private final List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repositorio){
        this.repositorio = repositorio;
    }

    public void exibeMenu() { // gerencia um menu interativo para buscar séries, episódios ou listar séries já buscadas
        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar séries por título
                    5 - Buscar série por ator
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() { // obtém dados de uma série da web e os adiciona a uma lista interna
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() { // solicita o nome da série ao usuário, faz a requisição à API e converte o JSON em DadosSerie
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){ // busca e exibe todos os episódios de uma série, iterando por temporada
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome:");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }

    }

    private void listarSeriesBuscadas(){ // converte a lista de DadosSerie em objetos Serie e os exibe, ordenando por gênero
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero)) // isso cria um Comparator que compara objetos Serie com base no valor retornado pelo metodo getGenero() de cada Serie. Ou seja, ele ordena as séries em ordem crescente de seus gêneros. Se getGenero() retorna um String, a ordenação será lexicográfica (alfabética)
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome:");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()){
            System.out.println("Dados da Série: " + serieBuscada.get());
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriePorAtor(){
        System.out.println("Qual o nome para a busca?");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de qual valor?");
        var avaliacao = leitura.nextDouble();
        List<Serie> serieEncontrada = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
        serieEncontrada.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

}
