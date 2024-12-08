const pessoaForm = document.getElementById('pessoaForm');
const errorMessage = document.getElementById('errorMessage');
const pessoaList = document.getElementById('pessoaList');
const enderecoModal = document.getElementById('enderecoModal');
const enderecoList = document.getElementById('enderecoList');
const closeModal = document.getElementById('closeModal');
const enderecosContainer = document.getElementById('enderecosContainer');

document.getElementById('addEndereco').addEventListener('click', () => {
    const enderecoDiv = document.createElement('div');
    enderecoDiv.classList.add('endereco-group');
    enderecoDiv.innerHTML = `
        <h4> Endereço </h4>
        <input type="number" placeholder="Código Bairro" class="codigoBairro" required>
        <input type="text" placeholder="Nome Rua" class="nomeRua" required>
        <input type="text" placeholder="Número" class="numero" required>
        <input type="text" placeholder="Complemento" class="complemento">
        <input type="text" placeholder="CEP" class="cep" required>
    `;
    enderecoDiv.style.marginBottom = '10px';
    enderecosContainer.appendChild(enderecoDiv);
});

function fetchPessoas() {
    axios.get('http://localhost:8080/pessoa')
        .then(response => {
            const pessoas = response.data;
            pessoaList.innerHTML = '';
            pessoas.forEach(pessoa => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${pessoa.codigoPessoa}</td>
                    <td>${pessoa.nome}</td>
                    <td>${pessoa.sobrenome}</td>
                    <td>${pessoa.idade}</td>
                    <td>${pessoa.status === 1 ? 'Ativo' : 'Inativo'}</td>
                `;
                row.addEventListener('click', () => showEnderecos(pessoa.codigoPessoa));
                pessoaList.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Erro ao buscar Pessoas:', error);
        });
}

function showEnderecos(codigoPessoa) {
    axios.get(`http://localhost:8080/pessoa?codigoPessoa=${codigoPessoa}`)
        .then(response => {
            const pessoa = response.data;
            enderecoList.innerHTML = '';
            pessoa.enderecos.forEach(endereco => {
                const listItem = document.createElement('li');
                listItem.textContent = `
                    Rua: ${endereco.nomeRua}, Número: ${endereco.numero}, Complemento: ${endereco.complemento}, CEP: ${endereco.cep}
                `;
                enderecoList.appendChild(listItem);
            });
            enderecoModal.style.display = 'block';
        })
        .catch(error => {
            console.error('Erro ao buscar endereços:', error);
        });
}

closeModal.addEventListener('click', () => {
    enderecoModal.style.display = 'none';
});

function savePessoa(event) {
    event.preventDefault();
    const codigoPessoa = document.getElementById('codigoPessoa').value;
    const nome = document.getElementById('nome').value.trim();
    const sobrenome = document.getElementById('sobrenome').value.trim();
    const idade = document.getElementById('idade').value;
    const login = document.getElementById('login').value.trim();
    const senha = document.getElementById('senha').value;
    const status = document.getElementById('status').value;

    const enderecoElements = document.querySelectorAll('.endereco-group');
    const enderecos = Array.from(enderecoElements).map(group => ({
        codigoBairro: group.querySelector('.codigoBairro').value,
        nomeRua: group.querySelector('.nomeRua').value.trim(),
        numero: group.querySelector('.numero').value.trim(),
        complemento: group.querySelector('.complemento').value.trim(),
        cep: group.querySelector('.cep').value.trim()
    }));

    const pessoaData = { nome, sobrenome, idade, login, senha, status, enderecos };
    if (codigoPessoa) pessoaData.codigoPessoa = codigoPessoa;

    const request = codigoPessoa
        ? axios.put('http://localhost:8080/pessoa', pessoaData)
        : axios.post('http://localhost:8080/pessoa', pessoaData);

    request
        .then(() => {
            errorMessage.textContent = '';
            pessoaForm.reset();
            fetchPessoas();
            alert('Pessoa salva com sucesso!');
        })
        .catch(error => {
            if (error.response && error.response.status === 404) {
                errorMessage.textContent = error.response.data.mensagem;
            } else {
                console.error('Erro ao salvar Pessoa:', error);
            }
        });
}

pessoaForm.addEventListener('submit', savePessoa);
fetchPessoas();