
package Netflix;

import Netflix.model.*;
import Netflix.repository.CatalogoArquivoService;
import Netflix.repository.UsuarioArquivoService;
import Netflix.service.*;

import java.awt.Desktop;
import java.net.URI;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        CatalogoService catalogo = new CatalogoService();
        UsuarioService usuarioService = new UsuarioService();
        AvaliacaoService avaliacaoService = new AvaliacaoService();

        UsuarioArquivoService arquivoService =
                new UsuarioArquivoService();

        CatalogoArquivoService catalogoArquivo =
                new CatalogoArquivoService();

        // Carregar usuários salvos
        for (Usuario usuario : arquivoService.carregarUsuarios()) {
            usuarioService.carregarUsuario(usuario);
        }

        System.out.println("Usuários carregados do arquivo.");

        Usuario usuarioLogado = null;
        MinhaLista minhaLista = null;

        // Carregar catálogo salvo
        var conteudosSalvos = catalogoArquivo.carregarCatalogo();

        if (conteudosSalvos.isEmpty()) {

            catalogo.adicionarConteudo(
                    new Filme(
                            1,
                            "Interestelar",
                            "Uma viagem pelo espaço e pelo tempo.",
                            2014,
                            "Ficção científica",
                            169
                    )
            );

            catalogo.adicionarConteudo(
                    new Serie(
                            2,
                            "Stranger Things",
                            "Mistérios em uma pequena cidade.",
                            2016,
                            "Ficção científica",
                            4
                    )
            );

        } else {
            catalogo.carregarConteudos(conteudosSalvos);
        }

        // Preencher informações iniciais, quando estiverem vazias
        Conteudo interestelar = catalogo.buscarPorId(1);

        if (interestelar != null) {

            if (interestelar.getElenco().isBlank()) {
                interestelar.setElenco(
                        "Matthew McConaughey, Anne Hathaway, Jessica Chastain"
                );
            }

            if (interestelar.getClassificacaoIndicativa().isBlank()) {
                interestelar.setClassificacaoIndicativa("10 anos");
            }

            if (interestelar.getImagemUrl().isBlank()) {
                interestelar.setImagemUrl("imagens/interestelar.jpg");
            }

            if (interestelar.getTrailerUrl().isBlank()) {
                interestelar.setTrailerUrl(
                        "https://www.youtube.com/watch?v=zSWdZVtXT7E"
                );
            }

            interestelar.setDestaque(true);
        }

        Conteudo strangerThings = catalogo.buscarPorId(2);

        if (strangerThings != null) {

            if (strangerThings.getElenco().isBlank()) {
                strangerThings.setElenco(
                        "Millie Bobby Brown, Finn Wolfhard, David Harbour"
                );
            }

            if (strangerThings.getClassificacaoIndicativa().isBlank()) {
                strangerThings.setClassificacaoIndicativa("16 anos");
            }

            if (strangerThings.getImagemUrl().isBlank()) {
                strangerThings.setImagemUrl(
                        "imagens/stranger-things.jpg"
                );
            }

            if (strangerThings.getTrailerUrl().isBlank()) {
                strangerThings.setTrailerUrl(
                        "https://www.youtube.com/results?search_query=stranger+things+trailer"
                );
            }
        }

        catalogoArquivo.salvarCatalogo(catalogo.getCatalogo());

        int opcao;

        do {

            System.out.println("\n===== STREAMING APP =====");
            System.out.println("1 - Cadastrar usuário");
            System.out.println("2 - Fazer login");
            System.out.println("3 - Listar catálogo");
            System.out.println("4 - Minha Lista");
            System.out.println("5 - Avaliar conteúdo");
            System.out.println("6 - Assistir trailer");
            System.out.println("7 - Buscar por categoria");
            System.out.println("8 - Ver ficha técnica");
            System.out.println("9 - Ver destaque principal");
            System.out.println("10 - Sair da conta");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = lerInteiro(scanner);

            switch (opcao) {

                // CADASTRAR USUÁRIO
                case 1: {

                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();

                    System.out.print("E-mail: ");
                    String email = scanner.nextLine();

                    System.out.print("Senha: ");
                    String senha = scanner.nextLine();

                    Usuario novoUsuario =
                            usuarioService.cadastrarUsuario(
                                    nome, email, senha
                            );

                    if (novoUsuario != null) {
                        arquivoService.salvarUsuarios(
                                usuarioService.getUsuarios()
                        );
                    }

                    break;
                }

                // FAZER LOGIN
                case 2: {

                    if (usuarioLogado != null) {
                        System.out.println(
                                "Saia da conta atual antes de entrar em outra."
                        );
                        break;
                    }

                    System.out.print("E-mail: ");
                    String emailLogin = scanner.nextLine();

                    System.out.print("Senha: ");
                    String senhaLogin = scanner.nextLine();

                    if (usuarioService.fazerLogin(
                            emailLogin, senhaLogin)) {

                        usuarioLogado =
                                usuarioService.buscarPorEmail(emailLogin);

                        minhaLista =
                                usuarioService.buscarMinhaLista(
                                        usuarioLogado.getId()
                                );

                        System.out.println(
                                "Bem-vindo, " + usuarioLogado.getNome()
                        );
                    }

                    break;
                }

                // LISTAR CATÁLOGO
                case 3: {

                    catalogo.listarConteudos();
                    break;
                }

                // MINHA LISTA
                case 4: {

                    if (usuarioLogado == null) {
                        System.out.println("Faça login primeiro.");
                        break;
                    }

                    System.out.println("\n===== MINHA LISTA =====");
                    System.out.println("1 - Adicionar conteúdo");
                    System.out.println("2 - Exibir minha lista");
                    System.out.print("Escolha: ");

                    int opcaoLista = lerInteiro(scanner);

                    if (opcaoLista == 1) {

                        catalogo.listarConteudos();

                        System.out.print("ID do conteúdo: ");
                        int idConteudo = lerInteiro(scanner);

                        Conteudo conteudo =
                                catalogo.buscarPorId(idConteudo);

                        if (conteudo != null) {
                            minhaLista.adicionarConteudo(conteudo);
                        } else {
                            System.out.println(
                                    "Conteúdo não encontrado."
                            );
                        }

                    } else if (opcaoLista == 2) {

                        minhaLista.listarConteudos();

                    } else {
                        System.out.println("Opção inválida.");
                    }

                    break;
                }

                // AVALIAR CONTEÚDO
                case 5: {

                    if (usuarioLogado == null) {
                        System.out.println("Faça login primeiro.");
                        break;
                    }

                    catalogo.listarConteudos();

                    System.out.print("ID do conteúdo: ");
                    int idAvaliacao = lerInteiro(scanner);

                    Conteudo conteudoAvaliacao =
                            catalogo.buscarPorId(idAvaliacao);

                    if (conteudoAvaliacao == null) {
                        System.out.println(
                                "Conteúdo não encontrado."
                        );
                        break;
                    }

                    System.out.print("Nota de 0 a 10: ");
                    int nota = lerInteiro(scanner);

                    System.out.print("Comentário: ");
                    String comentario = scanner.nextLine();

                    try {

                        Avaliacao avaliacao = new Avaliacao(
                                usuarioLogado,
                                conteudoAvaliacao,
                                nota,
                                comentario
                        );

                        avaliacaoService.cadastrarAvaliacao(avaliacao);

                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }

                    break;
                }

                // ASSISTIR TRAILER
                case 6: {

                    catalogo.listarConteudos();

                    System.out.print(
                            "Digite o ID do conteúdo: "
                    );

                    int idTrailer = lerInteiro(scanner);

                    Conteudo conteudoTrailer =
                            catalogo.buscarPorId(idTrailer);

                    if (conteudoTrailer == null) {
                        System.out.println(
                                "Conteúdo não encontrado."
                        );
                        break;
                    }

                    String trailerUrl =
                            conteudoTrailer.getTrailerUrl();

                    if (trailerUrl == null ||
                            trailerUrl.isBlank()) {

                        System.out.println(
                                "Este conteúdo não possui trailer cadastrado."
                        );
                        break;
                    }

                    System.out.println(
                            "Abrindo trailer de: " +
                                    conteudoTrailer.getTitulo()
                    );

                    try {

                        if (Desktop.isDesktopSupported() &&
                                Desktop.getDesktop()
                                        .isSupported(Desktop.Action.BROWSE)) {

                            Desktop.getDesktop().browse(
                                    new URI(trailerUrl)
                            );

                        } else {
                            System.out.println(
                                    "Abra o link no navegador: " +
                                            trailerUrl
                            );
                        }

                    } catch (Exception e) {

                        System.out.println(
                                "Não foi possível abrir o navegador."
                        );

                        System.out.println(
                                "Link: " + trailerUrl
                        );
                    }

                    break;
                }

                // BUSCAR POR CATEGORIA
                case 7: {

                    System.out.print(
                            "Digite o gênero desejado: "
                    );

                    String generoBusca = scanner.nextLine();

                    catalogo.listarPorGenero(generoBusca);

                    break;
                }

                // VER FICHA TÉCNICA
                case 8: {

                    System.out.println(
                            "\n===== FICHA TÉCNICA ====="
                    );

                    catalogo.listarConteudos();

                    System.out.print(
                            "\nDigite o ID do conteúdo: "
                    );

                    int idFicha = lerInteiro(scanner);

                    Conteudo conteudoFicha =
                            catalogo.buscarPorId(idFicha);

                    if (conteudoFicha == null) {
                        System.out.println(
                                "Conteúdo não encontrado."
                        );
                        break;
                    }

                    conteudoFicha.exibirDetalhes();

                    break;
                }
                case 9:
                    catalogo.exibirDestaque();
                    break;

                // SAIR DA CONTA
                case 10: {

                    if (usuarioLogado == null) {
                        System.out.println(
                                "Nenhum usuário está conectado."
                        );
                        break;
                    }

                    System.out.println(
                            "Até logo, " +
                                    usuarioLogado.getNome() + "!"
                    );

                    usuarioLogado = null;
                    minhaLista = null;

                    System.out.println(
                            "Logout realizado com sucesso!"
                    );

                    break;
                }

                // ENCERRAR PROGRAMA
                case 0: {

                    System.out.println(
                            "Encerrando o aplicativo..."
                    );

                    break;
                }

                default: {
                    System.out.println("Opção inválida.");
                }
            }

        } while (opcao != 0);

        scanner.close();
    }

    // Ler números sem deixar quebras de linha pendentes
    public static int lerInteiro(Scanner scanner) {

        while (!scanner.hasNextInt()) {
            System.out.print("Digite um número válido: ");
            scanner.nextLine();
        }

        int numero = scanner.nextInt();
        scanner.nextLine();

        return numero;
    }
}
