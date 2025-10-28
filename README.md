# 🧠 Projeto: Sistema Distribuído de Contagem em Java  
**Disciplina:** Programação Paralela e Distribuída  
**Período de desenvolvimento:** 24/10 a 27/10  
**Integrantes:**  
- 👨‍💻 Gustavo  
- 👨‍💻 Felipe  
- 👨‍💻 Leonardo

---

## 📘 Descrição do Projeto
O objetivo da atividade foi desenvolver um **sistema distribuído em Java** capaz de realizar a contagem de ocorrências de um número específico dentro de um vetor de inteiros.  
A arquitetura proposta envolveu:
- Um **Distribuidor (D)**: responsável por gerar o vetor, dividir em partes e distribuir tarefas.
- Vários **Receptores (R)**: responsáveis por processar as partes do vetor recebidas via TCP/IP e retornar o resultado parcial.  

A comunicação foi feita utilizando **serialização de objetos** e **conexões persistentes**, com sincronização por threads no Distribuidor.

---

## 🧾 Diário de Desenvolvimento

### 🗓️ Sexta-feira (24/10) — Início do Projeto
**Horário:** 19h00 – 23h30  
**Atividades:**
- **Leonardo:** criou o repositório, configurou o ambiente Java e estruturou os pacotes (`comunicacao`, `distribuidor`, `receptor`).
- **Gustavo:** implementou as classes `Comunicado`, `Pedido`, `Resposta` e `ComunicadoEncerramento` com `Serializable`.
- **Felipe:** testou a comunicação cliente-servidor com `ServerSocket` e `Socket`.
---

### 🗓️ Sábado (25/10) — Desenvolvimento Principal
**Horário:** 10h00 – 18h30  
**Atividades:**
- **Gustavo:** finalizou as classes de comunicação e testou o método `contar()` em `Pedido`.  
- **Felipe:** criou o servidor **Receptor (R)**, com logs informativos e loop de recebimento de pedidos.  
- **Leonardo:** iniciou o **Distribuidor (D)**, gerando o vetor principal, dividindo-o em partes e enviando para os Receptores via threads.

**Resultado:** sistema local funcional, com envio e resposta de pedidos corretos.
---

### 🗓️ Domingo (26/10) — Testes e Otimizações
**Horário:** 14h00 – 22h00  
**Atividades:**
- **Leonardo:** implementou `Thread.join()` e medição de tempo.  
- **Felipe:** aprimorou o Receptor para aceitar múltiplas conexões e encerrar corretamente com `ComunicadoEncerramento`.  
- **Gustavo:** criou o comparativo com versão **sem paralelismo**.  
---

### 🗓️ Segunda-feira (27/10) — Finalização e Entrega
**Horário:** 09h00 – 13h00  
**Atividades:**
- **Gustavo:** revisou e comentou o código.  
- **Leonardo:** capturou prints de execução e organizou os arquivos.  
- **Felipe:** testou o sistema em duas máquinas na mesma rede.  
---

## 🧩 Conclusão
O sistema foi concluído com sucesso, atendendo a todos os requisitos do enunciado.  
O grupo implementou corretamente a comunicação via TCP/IP, a serialização de objetos e o uso de threads para paralelismo real.  
O desempenho obtido comprovou a eficiência da distribuição de tarefas em relação à execução sequencial.

**Aprendizados principais:**
- Comunicação cliente-servidor via `Socket` e `ObjectStream`.
- Uso de `Thread` e `join()` para sincronização.  
- Manipulação de vetores grandes e análise de desempenho.  
- Importância do trabalho em equipe e divisão clara de responsabilidades.
---

## 🧠 Créditos
Desenvolvido por **Gustavo Lessio**, **Felipe Proença** e **Leonardo Danna**
📅 *PUC-Campinas — Outubro de 2025*  


