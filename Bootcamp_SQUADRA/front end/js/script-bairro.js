const bairroForm = document.getElementById('bairroForm');
const errorMessage = document.getElementById('errorMessage');
const bairroList = document.getElementById('bairroList');

function fetchBairros() {
    axios.get('http://localhost:8080/bairro')
        .then(response => {
            const bairros = response.data;
            bairroList.innerHTML = '';
            bairros.forEach(bairro => {
                const row = `<tr>
                    <td>${bairro.codigoBairro}</td>
                    <td>${bairro.codigoMunicipio}</td>
                    <td>${bairro.nome}</td>
                    <td>${bairro.status === 1 ? 'Ativo' : 'Inativo'}</td>
                </tr>`;
                bairroList.innerHTML += row;
            });
        })
        .catch(error => {
            console.error('Erro ao buscar Bairros:', error);
        });
}

function saveBairro(event) {
    event.preventDefault();
    const codigoBairro = document.getElementById('codigoBairro').value;
    const codigoMunicipio = document.getElementById('codigoMunicipio').value;
    const nome = document.getElementById('nome').value.trim();
    const status = document.getElementById('status').value;

    const bairroData = { codigoMunicipio, nome, status };
    if (codigoBairro) {
        bairroData.codigoBairro = codigoBairro;
    }

    const request = codigoBairro
        ? axios.put('http://localhost:8080/bairro', bairroData)
        : axios.post('http://localhost:8080/bairro', bairroData);

    request
        .then(() => {
            errorMessage.textContent = '';
            bairroForm.reset();
            fetchBairros();
            alert('Bairro salvo com sucesso!');
        })
        .catch(error => {
            if (error.response && error.response.status === 404) {
                errorMessage.textContent = error.response.data.mensagem;
            } else {
                console.error('Erro ao salvar Bairro:', error);
            }
        });
}

bairroForm.addEventListener('submit', saveBairro);
fetchBairros();
