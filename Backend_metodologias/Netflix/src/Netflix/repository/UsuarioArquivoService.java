package Netflix.repository;

import Netflix.model.Usuario;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class UsuarioArquivoService {

    private final String arquivo = "usuarios.txt";

    public void salvarUsuarios(ArrayList<Usuario> usuarios) {
        try (BufferedWriter escritor =
                     new BufferedWriter(new FileWriter(arquivo))) {

            for (Usuario usuario : usuarios) {
                escritor.write(
                        usuario.getId() + ";" +
                                usuario.getNome() + ";" +
                                usuario.getEmail() + ";" +
                                usuario.getSenha()
                );

                escritor.newLine();
            }

            System.out.println("Usuários salvos com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }


    public ArrayList<Usuario> carregarUsuarios() {
        ArrayList<Usuario> usuariosCarregados = new ArrayList<>();

        try (BufferedReader leitor =
                     new BufferedReader(new FileReader(arquivo))) {

            String linha;

            while ((linha = leitor.readLine()) != null) {
                String[] dados = linha.split(";", -1);

                if (dados.length == 4) {
                    try {
                        int id = Integer.parseInt(dados[0]);

                        Usuario usuario = new Usuario(
                                id,
                                dados[1],
                                dados[2],
                                dados[3]
                        );

                        usuariosCarregados.add(usuario);

                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Um usuário possui ID inválido e foi ignorado."
                        );
                    }
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Nenhum arquivo de usuários encontrado. Iniciando vazio."
            );
        }

        return usuariosCarregados;
    }

}
