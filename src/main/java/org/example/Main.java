package org.example;

import model.UsuarioModel;
import repository.UsuarioRepository;
import service.UsuarioService;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    private static final UsuarioService service = new UsuarioService(new UsuarioRepository());
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            mostrarMenu();
            int opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> criarUsuario();
                case 2 -> listarUsuarios();
                case 3 -> buscarUsuario();
                case 4 -> atualizarUsuario();
                case 5 -> removerUsuario();
                case 6 -> {
                    System.out.println("Saindo...");
                    running = false;
                }
                default -> System.out.println("Opção inválida!");
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("====== MENU ======");
        System.out.println("1 - Criar usuário");
        System.out.println("2 - Listar usuários");
        System.out.println("3 - Buscar usuário por ID");
        System.out.println("4 - Atualizar usuário");
        System.out.println("5 - Remover usuário");
        System.out.println("6 - Sair");
    }

    private static int lerInteiro(String msg) {
        System.out.print(msg);
        while (!scanner.hasNextInt()) {
            System.out.print("Digite um número válido: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    private static void criarUsuario() {
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine().trim();

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            service.criar(nome, email);
            System.out.println("Usuário criado com sucesso!");

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void listarUsuarios() {
        List<UsuarioModel> usuarios = service.listar();
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            System.out.println("ID | Nome | Email");
            for (UsuarioModel u : usuarios) {
                System.out.println(u.getId() + " | " + u.getNome() + " | " + u.getEmail());
            }
        }
    }

    private static void buscarUsuario() {
        long id = lerInteiro("Digite o ID do usuário: ");
        UsuarioModel u = service.buscar(id);
        if (u != null) {
            System.out.println("Encontrado: " + u.getId() + " | " + u.getNome() + " | " + u.getEmail());
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    private static void atualizarUsuario() {
        long id = lerInteiro("ID do usuário a atualizar: ");
        UsuarioModel u = service.buscar(id);
        if (u == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        System.out.print("Novo nome (" + u.getNome() + "): ");
        String nome = scanner.nextLine();
        if (nome.isBlank()) nome = u.getNome();

        System.out.print("Novo email (" + u.getEmail() + "): ");
        String email = scanner.nextLine();
        if (email.isBlank()) email = u.getEmail();

        boolean ok = service.atualizar(id, nome, email);
        System.out.println(ok ? "Usuário atualizado!" : "Erro ao atualizar usuário.");
    }

    private static void removerUsuario() {
        long id = lerInteiro("ID do usuário a remover: ");
        UsuarioModel u = service.buscar(id);
        if (u == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }
        boolean ok = service.deletar(id);
        System.out.println(ok ? "Usuário removido!" : "Usuário não encontrado.");
    }
}
