@echo off
echo Starting Spring Boot backend server...
cd %~dp0backend
mvn spring-boot:run
