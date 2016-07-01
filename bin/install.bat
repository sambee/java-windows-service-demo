@echo off

rem �޸Ŀ���̨��ɫ
color 1d

rem * ʹ��JavaService��TimeRemider��װΪWindows����Ľű�
rem *
rem * JavaService - Windows NT Service Daemon for Java applications
rem * Copyright (C) 2006 Multiplan Consultants Ltd. LGPL Licensing applies
rem * Information about the JavaService software is available at the ObjectWeb
rem * web site. Refer to http://javaservice.objectweb.org for more details.

rem ��ʼ�������ļ��л����Ķ��ı��ػ���������ʹ��endlocal�󻷾����ָ���ԭ�ȵ�����
SETLOCAL

rem ���û�������,ָ��ǰ·��
SET BASE_PATH=%CD%

rem ����Java path: jre_home

if "%JRE_HOME%" == "" SET JRE_HOME=D:\jdk1.7.0_79x64\jdk\jre

rem �ж�JRE_HOME�Ƿ���ȷ
if "%JRE_HOME%" == "" goto no_java
if not exist "%JRE_HOME%\bin\java.exe" goto no_java

rem ����jvm�ڴ�������
set JVM_MEMORY=-Xms128m -Xmx256m 

rem ����jvmdllʹ����һ��ģʽ
SET jvmdll=%JRE_HOME%\bin\server\jvm.dll
if not exist "%jvmdll%" SET jvmdll=%JRE_HOME%\bin\hotspot\jvm.dll
if not exist "%jvmdll%" goto no_java

rem ����JavaService·��
set JSBINDIR=.
set JSEXE=%JSBINDIR%\JavaService-x64.exe

rem �ж�jar�Ƿ���ȷ
SET acctjar=%BASE_PATH%\TestTimeReminder.jar
if not exist "%acctjar%" goto no_peer

@echo . Using following version of JavaService executable:
@echo .
"%JSEXE%" -version
@echo .

rem parameters and files seem ok, go ahead with the service installation
@echo .

rem �����������������������̨��������ģʽ���Զ�
SET svcmode=
if "%1" == "-manual" SET svcmode=-manual
if "%1" == "-auto" SET svcmode=-auto


rem ����JAVA_OPTS
set JAVA_OPTS=%JAVA_OPTS% -Djava.class.path="%acctjar%"
set JAVA_OPTS=%JAVA_OPTS% %JVM_MEMORY%

rem ����startstop
SET START_STOP=-start com.hhf.schedule.test.TestTimeReminder -method startService


rem ����Log�ļ�·��
set OUT_ERR=-out "%BASE_PATH%\service_out.log" -err "%BASE_PATH%\service_err.log"

rem ����desp
set DESP=-description "TestTimeReminder Service" 

rem ����ִ��������
set runcmd="%JSEXE%" -install TestTimeReminderService
set runcmd=%runcmd% "%jvmdll%"
set runcmd=%runcmd% %JAVA_OPTS%
set runcmd=%runcmd% %START_STOP%
set runcmd=%runcmd% %OUT_ERR%
set runcmd=%runcmd% -current
set runcmd=%runcmd% "%BASE_PATH%"
set runcmd=%runcmd% %svcmode%
set runcmd=%runcmd% -overwrite
set runcmd=%runcmd% -startup 6
set runcmd=%runcmd% %DESP%
set rumcmd=%rumcmd%  -stop com.hhf.schedule.test.TestTimeReminder -method stopService
echo %runcmd%

rem ִ�а�װ����
%runcmd%

rem ��������
net start TestTimeReminderService

if ERRORLEVEL 1 goto js_error

goto end

:no_java
@echo . û��Java���л�������װ�ű���������
goto error_exit

:no_peer
@echo . �����ļ� "%acctjar%" TestTimeReminder.jar�����ڣ���װ�ű���������
goto error_exit


:no_jsexe
@echo . ��ִ���ļ�JavaService-x64.exe �����ڣ���װ�ű���������
goto error_exit


:js_error
@echo . TimeRemiderService�ڰ�װΪ����Ĺ����з����˴������������־�ļ�
goto error_exit

:error_exit

@echo .
@echo . ��װʧ�ܣ����ܽ� TestTimeReminderService��װΪWindows����
@echo .
@echo . �����ʽ:
@echo .
@echo .%~n0 [-auto / -manual] [-np]
@echo .
@echo . ����:
@echo .-auto (Ĭ��) or -manual ����˵���˷��������ģʽ���Զ������ֶ�
@echo .-np ����������ִ����Ϻ���ͣ
@echo .
@echo . ����:
@echo .%~n0 -auto -np

:end
ENDLOCAL
@echo .
if "%2" NEQ "-np" @pause 