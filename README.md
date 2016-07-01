# Java 下的windows服务集成方案Demo

## 编写代码 (Coding)
 编写Java服务器，其中startService 为服务的启动方法，　stopService为服务的停止方法，在方法启动时，程序调用startService,

 在方法里面，创建一个定时器，指定调用时间，当定时器到害时，执行此方法。

    public class TestTimeReminder {

    private static Scheduler scheduler = new Scheduler();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     *  stop service with argument
     *
     * @param args
     */
    public static void stopService(String[] args) {
        log("",sdf.format(new Date()) + "Service stopped");
    }

    /**
     * start service with arguments
     *
     * @param args
     */
    public static void startService(String[] args) {
        Log.log("",sdf.format(new Date()) +"service start");
        ScheduleIterator iterator = new DailyIterator();

        scheduler.schedule(new SchedulerTask() {
            @Override
            public void run() {
                log("",sdf.format(new Date()) +"service running");
            }
        }, iterator);
    }

    public static void main(String[] args) {
        new TestTimeReminder().startService(new String[]{""});
    }

    }

#安装Ｗindows服务（install）
然后使用 JavaService.exe 安装成Window 服务
在jar文件所在的目录下，建立如下批处理文件,执行后即可完成服务的注册和auto start 


    @echo off
    
    rem 修改控制台颜色
    color 1d
    
    rem * 使用JavaService将TimeRemider安装为Windows服务的脚本
    rem *
    rem * JavaService - Windows NT Service Daemon for Java applications
    rem * Copyright (C) 2006 Multiplan Consultants Ltd. LGPL Licensing applies
    rem * Information about the JavaService software is available at the ObjectWeb
    rem * web site. Refer to http://javaservice.objectweb.org for more details.
    
    rem 开始批处理文件中环境改动的本地化操作，在使用endlocal后环境将恢复到原先的内容
    SETLOCAL
    
    rem 设置环境变量,指向当前路径
    SET BASE_PATH=%CD%
    
    rem 设置Java path: jre_home
    
    if "%JRE_HOME%" == "" SET JRE_HOME=D:\jdk1.7.0_79x64\jdk\jre
    
    rem 判断JRE_HOME是否正确
    if "%JRE_HOME%" == "" goto no_java
    if not exist "%JRE_HOME%\bin\java.exe" goto no_java
    
    rem 设置jvm内存分配情况
    set JVM_MEMORY=-Xms128m -Xmx256m 
    
    rem 设置jvmdll使用哪一种模式
    SET jvmdll=%JRE_HOME%\bin\server\jvm.dll
    if not exist "%jvmdll%" SET jvmdll=%JRE_HOME%\bin\hotspot\jvm.dll
    if not exist "%jvmdll%" goto no_java
    
    rem 设置JavaService路径
    set JSBINDIR=.
    set JSEXE=%JSBINDIR%\JavaService-x64.exe
    
    rem 判断jar是否正确
    SET acctjar=%BASE_PATH%\TestTimeReminder.jar
    if not exist "%acctjar%" goto no_peer
    
    @echo . Using following version of JavaService executable:
    @echo .
    "%JSEXE%" -version
    @echo .
    
    rem parameters and files seem ok, go ahead with the service installation
    @echo .
    
    rem 处理该批处理的输入参数，后台服务启动模式：自动
    SET svcmode=
    if "%1" == "-manual" SET svcmode=-manual
    if "%1" == "-auto" SET svcmode=-auto
    
    
    rem 设置JAVA_OPTS
    set JAVA_OPTS=%JAVA_OPTS% -Djava.class.path="%acctjar%"
    set JAVA_OPTS=%JAVA_OPTS% %JVM_MEMORY%
    
    rem 设置startstop
    SET START_STOP=-start com.hhf.schedule.test.TestTimeReminder -method startService
    
    
    rem 设置Log文件路径
    set OUT_ERR=-out "%BASE_PATH%\service_out.log" -err "%BASE_PATH%\service_err.log"
    
    rem 设置desp
    set DESP=-description "TestTimeReminder Service" 
    
    rem 设置执行命令行
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
    
    rem 执行安装命令
    %runcmd%
    
    rem 启动服务
    net start TestTimeReminderService
    
    if ERRORLEVEL 1 goto js_error
    
    goto end
    
    :no_java
    @echo . 没有Java运行环境，安装脚本不能运行
    goto error_exit
    
    :no_peer
    @echo . 启动文件 "%acctjar%" TestTimeReminder.jar不存在，安装脚本不能运行
    goto error_exit
    
    
    :no_jsexe
    @echo . 可执行文件JavaService-x64.exe 不存在，安装脚本不能运行
    goto error_exit
    
    
    :js_error
    @echo . TimeRemiderService在安装为服务的过程中发生了错误，请检查相关日志文件
    goto error_exit
    
    :error_exit
    
    @echo .
    @echo . 安装失败，不能将 TestTimeReminderService安装为Windows服务
    @echo .
    @echo . 命令格式:
    @echo .
    @echo .%~n0 [-auto / -manual] [-np]
    @echo .
    @echo . 其中:
    @echo .-auto (默认) or -manual 参数说明了服务的启动模式：自动或者手动
    @echo .-np 批处理命令执行完毕后不暂停
    @echo .
    @echo . 比如:
    @echo .%~n0 -auto -np
    
    :end
    ENDLOCAL
    @echo .
    if "%2" NEQ "-np" @pause 

注意： 其中JRE_HOME 和 JSBINDIR（javaservice路径） 视安装路径而异。 详细可以install.bat

#卸载Ｗindows服务(Uninstall)
停止并卸载服务的批处理文件，其中service 名称必须与上面注册的service名称一致 

    @echo off  
      
    rem 修改控制台颜色  
    color 1d  
      
    rem * 使用JavaService卸载TestTimeReminderService服务的脚本  
    rem *  
    rem * JavaService - Windows NT Service Daemon for Java applications  
    rem * Copyright (C) 2006 Multiplan Consultants Ltd. LGPL Licensing applies  
    rem * Information about the JavaService software is available at the ObjectWeb  
    rem * web site. Refer to http://javaservice.objectweb.org for more details.  
      
    rem 开始批处理文件中环境改动的本地化操作，在使用endlocal后环境将恢复到原先的内容  
    SETLOCAL  
      
    rem 设置环境变量  
    SET BASE_PATH=.
      
    rem 设置JavaService的路径  
    set JSBINDIR=.
    set JSEXE=%JSBINDIR%\JavaService-x64.exe  
      
    rem 卸载服务前先停止服务  
    net stop TestTimeReminderService  
      
    rem 设置执行命令行  
    set runcmd="%JSEXE%" -uninstall TestTimeReminderService
      
    %runcmd%  
      
    @echo .  
      
    ENDLOCAL  
    @echo .  
    if "%2" NEQ "-np" @pause  