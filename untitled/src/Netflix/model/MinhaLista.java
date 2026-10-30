package Netflix.model;

import java.util.ArrayList;

public class MinhaLista {

    private Usuario usuario;
    private ArrayList<Conteudo> conteudos;

    public MinhaLista(Usuario usuario) {
        this.usuario = usuario;
        this.conteudos = new ArrayList<>();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public ArrayList<Conteudo> getConteudos() {
        return new ArrayList<>(conteudos);
    }

    public boolean adicionarConteudo(Conteudo conteudo) {

        if (conteudo == null) {
            System.out.println("Conteúdo inválido.");
            return false;
        }

        for (Conteudo c : conteudos) {
            if (c.getId() == conteudo.getId()) {
                System.out.println("Conteúdo já está na lista.");
                return false;
            }
        }

        conteudos.add(conteudo);
        System.out.println("Conteúdo adicionado à Minha Lista!");
        return true;
    }

    public boolean removerConteudo(int id) {

        for (Conteudo conteudo : conteudos) {
            if (conteudo.getId() == id) {
                conteudos.remove(conteudo);
                System.out.println("Conteúdo removido da lista.");
                return true;
            }
        }

        System.out.println("Conteúdo não encontrado na lista.");
        return false;
    }

    public void listarConteudos() {

        if (conteudos.isEmpty()) {
            System.out.println("Sua lista está vazia.");
            return;
        }

        System.out.println("Minha Lista - " + usuario.getNome());

        for (Conteudo conteudo : conteudos) {
            System.out.println("--------------------");
            conteudo.exibirDetalhes();
        }
    }
}
