# bwi-challenge

## Installation und Ausführung
Nachdem das Repository geclont wurde, kann das Progamm wie folgt kompiliert werden:<br/>
`mkdir out`<br/>
`javac -d out/ src/*.java`

Wenn erfolgreich kompiliert wurde, kann das Programm wie folgt ausgeführt werden:<br/>
`java -Xmx3072m -cp out Main`

**Alternativ** kann auch die .jar Datei [hier](https://github.com/ariogato/bwi-challenge/releases/tag/v1.0) heruntergeladen<br/>
und entsprechend den Anweisungen ausgeführt werden. 

## Wahl des Algorithmus
Das Problem ist eine leichte Abwandlung eines ***"Multiple Knapsack Problems"***.<br/>
Die exakte Lösung hierzu zu berechnen ist mit einer solchen Anzahl an items nicht (in vernünfitger Zeit) möglich.<br/>
Deswegen wird das Problem hier unterteilt in einfache Knapsack Probleme, die mit einer Abwandlung des allgemeinen Lösungsansatzes gelöst werden.

## Optimale Verteilung
<img src="res/screenshot.png" width="500" >

