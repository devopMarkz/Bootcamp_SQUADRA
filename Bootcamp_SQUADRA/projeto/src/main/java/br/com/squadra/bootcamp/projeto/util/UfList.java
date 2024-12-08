package br.com.squadra.bootcamp.projeto.util;

import java.util.Map;

/**
 * Classe utilitária que armazena os estados brasileiros (UFs) e suas respectivas siglas.
 * <p>
 * Esta classe serve como uma referência estática para validação de estados válidos no sistema,
 * impedindo que usuários cadastrem estados inexistentes.
 * </p>
 */
public class UfList {

    /**
     * Mapa estático contendo o nome completo dos estados brasileiros como chave e suas siglas como valor.
     * <p>
     * Exemplo de entrada:
     * - Chave: "ACRE", Valor: "AC"
     * - Chave: "SÃO PAULO", Valor: "SP"
     * </p>
     * Este mapa é imutável e pode ser utilizado para validação de dados ou exibição.
     */
    public static final Map<String, String> mapUfs = Map.ofEntries(
            Map.entry("ACRE", "AC"),
            Map.entry("ALAGOAS", "AL"),
            Map.entry("AMAPÁ", "AP"),
            Map.entry("AMAZONAS", "AM"),
            Map.entry("BAHIA", "BA"),
            Map.entry("CEARÁ", "CE"),
            Map.entry("DISTRITO FEDERAL", "DF"),
            Map.entry("ESPÍRITO SANTO", "ES"),
            Map.entry("GOIÁS", "GO"),
            Map.entry("MARANHÃO", "MA"),
            Map.entry("MATO GROSSO", "MT"),
            Map.entry("MATO GROSSO DO SUL", "MS"),
            Map.entry("MINAS GERAIS", "MG"),
            Map.entry("PARÁ", "PA"),
            Map.entry("PARAÍBA", "PB"),
            Map.entry("PARANÁ", "PR"),
            Map.entry("PERNAMBUCO", "PE"),
            Map.entry("PIAUÍ", "PI"),
            Map.entry("RIO DE JANEIRO", "RJ"),
            Map.entry("RIO GRANDE DO NORTE", "RN"),
            Map.entry("RIO GRANDE DO SUL", "RS"),
            Map.entry("RONDÔNIA", "RO"),
            Map.entry("RORAIMA", "RR"),
            Map.entry("SANTA CATARINA", "SC"),
            Map.entry("SÃO PAULO", "SP"),
            Map.entry("SERGIPE", "SE"),
            Map.entry("TOCANTINS", "TO")
    );

}
