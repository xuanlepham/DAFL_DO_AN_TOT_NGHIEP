@echo off
chcp 65001 > nul
setlocal enabledelayedexpansion
echo ========================================================
echo   DAFL E2E VISUAL TEST RUNNER (UI CHROME MODE)
echo   Thu muc project: %~dp0
echo ========================================================
cd /d "%~dp0"

rem ===== Kiem tra Java (JDK) + chuan hoa JAVA_HOME (Maven Wrapper can JDK) =====
if exist "%JAVA_HOME%\bin\java.exe" if exist "%JAVA_HOME%\bin\javac.exe" goto JAVA_OK
for %%V in ("Eclipse Adoptium" "Java" "Microsoft" "BellSoft" "Amazon Corretto" "Zulu") do (
    for /d %%D in ("C:\Program Files\%%~V\jdk*") do set "JAVA_HOME=%%D"
)
if exist "%JAVA_HOME%\bin\java.exe" if exist "%JAVA_HOME%\bin\javac.exe" goto JAVA_OK
java -version >nul 2>nul
if errorlevel 1 goto INSTALL_JAVA
for /f "tokens=1,* delims==" %%A in ('java -XshowSettings:properties -version 2^>^&1 ^| findstr /C:"java.home"') do set "JAVA_HOME=%%B"
if "%JAVA_HOME:~0,1%"==" " set "JAVA_HOME=%JAVA_HOME:~1%"
if exist "%JAVA_HOME%\bin\java.exe" if exist "%JAVA_HOME%\bin\javac.exe" goto JAVA_OK
echo [LOI] Khong xac dinh duoc JAVA_HOME hop le (can JDK, khong phai JRE).
pause
exit /b 1
:INSTALL_JAVA
echo [JAVA] Chua tim thay Java - dang tu cai Eclipse Temurin JDK 17 qua winget...
where winget >nul 2>nul
if errorlevel 1 (
    echo [LOI] May khong co winget. Hay cai JDK 17 tu https://adoptium.net roi chay lai.
    pause
    exit /b 1
)
winget install --id EclipseAdoptium.Temurin.17.JDK -e --silent --accept-package-agreements --accept-source-agreements
for /d %%D in ("C:\Program Files\Eclipse Adoptium\jdk-17*") do set "JAVA_HOME=%%D"
:JAVA_OK
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo [JAVA] JAVA_HOME = %JAVA_HOME%

rem ===== Phan giai doi so: --t TenTest / -t TenTest / TenTest =====
set "TARGET_TEST="
:parse_args
if "%~1"=="" goto run_tests
if /i "%~1"=="--t" ( set "TARGET_TEST=%~2" & shift & shift & goto parse_args )
if /i "%~1"=="-t" ( set "TARGET_TEST=%~2" & shift & shift & goto parse_args )
if not "%~1"=="" ( set "TARGET_TEST=%~1" & shift & goto parse_args )

:run_tests
if not "%TARGET_TEST%"=="" (
    echo [INFO] Che do chay don le: %TARGET_TEST%
    echo ========================================================
    call "%~dp0mvnw.cmd" test "-Dtest=%TARGET_TEST%" "-DsuiteXmlFile=" "-Dheadless=false" "-Dapp.url=http://localhost:3000"
) else (
    echo [INFO] Che do chay toan bo Test Suite (testng.xml)
    echo ========================================================
    call "%~dp0mvnw.cmd" test "-Dheadless=false" "-Dapp.url=http://localhost:3000"
)
echo.
echo [DONE] Hoan tat kiem thu E2E!
pause
