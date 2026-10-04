@echo off
echo Compilation et Lancement du Jeu de Dames...
javac *.java
if %errorlevel% equ 0 java Main
pause