
package Netflix.service;

import Netflix.model.Conteudo;

import java.util.ArrayList;
import java.util.List;

public class CatalogoService {

    private final List<Conteudo> catalogo = new ArrayList<>();

    // Adicionar filme ou série ao catálogo
    public void adicionarConteudo(Conteudo conteudo) {

        if (conteudo == null) {
            System.out.println("Conteúdo inválido.");
            return;
        }

        if (buscarPorId(conteudo.getId()) != null) {
            System.out.println("Já existe um conteúdo com esse ID.");
            return;
        }

        catalogo.add(conteudo);
        System.out.println("Conteúdo adicionado com sucesso!");
    }

    // Listar todos os filmes e séries

    public void listarConteudos() {
        System.out.println("\n===== CATÁLOGO =====");

        if (catalogo.isEmpty()) {
            System.out.println("Nenhum conteúdo cadastrado.");
            return;
        }

        for (Conteudo conteudo : catalogo) {
            System.out.println(
                    "ID: " + conteudo.getId() +
                            " | Título: " + conteudo.getTitulo() +
                            " | Ano: " + conteudo.getAno() +
                            " | Gênero: " + conteudo.getGenero()
            );
        }
    }

    // Buscar conteúdo pelo ID
    public Conteudo buscarPorId(int id) {

        for (Conteudo conteudo : catalogo) {
            if (conteudo.getId() == id) {
                return conteudo;
            }
        }

        return null;
    }

    // Carregar conteúdos salvos em arquivo
    public void carregarConteudos(List<Conteudo> conteudos) {

        catalogo.clear();

        if (conteudos != null) {
            catalogo.addAll(conteudos);
        }
    }

    // Retornar o catálogo
    public List<Conteudo> getCatalogo() {
        return catalogo;
    }

    // Listar filmes e séries de um gênero específico

    public void listarPorGenero(String genero) {

        if (genero == null || genero.isBlank()) {
            System.out.println("Informe um gênero válido.");
            return;
        }

        String generoBusca = normalizarTexto(genero);

        boolean encontrou = false;

        System.out.println("\n===== CATEGORIA: "
                + genero.toUpperCase() + " =====");

        for (Conteudo conteudo : catalogo) {

            String generoConteudo =
                    normalizarTexto(conteudo.getGenero());

            if (generoConteudo.equals(generoBusca)) {

                System.out.println(
                        "ID: " + conteudo.getId()
                                + " | Título: " + conteudo.getTitulo()
                                + " | Ano: " + conteudo.getAno()
                );

                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println(
                    "Nenhum conteúdo encontrado nessa categoria."
            );
        }
    }

    // Remove acentos, espaços extras e diferenças
// entre letras maiúsculas e minúsculas
    private String normalizarTexto(String texto) {

        String normalizado = java.text.Normalizer.normalize(
                texto.trim(),
                java.text.Normalizer.Form.NFD
        );

        return normalizado
                .replaceAll("\\p{M}", "")
                .replaceAll("\\s+", " ")
                .toLowerCase(java.util.Locale.ROOT);
    }

    public void exibirDestaque() {
        System.out.println("\n===== DESTAQUE PRINCIPAL =====");

        for (Conteudo conteudo : catalogo) {
            if (conteudo.isDestaque()) {
                System.out.println("Em destaque: " + conteudo.getTitulo());
                System.out.println("Sinopse: " + conteudo.getSinopse());
                System.out.println("Ano: " + conteudo.getAno());
                System.out.println("Gênero: " + conteudo.getGenero());
                return;
            }
        }

        System.out.println("Nenhum conteúdo em destaque.");
    }


}
