const ufForm = document.getElementById('ufForm');
const errorMessage = document.getElementById('errorMessage');
const ufList = document.getElementById('ufList');

function fetchUFs() {
    axios.get('http://localhost:8080/uf')
        .then(response => {
            const ufs = response.data;
            ufList.innerHTML = '';
            ufs.forEach(uf => {
                const row = `<tr>
                    <td>${uf.codigoUF}</td>
                    <td>${uf.sigla}</td>
                    <td>${uf.nome}</td>
                    <td>${uf.status === 1 ? 'Ativo' : 'Inativo'}</td>
                </tr>`;
                ufList.innerHTML += row;
            });
        })
        .catch(error => {
            console.error('Erro ao buscar UFs:', error);
        });
}

function saveUF(event) {
    event.preventDefault();
    const codigoUF = document.getElementById('codigoUF').value;
    const sigla = document.getElementById('sigla').value.trim();
    const nome = document.getElementById('nome').value.trim();
    const status = document.getElementById('status').value;

    const ufData = { sigla, nome, status };
    if (codigoUF) {
        ufData.codigoUF = codigoUF;
    }

    const request = codigoUF
        ? axios.put('http://localhost:8080/uf', ufData)
        : axios.post('http://localhost:8080/uf', ufData);

    request
        .then(() => {
            errorMessage.textContent = '';
            ufForm.reset();
            fetchUFs();
            alert('UF salva com sucesso!');
        })
        .catch(error => {
            if (error.response && error.response.status === 404) {
                errorMessage.textContent = error.response.data.mensagem;
            } else {
                console.error('Erro ao salvar UF:', error);
            }
        });
}

ufForm.addEventListener('submit', saveUF);
fetchUFs();
