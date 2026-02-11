package validation;

public class UsuarioValidator {

    public static void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }

        if (!nome.matches("[a-zA-ZÀ-ÿ ]+")) {
            throw new IllegalArgumentException("Nome deve conter apenas letras");
        }
    }

    public static void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new IllegalArgumentException("Email inválido");
        }
    }
}