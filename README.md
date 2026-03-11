# Escala de Designações - API

API desenvolvida em **Java 21** com **Spring Boot** para geração automática de **escalas de designações em Excel**.
A aplicação distribui pessoas em diferentes funções ao longo de um período de datas de forma **equilibrada e automatizada**.

## Funcionalidades

* Geração automática de escala por período
* Distribuição equilibrada das pessoas
* Evita repetição de funções em dias consecutivos
* Reduz repetição das mesmas duplas de trabalho
* Exportação da escala em **arquivo Excel (.xlsx)**

## Tecnologias

* **Java 21**
* **Spring Boot**
* **Apache POI** (geração de Excel)
* **Maven**

## Estrutura da Escala Gerada

| Data       | Dia da Semana | Função 1 | Função 2 | Função 3 |
| ---------- | ------------- | -------- | -------- | -------- |
| 01/05/2026 | Sexta-feira   | João     | Maria    | Pedro    |
| 02/05/2026 | Sábado        | Ana      | João     | Maria    |

## Regras de Distribuição

A geração da escala considera:

* **Carga de trabalho**: prioriza quem trabalhou menos
* **Função anterior**: evita repetir a mesma função em dias seguidos
* **Histórico de duplas**: reduz repetição de combinações
* **Desempate aleatório** para manter variedade

## Resultado

A API retorna um **arquivo Excel gerado dinamicamente**, pronto para download ou envio.

## Objetivo

Automatizar a criação de escalas de equipe, garantindo **justiça na distribuição** e **redução de trabalho manual**.
