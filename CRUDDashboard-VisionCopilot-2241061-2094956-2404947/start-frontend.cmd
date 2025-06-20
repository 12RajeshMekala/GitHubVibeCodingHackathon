@echo off
echo Installing frontend dependencies...
cd %~dp0frontend
call npm install

echo Starting React frontend server...
call npm start
