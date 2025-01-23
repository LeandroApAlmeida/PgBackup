<h3>Backup do Banco de Dados:</h3>

<br>

O ícone do programa é criado na barra de tarefas do Windows.

![Captura de tela 2025-01-23 121501](https://github.com/user-attachments/assets/c6fcb07b-2439-430b-995a-6c6d8e28b977)

<br>

Clicando com o botão direito do mouse, tem-se acesso ao menu do programa. Para configurar, clique em <b>Configurar...</b>

![Captura de tela 2025-01-23 122007](https://github.com/user-attachments/assets/1769051b-d820-40c0-b67f-7f36ae1076c6)

<br>

Na seção <b>Postgre Server</b>, deve-se informar os dados sobre a conexão com o banco de dados. Na guia <b>Parâmetros Gerais</b>, configura-se o modo de backup do banco de dados. Em <b>Destino do Backup</b>, deve se selecionar a opção <b>Drive de rede</b> ou <b>Drive removível</b>. No caso de <b>Drive de rede</b>, é necessário selecionar o drive de destino do backup na lista.

![Captura de tela 2025-01-23 122041](https://github.com/user-attachments/assets/5595d2ef-b714-4e14-abe9-587d4c152b62)

<br>

Na guia <b>Backup Automático</b>, deve se marcar a opção <b>habilitar o backup automático do Banco de Dados (recomendável)</b> para realizar o backup do banco de dados nos dias e horários definidos. Na seção <b>Dias da Semana</b>, pode se selecionar em quais dias da semana será realizado o Backup do banco de dados. Na seção <b>Horários</b>, são definidos os horários que serão feito o backup.

![Captura de tela 2025-01-23 122145](https://github.com/user-attachments/assets/766ad957-5d42-402d-a554-935eafb3c453)

<br>

Na guia <b>Ferramentas</b>, no campo <b>Programa para o Backup (pg_dump)</b>, deve-se localizar o local de instalação do PostgreSQL e selecionar o programa <b>pg_dump.exe</b>, no diretório <b>bin</b>. No campo <b>Programa para o Restore (pg_restore)</b>, deve-se localizar o local de instalação do PostgreSQL e selecionar o programa <b>pg_restore.exe</b>, no diretório <b>bin</b>.

![Captura de tela 2025-01-23 122208](https://github.com/user-attachments/assets/b9377eb3-f7f9-431b-9fa8-01b7591ac0f9)

<br>

Para fazer o backup a qualquer momento, sem ser nos horários agendados, deve-se clicar com o botão esquerdo do mouse no ícone do programa ou selecionar a opção <b>Fazer o Backup</b> no menu do programa.

![Captura de tela 2025-01-23 122244](https://github.com/user-attachments/assets/d2942588-cd21-4ac7-88ff-78a1744ce0b3)

<br>

Caso esteja configurado para backup em drive remoto, será exibida esta tela. Para fazer o backup, deve-se clicar no botão <b>Fazer o Backup</b>.

![Captura de tela 2025-01-23 122319](https://github.com/user-attachments/assets/a9cf4042-3d37-47e6-94ab-9a2069dc19e8)

![Captura de tela 2025-01-23 122345](https://github.com/user-attachments/assets/228cd1c0-a27d-47cd-b463-6f3f0d9585c9)

<br>

Para configurar para fazer o backup em drive USB, deve-se configurar o destino do backup para <b>Drive Removível</b> na guia <b>Parâmetros Gerais</b>.

![Captura de tela 2025-01-23 122423](https://github.com/user-attachments/assets/0b1d11dd-1fa7-4c00-bb29-1fb444c0688c)

![Captura de tela 2025-01-23 122456](https://github.com/user-attachments/assets/7e306768-35f6-4b43-9c89-df84e3d83e06)

<br>

Ao configurar o backup para Drive Removível, agora o backup será feito somente em Drive USB.

![Captura de tela 2025-01-23 122544](https://github.com/user-attachments/assets/674ec696-f51d-4363-84dd-715e17c3b9de)

![Captura de tela 2025-01-23 122634](https://github.com/user-attachments/assets/95e23017-0445-4c02-9205-b44d3da3d16f)

![Captura de tela 2025-01-23 122708](https://github.com/user-attachments/assets/7db744e0-76fd-495b-a3bd-3e77dd800cd7)

<br>

<h3>Restauração do Banco de Dados:</h3>

<br>

Para fazer o restore, deve-se selecionar a opção <b>Restaurar o Backup</b> no menu do programa.

![Captura de tela 2025-01-23 141735](https://github.com/user-attachments/assets/72b44c2f-0c02-4167-9a7c-949da7f9870d)

<br>

No campo <b>Arquivo de backup</b>, deve-se selecionar o arquivo de backup do Banco de dados no drive de rede ou drive removível.

![Captura de tela 2025-01-23 141809](https://github.com/user-attachments/assets/a61179f0-0b57-4630-b395-527709bcc0ac)

![Captura de tela 2025-01-23 141838](https://github.com/user-attachments/assets/9422641a-df65-48dc-bce2-5e7260a16c51)

![Captura de tela 2025-01-23 141915](https://github.com/user-attachments/assets/9479d36b-c465-4073-a943-8d4148fdd8bb)

<br>

Ao clicar no botão <b>Restaurar o backup</b>, se não houver um banco de dados criado com o mesmo nome do do backup, dará o seguinte erro:

![Captura de tela 2025-01-23 142107](https://github.com/user-attachments/assets/5e82b2ff-f9f1-4597-84b2-3d98ad26e643)

<br>

Deverá ser criado o banco de dados para prosseguir. Após criado, deve-se clicar novamente no mesmo botão.

![Captura de tela 2025-01-23 142414](https://github.com/user-attachments/assets/a0f3b8f9-dc33-4ed1-bdbb-d44874b4234e)

Com isso, o banco de dados é restaurado.
