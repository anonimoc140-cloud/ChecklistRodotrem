@echo off
setlocal
cd /d "%~dp0"
chcp 65001 >nul

echo ==============================================
echo   CHECKLIST RODOTREM - GERADOR DE APK
 echo ==============================================
echo.

if not defined ANDROID_HOME if defined ANDROID_SDK_ROOT set "ANDROID_HOME=%ANDROID_SDK_ROOT%"
if not defined ANDROID_HOME if exist "%LOCALAPPDATA%\Android\Sdk" set "ANDROID_HOME=%LOCALAPPDATA%\Android\Sdk"
if not defined ANDROID_HOME if exist "%USERPROFILE%\AppData\Local\Android\Sdk" set "ANDROID_HOME=%USERPROFILE%\AppData\Local\Android\Sdk"

if not defined ANDROID_HOME (
  echo [ERRO] O Android SDK nao foi encontrado neste computador.
  echo.
  echo Para a opcao mais simples, use o metodo do GitHub explicado no arquivo LEIA-ME.txt.
  echo.
  pause
  exit /b 1
)

where gradle >nul 2>nul
if errorlevel 1 (
  echo [ERRO] O Gradle nao esta instalado.
  echo.
  echo Use o metodo do GitHub no arquivo LEIA-ME.txt ou instale o Gradle 8.9.
  echo.
  pause
  exit /b 1
)

echo Android SDK encontrado em:
echo %ANDROID_HOME%
echo.
echo Gerando APK...
echo.
gradle assembleDebug
if errorlevel 1 (
  echo.
  echo [ERRO] A compilacao falhou. Veja as mensagens acima.
  pause
  exit /b 1
)

echo.
echo ==============================================
echo APK criado com sucesso!
echo ==============================================
echo.
echo Arquivo:
echo %CD%\app\build\outputs\apk\debug\app-debug.apk
echo.
pause
