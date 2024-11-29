
# MenuGuru - Customer

Este repositório implementa o microserviço Menuguru Customer de um sistema fictício de gestão de pedidos para restaurantes (MenuGuru) como parte do trabalho de avaliação do curso de Pós Graduação em Software Architecture da FIAP.

O MenuGuru Customer tem como objetivo principal registrar e manter o cadastro de clientes do sistema Menuguru.

Funcionalidades:
* Cadastro e Identificação de Clientes

## Projetos relacionados

O projeto foi estruturado em 5 repositórios, cada um com o objetivo de armazenar os códigos fontes e scripts de cada estrutura da arquitetura do projeto. Cada repositório pode ser acessado nos seguintes links:
* **Aplicação (backend):** repositório com o código Java da aplicação executada em ambiente Kubernetes (*este repositório*).
* **Banco de dados:** repositório contendo os scripts de Terraform para criação do banco de dados MySQL executado na AWS RDS: [https://github.com/perisatto/menuguru-database](https://github.com/perisatto/menuguru-database).
* **Infraestrutura (Kubernetes):** repositório contendo os scripts de Terraform para criação do cluster EKS: [https://github.com/perisatto/menuguru-kubernetes](https://github.com/perisatto/menuguru-kubernetes).
* **Função Lambda:** repositório com o código fonte Java + template SAM para a criação de função Lambda utilizada para autenticação: [https://github.com/perisatto/menuguru-lambda](https://github.com/perisatto/menuguru-lambda).
* **API Gateway:** repositório contendo os scripts de Terraform para o API Gateway utilizado para expor e garantir validação da autenticação das rotas da aplicação backend: [https://github.com/perisatto/menuguru-api-gateway](https://github.com/perisatto/menuguru-api-gateway).


# Arquitetura

Na versão mais atual do projeto o Menuguru foi estruturado para ser executado em uma arquitetura de Microsserviços. 

### Desenho de Infraestrutura

Abaixo estão representadas a infraestrutura padrão para o Menuguru. O desenho demonstra a implantação em ambiente AWS mas você pode executar o projeto em um provedor Cloud de sua preferência, realizando as devidas adaptações.

![Desenho de Infraestrutura](/images/DIF_AWS.png)

Abstraímos aqui os componentes do Kubernetes (ControlPlane, etcd, etc) para focar apenas nos componentes da aplicação.


# Cobertura de Testes

Para verificação de cobertura de testes foi utilizado o serviço Sonar Cloud. O percentual de cobertura de testes deste projeto foi de 81.1%, podendo ser verificado na imagem abaixo ou diretamente no [Sonar Cloud](https://sonarcloud.io/project/overview?id=perisatto_menuguru-customer):

![SonarCloud - Cobertura de Testes](/images/Cobertura.png)


# Demonstração

Para demonstrar a arquitetura do projeto e a execução do mesmo em ambiente AWS, disponibilizamos o video no link abaixo com a explicação do projeto e de sua execução.

* [YouTube](https://youtu.be/RLnS_9L-QcA)
