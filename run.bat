@echo off
echo Compilation et Lancement du Jeu de Dames...
javac -encoding UTF-8 *.java
if %errorlevel% equ 0 java Main
pause