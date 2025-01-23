<h3>Funcionamento do programa:</h3>

<br>

![Captura de tela 2025-01-23 121501](https://github.com/user-attachments/assets/c6fcb07b-2439-430b-995a-6c6d8e28b977)


O ícone do programa é criado na barra de tarefas do Windows.

<br>

![Captura de tela 2025-01-23 122007](https://github.com/user-attachments/assets/1769051b-d820-40c0-b67f-7f36ae1076c6)

Clicando com o botão direito do mouse, tem-se acesso ao menu do programa. Para configurar, clique em <b>Configurar...</b>

<br>

![Captura de tela 2025-01-23 122041](https://github.com/user-attachments/assets/5595d2ef-b714-4e14-abe9-587d4c152b62)

Na seção <b>Postgre Server</b>, deve-se informar os dados sobre a conexão com o banco de dados. Na guia <b>Parâmetros Gerais</b>, configura-se o modo de backup do banco de dados. Em <b>Destino do Backup</b>, deve se selecionar a opção <b>Drive de rede</b> ou <b>Drive removível</b>. No caso de <b>Drive de rede</b>, é necessário selecionar o drive de destino do backup na lista.

<br>

![Captura de tela 2025-01-23 122145](https://github.com/user-attachments/assets/766ad957-5d42-402d-a554-935eafb3c453)

Na guia <b>Backup Automático</b>, deve se marcar a opção <b>habilitar o backup automático do Banco de Dados (recomendável)</b> para realizar o backup do banco de dados nos dias e horários definidos. Na seção <b>Dias da Semana</b>, pode se selecionar em quais dias da semana será realizado o Backup do banco de dados. Na seção <b>Horários</b>, são definidos os horários que serão feito o backup.

<br>

![Captura de tela 2025-01-23 122208](https://github.com/user-attachments/assets/b9377eb3-f7f9-431b-9fa8-01b7591ac0f9)

Na guia <b>Ferramentas</b>, no campo <b>Programa para o Backup (pg_dump)</b>, deve-se localizar o local de instalação do PostgreSQL e selecionar o programa <b>pg_dump.exe</b>, no diretório <b>bin</b>. No campo <b>Programa para o Restore (pg_restore)</b>, deve-se localizar o local de instalação do PostgreSQL e selecionar o programa <b>pg_restore.exe</b>, no diretório <b>bin</b>.

<br>

![Captura de tela 2025-01-23 122244](https://github.com/user-attachments/assets/d2942588-cd21-4ac7-88ff-78a1744ce0b3)

Para fazer o backup a qualquer momento, sem ser nos horários agendados, deve-se clicar com o botão esquerdo do mouse no ícone do programa ou selecionar a opção <b>Fazer o Backup</b> no menu do programa.

<br>

![Captura de tela 2025-01-23 122319](https://github.com/user-attachments/assets/a9cf4042-3d37-47e6-94ab-9a2069dc19e8)

![Captura de tela 2025-01-23 122345](https://github.com/user-attachments/assets/228cd1c0-a27d-47cd-b463-6f3f0d9585c9)

Caso esteja configurado para backup em drive remoto, será exibida esta tela. Para fazer o backup, deve-se clicar no botão <b>Fazer o Backup</b>.

<br>

![Captura de tela 2025-01-23 122423](https://github.com/user-attachments/assets/0b1d11dd-1fa7-4c00-bb29-1fb444c0688c)

![Captura de tela 2025-01-23 122456](https://github.com/user-attachments/assets/7e306768-35f6-4b43-9c89-df84e3d83e06)

Para configurar para fazer o backup em drive USB, deve-se configurar o destino do backup para <b>Drive Removível</b> na guia <b>Parâmetros Gerais</b>.

<br>

![Captura de tela 2025-01-23 122544](https://github.com/user-attachments/assets/674ec696-f51d-4363-84dd-715e17c3b9de)

![Captura de tela 2025-01-23 122634](https://github.com/user-attachments/assets/95e23017-0445-4c02-9205-b44d3da3d16f)

![Captura de tela 2025-01-23 122708](https://github.com/user-attachments/assets/7db744e0-76fd-495b-a3bd-3e77dd800cd7)

Ao configurar o backup para Drive Removível, agora o backup será feito somente em Drive USB.


