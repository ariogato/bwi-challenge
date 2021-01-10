# bwi-challenge

## Installation und Ausführung
Nachdem das Repository geclont wurde, kann das Progamm wie folgt kompiliert werden:<br/>
`mkdir out`<br/>
`javac -d out/ src/*.java`

Wenn erfolgreich kompiliert wurde, kann das Programm wie folgt ausgeführt werden:<br/>
`java -Xmx3072m -cp out Main`

**Alternativ** kann auch die .jar Datei [hier](https://github.com/ariogato/bwi-challenge/releases/tag/v1.0) heruntergeladen 
und entsprechend den Anweisungen auf der Seite ausgeführt werden. 

>Anmerkung: Zur Ausführung werden mind. 3GB freien Arbeitsspeichers benötigt. Das flag -Xmx3072m reserviert eben diese 3GB für die Anwendung.
Es hat auch schon mit weniger geklappt (z.B. -Xmx2048m), jedoch würde ich - sofern möglich - bei den 3GB bleiben. Ansonsten droht evtl. ein `OutOfMemoryError`.

## Wahl des Algorithmus
Das Problem ist eine leichte Abwandlung eines ***"Multiple Knapsack Problems"***.<br/>
Die exakte Lösung hierzu zu berechnen ist mit einer solchen Anzahl an items nicht (in vernünfitger Zeit) möglich.<br/>
Deswegen wird das Problem hier in *"einfache Knapsack Probleme"* unterteilt, die mit einer Abwandlung des allgemeinen Lösungsansatzes gelöst werden.<br/>
Das Ergebnis ist eine Approximation des exakten Ergebnisses. Der hier entstandene Fehler dürfte<br/>
jedoch aufgrund der geringen Anzahl der *"Knapsacks"* (zwei) nicht allzu stark ins Gewicht fallen.

## Optimale Verteilung
<img src="res/screenshot.png" width="500" ><br/>
>Anmerkung: Das Gesamtgewicht ist in Gramm gegeben
