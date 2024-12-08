const municipioForm = document.getElementById('municipioForm');
const errorMessage = document.getElementById('errorMessage');
const municipioList = document.getElementById('municipioList');

function fetchMunicipios() {
    axios.get('http://localhost:8080/municipio')
        .then(response => {
            const municipios = response.data;
            municipioList.innerHTML = '';
            municipios.forEach(municipio => {
                const row = `<tr>
                    <td>${municipio.codigoMunicipio}</td>
                    <td>${municipio.codigoUF}</td>
                    <td>${municipio.nome}</td>
                    <td>${municipio.status === 1 ? 'Ativo' : 'Inativo'}</td>
                </tr>`;
                municipioList.innerHTML += row;
            });
        })
        .catch(error => {
            console.error('Erro ao buscar Municípios:', error);
        });
}

function saveMunicipio(event) {
    event.preventDefault();
    const codigoMunicipio = document.getElementById('codigoMunicipio').value;
    const codigoUF = document.getElementById('codigoUF').value;
    const nome = document.getElementById('nome').value.trim();
    const status = document.getElementById('status').value;

    const municipioData = { codigoUF, nome, status };
    if (codigoMunicipio) {
        municipioData.codigoMunicipio = codigoMunicipio;
    }

    const request = codigoMunicipio
        ? axios.put('http://localhost:8080/municipio', municipioData)
        : axios.post('http://localhost:8080/municipio', municipioData);

    request
        .then(() => {
            errorMessage.textContent = '';
            municipioForm.reset();
            fetchMunicipios();
            alert('Município salvo com sucesso!');
        })
        .catch(error => {
            if (error.response && error.response.status === 404) {
                errorMessage.textContent = error.response.data.mensagem;
            } else {
                console.error('Erro ao salvar Município:', error);
            }
        });
}

municipioForm.addEventListener('submit', saveMunicipio);
fetchMunicipios();