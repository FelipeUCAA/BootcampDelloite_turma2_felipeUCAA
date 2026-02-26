const form = document.getElementById("formUsuario");
const tabela = document.getElementById("tabelaUsuarios").getElementsByTagName("tbody")[0];
const mensagem = document.getElementById("mensagem");
const btnSair = document.getElementById("btnSair");
const btnBuscar = document.getElementById("btnBuscar");
const inputBuscar = document.getElementById("buscarId");
const resultadoBusca = document.getElementById("resultadoBusca");

// Modal e campos de edição
const editarModalEl = document.getElementById('editarModal');
const editarModal = new bootstrap.Modal(editarModalEl);
const formEditar = document.getElementById("formEditar");
const editarIdInput = document.getElementById("editarId");
const editarNome = document.getElementById("editarNome");
const editarEmail = document.getElementById("editarEmail");

let editarId = null;

// ======== Listar usuários ========
async function listarUsuarios() {
    tabela.innerHTML = '';
    try {
        const res = await fetch('/usuarios');
        const usuarios = await res.json();

        usuarios.forEach(u => {
            const row = tabela.insertRow();
            row.innerHTML = `
                <td>${u.id}</td>
                <td>${u.nome}</td>
                <td>${u.email}</td>
                <td>
                    <button class="btn btn-sm btn-warning btn-editar" data-id="${u.id}">Editar</button>
                    <button class="btn btn-sm btn-danger btn-excluir" data-id="${u.id}">Excluir</button>
                </td>
            `;
        });
        adicionarEventos();
    } catch (err) {
        console.error(err);
        mensagem.innerHTML = '<div class="alert alert-danger">Erro ao listar usuários</div>';
    }
}

// ======== Adicionar eventos aos botões ========
function adicionarEventos() {
    document.querySelectorAll(".btn-editar").forEach(btn => {
        btn.onclick = async () => {
            const id = btn.getAttribute("data-id");
            try {
                const res = await fetch(`/usuarios/${id}`);
                const usuario = await res.json();
                editarId = usuario.id;
                editarIdInput.value = usuario.id;
                editarNome.value = usuario.nome;
                editarEmail.value = usuario.email;
                editarModal.show(); // abre modal
            } catch (err) {
                console.error(err);
                mensagem.innerHTML = '<div class="alert alert-danger">Erro ao carregar usuário</div>';
            }
        };
    });

    document.querySelectorAll(".btn-excluir").forEach(btn => {
        btn.onclick = async () => {
            const id = btn.getAttribute("data-id");
            if (!confirm("Deseja realmente excluir este usuário?")) return;
            try {
                await fetch(`/usuarios/${id}`, { method: 'DELETE' });
                listarUsuarios();
                mensagem.innerHTML = '<div class="alert alert-success">Usuário excluído com sucesso!</div>';
            } catch (err) {
                console.error(err);
                mensagem.innerHTML = '<div class="alert alert-danger">Erro ao excluir usuário</div>';
            }
        };
    });
}

// ======== Criar usuário ========
form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const usuario = {
        nome: document.getElementById("nome").value,
        email: document.getElementById("email").value
    };
    try {
        await fetch('/usuarios', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(usuario)
        });
        form.reset();
        listarUsuarios();
        mensagem.innerHTML = '<div class="alert alert-success">Usuário criado com sucesso!</div>';
    } catch (err) {
        console.error(err);
        mensagem.innerHTML = '<div class="alert alert-danger">Erro ao criar usuário</div>';
    }
});

// editar usuario via modal
formEditar.addEventListener("submit", async (e) => {
    e.preventDefault();
    const usuario = {
        nome: editarNome.value,
        email: editarEmail.value
    };
    try {
        await fetch(`/usuarios/${editarId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(usuario)
        });
        editarModal.hide();
        listarUsuarios();
        mensagem.innerHTML = '<div class="alert alert-success">Usuário atualizado com sucesso!</div>';
    } catch (err) {
        console.error(err);
        mensagem.innerHTML = '<div class="alert alert-danger">Erro ao atualizar usuário</div>';
    }
});

//busca por id
btnBuscar.onclick = async () => {
    const id = inputBuscar.value.trim();
    if (!id) {
        resultadoBusca.innerHTML = '<div class="alert alert-warning">Informe um ID válido</div>';
        return;
    }
    try {
        const res = await fetch(`/usuarios/${id}`);
        if (res.status === 404) {
            resultadoBusca.innerHTML = '<div class="alert alert-danger">Usuário não encontrado</div>';
            return;
        }
        const u = await res.json();
        resultadoBusca.innerHTML = `<div class="alert alert-info">ID: ${u.id} | Nome: ${u.nome} | Email: ${u.email}</div>`;
    } catch (err) {
        console.error(err);
        resultadoBusca.innerHTML = '<div class="alert alert-danger">Erro na busca</div>';
    }
};

//logout
btnSair.onclick = () => {
    window.location.href = '/';
};

listarUsuarios();