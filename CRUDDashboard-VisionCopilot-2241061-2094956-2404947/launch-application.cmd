@echo off
echo ========================================
echo Project Apex: Intelligent Inventory Hub
echo ========================================
echo.
echo This script will launch both the backend and frontend servers.
echo.
echo 1. Starting Spring Boot backend server...
start cmd /k "%~dp0start-backend.cmd"
echo.
echo 2. Waiting for backend to initialize (10 seconds)...
timeout /t 10 /nobreak > nul
echo.
echo 3. Starting React frontend server...
start cmd /k "%~dp0start-frontend.cmd"
echo.
echo Both servers are starting up. Please wait a moment.
echo.
echo - Backend will be available at: http://localhost:8080
echo - Frontend will be available at: http://localhost:3000
echo.
echo Press any key to exit this window...
pause > nul
