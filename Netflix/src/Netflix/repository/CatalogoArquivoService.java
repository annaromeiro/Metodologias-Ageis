
package Netflix.repository;

import Netflix.model.Conteudo;
import Netflix.model.Filme;
import Netflix.model.Serie;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CatalogoArquivoService {

    private final String arquivo = "catalogo.txt";

    public void salvarCatalogo(List<Conteudo> catalogo) {

        try (BufferedWriter escritor =
                     new BufferedWriter(new FileWriter(arquivo))) {

            for (Conteudo conteudo : catalogo) {

                String tipo;
                int quantidade;

                if (conteudo instanceof Filme filme) {
                    tipo = "FILME";
                    quantidade = filme.getDuracao();

                } else if (conteudo instanceof Serie serie) {
                    tipo = "SERIE";
                    quantidade = serie.getTemporadas();

                } else {
                    continue;
                }

                String linha = String.join(";",
                        tipo,
                        String.valueOf(conteudo.getId()),
                        limpar(conteudo.getTitulo()),
                        limpar(conteudo.getSinopse()),
                        String.valueOf(conteudo.getAno()),
                        limpar(conteudo.getGenero()),
                        String.valueOf(quantidade),
                        limpar(conteudo.getElenco()),
                        limpar(conteudo.getClassificacaoIndicativa()),
                        limpar(conteudo.getImagemUrl()),
                        limpar(conteudo.getTrailerUrl()),
                        String.valueOf(conteudo.isDestaque())
                );

                escritor.write(linha);
                escritor.newLine();
            }

            System.out.println("Catálogo salvo com sucesso!");

        } catch (IOException e) {
            System.out.println(
                    "Erro ao salvar catálogo: " + e.getMessage()
            );
        }
    }

    public ArrayList<Conteudo> carregarCatalogo() {

        ArrayList<Conteudo> conteudos = new ArrayList<>();

        try (BufferedReader leitor =
                     new BufferedReader(new FileReader(arquivo))) {

            String linha;

            while ((linha = leitor.readLine()) != null) {

                String[] dados = linha.split(";", -1);

                // Aceita o formato antigo e o novo
                if (dados.length != 7 && dados.length != 12) {
                    continue;
                }

                try {
                    int id = Integer.parseInt(dados[1]);
                    String titulo = dados[2];
                    String sinopse = dados[3];
                    int ano = Integer.parseInt(dados[4]);
                    String genero = dados[5];
                    int quantidade = Integer.parseInt(dados[6]);

                    Conteudo conteudo;

                    if (dados[0].equals("FILME")) {
                        conteudo = new Filme(
                                id, titulo, sinopse,
                                ano, genero, quantidade
                        );

                    } else if (dados[0].equals("SERIE")) {
                        conteudo = new Serie(
                                id, titulo, sinopse,
                                ano, genero, quantidade
                        );

                    } else {
                        continue;
                    }

                    // Carregar os novos atributos, quando existirem
                    if (dados.length == 12) {
                        conteudo.setElenco(dados[7]);
                        conteudo.setClassificacaoIndicativa(dados[8]);
                        conteudo.setImagemUrl(dados[9]);
                        conteudo.setTrailerUrl(dados[10]);
                        conteudo.setDestaque(
                                Boolean.parseBoolean(dados[11])
                        );
                    }

                    conteudos.add(conteudo);

                } catch (NumberFormatException e) {
                    System.out.println(
                            "Conteúdo com dados inválidos foi ignorado."
                    );
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(
                    "Catálogo não encontrado. Usando conteúdos iniciais."
            );

        } catch (IOException e) {
            System.out.println(
                    "Erro ao carregar catálogo: " + e.getMessage()
            );
        }

        return conteudos;
    }

    // Evita que ponto e vírgula ou quebras de linha
    // atrapalhem a leitura do arquivo
    private String limpar(String texto) {

        if (texto == null) {
            return "";
        }

        return texto.replace(";", ",")
                .replace("\n", " ")
                .replace("\r", " ");
    }
}
