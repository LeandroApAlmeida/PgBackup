<h3>Serviço de Backup do PostgreSQL:</h3>

<br>

Este programa é escrito em Java, porém está configurado para rodar apenas em Windows nesta versão 1.2. Desta forma, algumas configurações são necessárias.

Na diretório raiz do programa, copie os seguintes arquivos, juntamente com os .jars do projeto:

<br>

![Captura de tela 2025-01-24 161519](https://github.com/user-attachments/assets/b32ffbe8-be9d-419a-afc6-fa816c907176)

<br>

<ul>

<li><b>Config.vbs</b>: programa para configuração no ambiente Microsoft Windows;</li>

<li><b>jacob-1.20-x64.dll</b>: biblioteca de vínculo dinâmico do JACOB (JAVA-COM Bridge) para arquiteturas x64;</li>

<li><b>jacob-1.20-x86.dll</b>: biblioteca de vínculo dinâmico do JACOB para arquiteturas x86;</li>

<li><b>PostgreBackup.bat</b>: arquivo de execução do serviço, usando a JVM instalada no Windows.</li>
  
</ul>

<br>

<b>Config.vbs</b> e <b>PostgreBackup.bar</b> se encontram no diretória Res, e <b>jacob-1.20-x64.dll</b> e <b>jacob-1.20-x86.dll</b> no diretório raiz do projeto. Os arquivos com extensão .xml são criados automaticamente pelo serviço, com base nas configurações feitas pelo administrador e com a execução dos backups.

<br>

https://github.com/user-attachments/assets/5733fd97-32c5-4a98-b3bf-c76cebe07f21

<br>

Para configurar o serviço de backup para inicializar automaticamente com o Windows, é necessário dar duplo clique sobre o programa Config.vbs, quando este já foi copiado para o diretório raiz. Feito isso, ele cria uma entrada na chave de registro <b>HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Run</b>, com o nome <b>PgBackup</b>, não exigindo permissões de administrador do sistema para tal. Eu utilizo um Serviço criado em C# para esta finalidade, porém não vou disponibilizar o fonte do mesmo.

<br>

<h3>Backup do Banco de Dados:</h3>

<br>

Ao executar o programa, ele cria um ícone na barra de tarefas do Windows. Clicando com o botão direito do mouse no ícone e escolhendo a opção <b>Configurar...</b> tem-se acesso às configurações do serviço. O backup pode ser em drive USB local ou drive de rede. No caso de drive de rede, deve-se escolher o drive na lista.

O Backup pode ser realizado a qualquer momento, clicando com o botão esquerdo do mouse. A forma padrão é o backup em dias e horários agendados.

https://github.com/user-attachments/assets/d05527e5-da0b-4fcc-941d-01d3e5745a48

<br>

Para fazer o backup em um drive removível conectado a uma porta USB, é necessário mudar a configuração do serviço. Logo após isso, se exigirá que o drive seja conectado ao computador toda vez que for realizar o backup.

https://github.com/user-attachments/assets/15e632e3-27b3-446d-ab6e-1e78fe85b4ee

<br>

<h3>Restauração do Banco de Dados:</h3>

<br>

Para restaurar o banco de dados, deve-se criar o banco de dados vazio antes, com o mesmo nome definido no backup. Logo após isso, clicando com o botão direito do mouse no ícone do programa e escolhendo a opção <b>Restaurar o backup</b>, pode se selecionar o arquivo de backup a ser restaurado e proceder à restauração.

https://github.com/user-attachments/assets/2a58ddef-f698-4b28-90b9-f88c133ffede
