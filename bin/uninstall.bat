@echo off  
  
rem �޸Ŀ���̨��ɫ  
color 1d  
  
rem * ʹ��JavaServiceж��TestTimeReminderService����Ľű�  
rem *  
rem * JavaService - Windows NT Service Daemon for Java applications  
rem * Copyright (C) 2006 Multiplan Consultants Ltd. LGPL Licensing applies  
rem * Information about the JavaService software is available at the ObjectWeb  
rem * web site. Refer to http://javaservice.objectweb.org for more details.  
  
rem ��ʼ�������ļ��л����Ķ��ı��ػ���������ʹ��endlocal�󻷾����ָ���ԭ�ȵ�����  
SETLOCAL  
  
rem ���û�������  
SET BASE_PATH=.
  
rem ����JavaService��·��  
set JSBINDIR=.
set JSEXE=%JSBINDIR%\JavaService-x64.exe  
  
rem ж�ط���ǰ��ֹͣ����  
net stop TestTimeReminderService  
  
rem ����ִ��������  
set runcmd="%JSEXE%" -uninstall TestTimeReminderService
  
%runcmd%  
  
@echo .  
  
ENDLOCAL  
@echo .  
if "%2" NEQ "-np" @pause  