# Vektorový editor v javě

Editor umožní vykreslení základních tvarů (elipsa, kruh, obdélník, čtverec, čára, lomená čára, trojúhelník, případně další). Každý tvar bude možné vytvořit, editovat a smazat. Úpravou tvaru je myšlena změna typu a barvy čáry, změna výplně, změna velikosti a umístění. 

Výsledný obrázek bude možné exportovat do bitmapového souboru. 
# Ukázka

https://youtu.be/wzyEdHbw3-U
# Setup

1. git clone https://gitlab.spseplzen.cz/studentske-projekty/projekty-2024-2025/mtp5/7.git
2. Otevřete složku v programu Intellij Idea
3. Projekt vyžaduje Java SDK 16
4. Je nutno přidat javafx-sdk-11.0.2 jako externí knihovnu a to v Run->Edit configurations->Modify options->Add VM options
5. Zde vložit --module-path #Vaše Cesta#\javafx-sdk-11.0.2\lib --add-modules javafx.controls,javafx.fxml
5. Pro spuštění aktuální verze je nutné jít do src/Launcher.java a dát 'Run Launcher.main()'

