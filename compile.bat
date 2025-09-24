@echo off
echo Compilando projeto...

mkdir classes 2>nul

javac -cp "lib\mysql-connector-j-8.2.0.jar" -d classes src\main\java\com\oracle\gestao\*.java src\main\java\com\oracle\gestao\model\*.java src\main\java\com\oracle\gestao\dao\*.java src\main\java\com\oracle\gestao\view\*.java src\main\java\com\oracle\gestao\controller\*.java src\main\java\com\oracle\gestao\util\*.java

if %errorlevel% equ 0 (
    echo Compilacao concluida com sucesso!
) else (
    echo Erro na compilacao!
)