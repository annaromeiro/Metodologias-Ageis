
package Netflix.model;

public class Filme extends Conteudo {

    private int duracao;

    public Filme(int id, String titulo, String sinopse,
                 int ano, String genero, int duracao) {

        super(id, titulo, sinopse, ano, genero);
        this.duracao = duracao;
    }

    public int getDuracao() {
        return duracao;
    }

    @Override
    public void exibirDetalhes() {

        System.out.println("\n===== FICHA TÉCNICA DO FILME =====");

        System.out.println("Título: " + getTitulo());
        System.out.println("Sinopse: " + getSinopse());
        System.out.println("Ano de lançamento: " + getAno());
        System.out.println("Gênero: " + getGenero());
        System.out.println("Duração: " + duracao + " minutos");

        System.out.println("Elenco: " +
                (getElenco().isBlank()
                        ? "Não informado"
                        : getElenco()));

        System.out.println("Classificação indicativa: " +
                (getClassificacaoIndicativa().isBlank()
                        ? "Não informada"
                        : getClassificacaoIndicativa()));

        System.out.println("Imagem: " +
                (getImagemUrl().isBlank()
                        ? "Não disponível"
                        : getImagemUrl()));

        System.out.println("Trailer: " +
                (getTrailerUrl().isBlank()
                        ? "Não disponível"
                        : getTrailerUrl()));

        if (isDestaque()) {
            System.out.println("★ CONTEÚDO EM DESTAQUE");
        }
    }
}
