package Netflix.service;

import Netflix.model.MinhaLista;
import Netflix.model.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsuarioService {

    private ArrayList<Usuario> usuarios;
    private Map<Integer, MinhaLista> listasUsuarios;
    private int proximoId;

    public UsuarioService() {
        usuarios = new ArrayList<>();
        listasUsuarios = new HashMap<>();
        proximoId = 1;
    }

    public Usuario cadastrarUsuario(
            String nome, String email, String senha) {

        if (nome == null || nome.isBlank()
                || email == null || email.isBlank()
                || senha == null || senha.isBlank()) {

            System.out.println("Preencha todos os campos.");
            return null;
        }

        if (buscarPorEmail(email) != null) {
            System.out.println("E-mail já cadastrado!");
            return null;
        }

        Usuario usuario = new Usuario(
                proximoId, nome, email, senha
        );

        usuarios.add(usuario);

        listasUsuarios.put(
                usuario.getId(),
                new MinhaLista(usuario)
        );

        proximoId++;

        System.out.println("Usuário cadastrado com sucesso!");

        return usuario;
    }

    public Usuario buscarPorEmail(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                return usuario;
            }
        }

        return null;
    }

    public boolean fazerLogin(String email, String senha) {
        Usuario usuario = buscarPorEmail(email);

        if (usuario != null && usuario.getSenha().equals(senha)) {
            System.out.println("Login realizado com sucesso!");
            return true;
        }

        System.out.println("E-mail ou senha incorretos.");
        return false;
    }

    public MinhaLista buscarMinhaLista(int idUsuario) {
        return listasUsuarios.get(idUsuario);
    }

    public void listarUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        for (Usuario usuario : usuarios) {
            System.out.println("--------------------");
            usuario.exibirDados();
        }
    }

    public ArrayList<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public void carregarUsuario(Usuario usuario) {
        if (usuario == null) {
            return;
        }

        if (buscarPorEmail(usuario.getEmail()) != null) {
            return;
        }

        usuarios.add(usuario);

        listasUsuarios.put(
                usuario.getId(),
                new MinhaLista(usuario)
        );

        if (usuario.getId() >= proximoId) {
            proximoId = usuario.getId() + 1;
        }
    }

}
