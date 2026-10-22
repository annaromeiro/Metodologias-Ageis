
package Netflix.model;

public abstract class Conteudo {

    private int id;
    private String titulo;
    private String sinopse;
    private int ano;
    private String genero;

    // Novos atributos
    private String elenco;
    private String classificacaoIndicativa;
    private String imagemUrl;
    private String trailerUrl;
    private boolean destaque;

    // Construtor original (mantido por compatibilidade)
    public Conteudo(int id, String titulo, String sinopse,
                    int ano, String genero) {

        this.id = id;
        this.titulo = titulo;
        this.sinopse = sinopse;
        this.ano = ano;
        this.genero = genero;

        this.elenco = "";
        this.classificacaoIndicativa = "";
        this.imagemUrl = "";
        this.trailerUrl = "";
        this.destaque = false;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSinopse() {
        return sinopse;
    }

    public int getAno() {
        return ano;
    }

    public String getGenero() {
        return genero;
    }

    public String getElenco() {
        return elenco;
    }

    public void setElenco(String elenco) {
        this.elenco = elenco;
    }

    public String getClassificacaoIndicativa() {
        return classificacaoIndicativa;
    }

    public void setClassificacaoIndicativa(
            String classificacaoIndicativa) {
        this.classificacaoIndicativa = classificacaoIndicativa;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public boolean isDestaque() {
        return destaque;
    }

    public void setDestaque(boolean destaque) {
        this.destaque = destaque;
    }

    public abstract void exibirDetalhes();
}
