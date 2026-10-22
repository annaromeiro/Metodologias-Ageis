
package Netflix.service;
import Netflix.model.Avaliacao;
import Netflix.model.Conteudo;
import Netflix.model.Usuario;

import java.util.ArrayList;

public class AvaliacaoService {

    private ArrayList<Avaliacao> avaliacoes;

    public AvaliacaoService() {
        avaliacoes = new ArrayList<>();
    }

    public boolean cadastrarAvaliacao(Avaliacao avaliacao) {
        if (avaliacao == null) {
            System.out.println("Avaliação inválida.");
            return false;
        }

        for (Avaliacao existente : avaliacoes) {

            boolean mesmoUsuario =
                    existente.getUsuario().getId()
                            == avaliacao.getUsuario().getId();

            boolean mesmoConteudo =
                    existente.getConteudo().getId()
                            == avaliacao.getConteudo().getId();

            if (mesmoUsuario && mesmoConteudo) {
                System.out.println(
                        "Você já avaliou este conteúdo."
                );
                return false;
            }
        }

        avaliacoes.add(avaliacao);

        System.out.println("Avaliação cadastrada com sucesso!");
        return true;
    }

    public void listarAvaliacoesDoConteudo(Conteudo conteudo) {
        if (conteudo == null) {
            System.out.println("Conteúdo inválido.");
            return;
        }

        boolean encontrou = false;

        System.out.println("Avaliações de: " + conteudo.getTitulo());

        for (Avaliacao avaliacao : avaliacoes) {
            if (avaliacao.getConteudo().getId() == conteudo.getId()) {
                System.out.println("--------------------");
                avaliacao.exibirAvaliacao();
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhuma avaliação cadastrada.");
        }
    }

    public double calcularMediaNotas(Conteudo conteudo) {
        if (conteudo == null) {
            return 0;
        }

        int soma = 0;
        int quantidade = 0;

        for (Avaliacao avaliacao : avaliacoes) {
            if (avaliacao.getConteudo().getId() == conteudo.getId()) {
                soma += avaliacao.getNota();
                quantidade++;
            }
        }

        if (quantidade == 0) {
            return 0;
        }

        return (double) soma / quantidade;
    }

    public void listarAvaliacoesDoUsuario(Usuario usuario) {
        if (usuario == null) {
            System.out.println("Usuário inválido.");
            return;
        }

        boolean encontrou = false;

        System.out.println("Avaliações de " + usuario.getNome());

        for (Avaliacao avaliacao : avaliacoes) {
            if (avaliacao.getUsuario().getId() == usuario.getId()) {
                System.out.println("--------------------");
                avaliacao.exibirAvaliacao();
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Este usuário ainda não fez avaliações.");
        }
    }
}
