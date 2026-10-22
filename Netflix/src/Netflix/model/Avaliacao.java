package Netflix.model;

public class Avaliacao {

    private Usuario usuario;
    private Conteudo conteudo;
    private int nota;
    private String comentario;

    public Avaliacao(Usuario usuario, Conteudo conteudo,
                     int nota, String comentario) {

        if (usuario == null || conteudo == null) {
            throw new IllegalArgumentException(
                    "Usuário e conteúdo são obrigatórios."
            );
        }

        if (nota < 0 || nota > 10) {
            throw new IllegalArgumentException(
                    "A nota deve estar entre 0 e 10."
            );
        }

        this.usuario = usuario;
        this.conteudo = conteudo;
        this.nota = nota;
        this.comentario = comentario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Conteudo getConteudo() {
        return conteudo;
    }

    public int getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void exibirAvaliacao() {
        System.out.println("Usuário: " + usuario.getNome());
        System.out.println("Conteúdo: " + conteudo.getTitulo());
        System.out.println("Nota: " + nota + "/10");
        System.out.println("Comentário: " + comentario);
    }
}
