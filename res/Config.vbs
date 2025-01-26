
' Este programa configura para que o serviço de backup inicialize automaticamente com o Windows.
' Utilize desta forma caso não use o serviço do Windows que inicializa o agendador.


Set objFSO = CreateObject("Scripting.FileSystemObject")
Set objFile = objFSO.GetFile(WScript.ScriptFullName)
strFolder = objFile.ParentFolder


Dim batFilePath
batFilePath = strFolder & "\PostgreBackup.bat"


Dim regKeyName
regKeyName = "PgBackup"


Dim regPath
regPath = "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Run"


Dim regCommand
regCommand = "reg add """ & regPath & """ /v """ & regKeyName & """ /t REG_SZ /d """ & batFilePath & """ /f"


Dim shell
Set shell = CreateObject("WScript.Shell")

shell.Run regCommand, 0, True


shell.Popup "Configuracao realizada com sucesso.", 10, "Concluido!", 64