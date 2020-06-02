@echo off

Rem set GRAALVM_HOME=C:\Users\IEUser\Downloads\graalvm\graalvm-ce-java11-20.1.0
Rem set PATH=%PATH%;C:\Users\IEUser\bin

if "%GRAALVM_HOME%"=="" (
    echo Please set GRAALVM_HOME
    exit /b
)
set JAVA_HOME=%GRAALVM_HOME%\bin
set PATH=%GRAALVM_HOME%\bin;%PATH%
set /P MULLION_VERSION=< .meta\VERSION

echo Building mullion %MULLION_VERSION%

mkdir graal-configs

call lein do clean, uberjar
if %errorlevel% neq 0 exit /b %errorlevel%

call %GRAALVM_HOME%\bin\gu install native-image

Rem the --no-server option is not supported in GraalVM Windows.
call %GRAALVM_HOME%\bin\native-image.cmd ^
  "-jar" "target/uberjar/mullion-%MULLION_VERSION%-standalone.jar" ^
  "-H:Name=mullion" ^
  "-H:+ReportExceptionStackTraces" ^
  "-J-Dclojure.spec.skip-macros=true" ^
  "-J-Dclojure.compiler.direct-linking=true" ^
  "-H:ConfigurationFileDirectories=graal-configs/" ^
  "--initialize-at-build-time" ^
  "-H:Log=registerResource:" ^
  "-H:EnableURLProtocols=http" ^
  "--verbose" ^
  "--allow-incomplete-classpath" ^
  "--no-fallback" ^
  "-J-Xmx5g"

if %errorlevel% neq 0 exit /b %errorlevel%

call mullion.exe --test-load

echo Creating zip archive
jar -cMf mullion-%MULLION_VERSION%-windows-amd64.zip mullion.exe
