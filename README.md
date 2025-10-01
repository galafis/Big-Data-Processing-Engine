# Big-Data-Processing-Engine

![Hero Image](docs/hero_image.png)

[![Java](https://img.shields.io/badge/Java-17-blue?logo=java&logoColor=white)](https://www.java.com/)
[![Maven](https://img.shields.io/badge/Maven-3.8.6-red?logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen)](./src/test/java/com/galafis/bigdataprocessingengine/BigDataProcessingSystemTest.java)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

## English

## Overview

This project, **Big-Data-Processing-Engine**, developed by Gabriel Demetrios Lafis, is an enterprise-grade system designed for efficient processing and analysis of large datasets. It leverages modern Java features and concurrent programming to handle data records, generate insightful summaries, and provide actionable recommendations.

### Features

*   **Data Ingestion**: Efficiently handles the collection and storage of data records.
*   **Concurrent Processing**: Utilizes `ExecutorService` for parallel data processing, ensuring high throughput.
*   **Data Analysis**: Calculates summary statistics (average, min, max, total records) and identifies key insights.
*   **Recommendation Engine**: Generates data-driven recommendations based on analysis.
*   **Modular Design**: Built with a clear separation of concerns, making it extensible and maintainable.

### Architecture

The system follows a modular architecture, as illustrated below:

![Architecture Diagram](docs/diagrams/architecture.png)

### Workflow

The data processing workflow involves several stages, from ingestion to final reporting:

![Workflow Diagram](docs/diagrams/workflow.png)

### Getting Started

To get a local copy up and running, follow these simple steps.

#### Prerequisites

*   Java Development Kit (JDK) 17 or higher
*   Apache Maven 3.8.6 or higher

#### Installation

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/GabrielDemetriosLafis/Big-Data-Processing-Engine.git
    cd Big-Data-Processing-Engine
    ```

2.  **Build the project:**

    ```bash
    mvn clean install
    ```

#### Usage

To run the system and see it in action, execute the main class:

```bash
java -jar target/big-data-processing-engine-1.0.0-jar-with-dependencies.jar
```

This will initialize the system with sample data, process it, and print the analysis results to the console.

## Português

## Visão Geral

Este projeto, **Big-Data-Processing-Engine**, desenvolvido por Gabriel Demetrios Lafis, é um sistema de nível empresarial projetado para o processamento e análise eficientes de grandes conjuntos de dados. Ele aproveita os recursos modernos do Java e a programação concorrente para lidar com registros de dados, gerar resumos perspicazes e fornecer recomendações acionáveis.

### Funcionalidades

*   **Ingestão de Dados**: Lida eficientemente com a coleta e armazenamento de registros de dados.
*   **Processamento Concorrente**: Utiliza `ExecutorService` para processamento paralelo de dados, garantindo alta taxa de transferência.
*   **Análise de Dados**: Calcula estatísticas de resumo (média, mínimo, máximo, total de registros) e identifica insights chave.
*   **Mecanismo de Recomendação**: Gera recomendações baseadas em dados a partir da análise.
*   **Design Modular**: Construído com uma clara separação de preocupações, tornando-o extensível e de fácil manutenção.

### Arquitetura

O sistema segue uma arquitetura modular, conforme ilustrado abaixo:

![Diagrama de Arquitetura](docs/diagrams/architecture.png)

### Fluxo de Trabalho

O fluxo de trabalho de processamento de dados envolve várias etapas, desde a ingestão até o relatório final:

![Diagrama de Fluxo de Trabalho](docs/diagrams/workflow.png)

### Primeiros Passos

Para ter uma cópia local e funcionando, siga estes passos simples.

#### Pré-requisitos

*   Java Development Kit (JDK) 17 ou superior
*   Apache Maven 3.8.6 ou superior

#### Instalação

1.  **Clone o repositório:**

    ```bash
    git clone https://github.com/GabrielDemetriosLafis/Big-Data-Processing-Engine.git
    cd Big-Data-Processing-Engine
    ```

2.  **Construa o projeto:**

    ```bash
    mvn clean install
    ```

#### Uso

Para executar o sistema e vê-lo em ação, execute a classe principal:

```bash
java -jar target/big-data-processing-engine-1.0.0-jar-with-dependencies.jar
```

Isso inicializará o sistema com dados de exemplo, os processará e imprimirá os resultados da análise no console.

