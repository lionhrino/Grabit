GRAB!T 🍔🛵

Sobre o Projeto

A GRAB!T é uma aplicação móvel nativa de entregas ao domicílio (food delivery), desenvolvida no âmbito da Unidade Curricular de Programação de Aplicação do Lado do Cliente no Instituto Politécnico de Beja.

O principal objetivo do projeto é oferecer uma solução digital interativa, rápida e intuitiva no setor do delivery. Através da transição de uma lógica prévia de desktop para um ambiente mobile nativo, a GRAB!T liga de forma eficiente três vertentes distintas: o Cliente Final, o Restaurante (parceiro) e o Estafeta.

Para além da aplicação móvel, foi também desenvolvido um website promocional de apresentação do projeto, focado na captação de parceiros e na demonstração das principais funcionalidades da plataforma.

Funcionalidades

A aplicação disponibiliza interfaces e permissões segmentadas de acordo com o perfil do utilizador autenticado.

🧑 Clientes

Registo e autenticação segura de utilizadores;

Navegação por categorias de alimentação (Burgers, Pizzas, Sushi, Healthy);

Pesquisa dinâmica de restaurantes e ementas;

Sistema de Carrinho de Compras com gestão de estado em tempo real;

Carteira Digital (Wallet) para carregamento de saldo e pagamentos rápidos;

Histórico de encomendas e acompanhamento de pedidos ativos.

🏪 Restaurantes (Empresas)

Gestão centralizada de Cardápio (CRUD): Adição, edição e remoção de pratos;

Painel de controlo de encomendas recebidas;

Gestão de estados de preparação (Na Cozinha / Pronto a recolher);

Consulta de ganhos acumulados e histórico de vendas na plataforma.

🛵 Estafetas

Central de entregas disponíveis em tempo real;

Aceitação de serviços e gestão de estados de transporte;

Carteira de ganhos com creditação automática após entrega concluída;

Interface otimizada para utilização em trânsito (botões de grande dimensão).

Tecnologias Utilizadas

Aplicação Móvel

Linguagem: Kotlin

Framework: Jetpack Compose (UI Declarativa e Reativa)

Arquitetura: MVVM (Model-View-ViewModel)

Concorrência: Kotlin Coroutines & Flow

Base de Dados

Persistência Local: Room Database (SQLite)

Estrutura relacional com chaves estrangeiras entre utilizadores, produtos e encomendas.

Ferramentas de Desenvolvimento e Design

Android Studio (IDE Principal)

GitHub (Controlo de versões e colaboração)

HTML / CSS (Desenvolvimento do Website Promocional)

Discord (Gestão de equipa e reuniões diárias)

Figma (Prototipagem e Identidade Visual)

Arquitetura

A aplicação foi desenvolvida recorrendo ao padrão arquitetural MVVM (Model-View-ViewModel). Esta escolha permitiu uma separação clara de responsabilidades:

Model: Representação das entidades da base de dados e lógica de dados (Room).

View: Interface construída com Jetpack Compose que reage automaticamente a mudanças de estado.

ViewModel: Camada intermédia que gere o estado da UI e comunica com o repositório de dados, garantindo que a informação não se perde durante rotações de ecrã ou mudanças de ciclo de vida.

Estrutura do Projeto

A organização de pastas segue as melhores práticas de desenvolvimento Android:

app/src/main/java/com/example/grabt/
├── dao             # Interfaces de acesso aos dados (Room DAOs)
├── database        # Configuração da base de dados (AppDatabase)
├── model           # Entidades e modelos de dados (Entities)
├── view            # Ecrãs e componentes visuais (Jetpack Compose Screens)
├── viewmodel       # Lógica de negócio e gestão de estado
└── ui.theme        # Definições de design system (Cores, Tipografia, Formas)


Objetivo

O foco central da GRAB!T é a Prevenção de Erros e a Eficiência. Utilizando os princípios de UX de Nielsen e Shneiderman, a interface foi desenhada para reduzir o número de cliques necessários para completar uma ação, oferecendo feedback constante através de estados de carregamento e validações em tempo real.

Demonstração

O ecossistema do projeto é composto por:

Aplicação Android Nativa (.apk);

Website Institucional (Promoção e suporte);

Screencast Demonstrativo das funcionalidades de ponta a ponta.

Equipa

Projeto desenvolvido por alunos de Engenharia Informática do Instituto Politécnico de Beja:

Gustavo Santos – 27964

André Noronha – 27976

Filipe Lino – 27978

Licença

Este projeto foi desenvolvido exclusivamente para fins académicos e de avaliação no âmbito da Unidade Curricular de Programação de Aplicação do Lado do Cliente.
