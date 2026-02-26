const formLogin = document.getElementById("formLogin");
const mensagemLogin = document.getElementById("mensagemLogin");

formLogin.addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("loginEmail").value.trim();
    const senha = document.getElementById("loginSenha").value.trim();

    if (!email || !senha) {
        mensagemLogin.innerHTML = '<div class="alert alert-warning">Preencha todos os campos!</div>';
        return;
    }

    try {
        mensagemLogin.innerHTML = '<div class="alert alert-success">Login realizado com sucesso!</div>';
        setTimeout(() => window.location.href = '/index', 1000);
    } catch (err) {
        console.error(err);
        mensagemLogin.innerHTML = '<div class="alert alert-danger">Erro no login</div>';
    }
});