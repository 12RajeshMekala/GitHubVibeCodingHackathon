@echo off
setlocal enableextensions

set MAVEN_PROJECTBASEDIR=%~dp0
set MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%
@REM Find the baseDir for the Maven wrapper
cd %MAVEN_PROJECTBASEDIR%
cd ..
set MAVEN_BASEDIR=%CD%
cd %MAVEN_PROJECTBASEDIR%

@REM Determine the wrapper file version
set WRAPPER_VERSION=3.2.0

@REM Download the wrapper if it doesn't exist
if not exist %MAVEN_PROJECTBASEDIR%\mvnw (
    echo Downloading Maven wrapper...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/%WRAPPER_VERSION%/maven-wrapper-%WRAPPER_VERSION%.jar' -OutFile '%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar'"
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/%WRAPPER_VERSION%/maven-wrapper-%WRAPPER_VERSION%.properties' -OutFile '%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties'"
    echo @REM Maven Wrapper generated > %MAVEN_PROJECTBASEDIR%\mvnw
    echo @REM exec is the batch equivalent of direct command execution >> %MAVEN_PROJECTBASEDIR%\mvnw
    echo @echo off >> %MAVEN_PROJECTBASEDIR%\mvnw
    echo @REM Execute Maven >> %MAVEN_PROJECTBASEDIR%\mvnw
    echo "%JAVA_HOME%\bin\java" -classpath "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" ^
         "-Dmaven.multiModuleProjectDirectory=%MAVEN_BASEDIR%" ^
         "-Dmaven.home=%M2_HOME%" ^
         org.apache.maven.wrapper.MavenWrapperMain %%* >> %MAVEN_PROJECTBASEDIR%\mvnw
    echo @REM end batch script >> %MAVEN_PROJECTBASEDIR%\mvnw
)

echo Maven wrapper created. You can now run './mvnw spring-boot:run' to start the application.
