# Sprint backlog

## Sprint 0 - inizio 18/09/2024

### Sprint planning

> Lo Sprint iniziale sarà dedicato a un'analisi approfondita del problema, alla definizione dei requisiti del progetto e
> alla progettazione
> di una prima versione del design dell'applicazione. Parallelamente, procederemo con la creazione e configurazione
> della repository
> e dell'ambiente di sviluppo.
> L'obiettivo finale dello sprint è sviluppare una prima implementazione funzionante dell'architettura ECS.
> Considerata la natura delle attività previste, lavoreremo tutti e tre in stretta collaborazione durante questo sprint.
> Per questo sprint sono simate 2 settimane di lavoro
>

| **Product backlog item**                         | **Sprint task**                              | **Assignee** | **Status** |
|--------------------------------------------------|----------------------------------------------|--------------|------------| 
| **Configurazione del progetto**                  | Creare la Repository                         | Tutti        | Finito     |
|                                                  | Configurare l'ambiente di sviluppo           | Tutti        | Finito     |
|                                                  | Organizzare le prime dependencies            | Tutti        | Finito     |
|                                                  | Test automatici                              | Tutti        | Finito     |
| **Sviluppare l'architettura base del framework** | Ricerca ed analisi delle possibili soluzioni | Tutti        | Finito     |
|                                                  | Realizzazione implementazione di base        | Tutti        | Finito     |
| **Requisiti e specifica**                        | Requisiti di business                        | Tutti        | Finito     |
|                                                  | Requisiti modello di dominio                 | Tutti        | Finito     |
|                                                  | Requisiti funzionali                         | Tutti        | Finito     |
|                                                  | Requisiti di implementazione                 | Tutti        | Finito     |

### Review

> In questo primo sprint è stata fatta una ricerca approfondita sullo stato dell'arte relativo a questo genere
> di applicazione. La ricerca è stata necessaria per avere un'idea del funzionamento delle soluzioni già esistenti,
> dal punto di vista dei strumenti e delle architetture utilizzate.
> Lo studio effettuato ha indirizzato la scelta dell'architettura su un sistema basato su architettura ECS,
> i membri del gruppo hanno dunque approfondito la conoscenza di tale pattern per essere in grado di implementarlo.
> Allo stesso modo è stato valutato che per la view potrebbe essere conveniente utilizzare Laminar, tuttavia verrà
> studiato nel
> dettaglio e implementato solo in seguito.
> Sono stati configurati Scalafmt, per garantire uno stile uniforme nello sviluppo del progetto, e Scalatest.
> Il team ha creato un design architetturale di massima, con l'ausilio di diagrammi UML (in modo che si avesse una
> struttura solida) e implementato una versione base del core del progetto dove vengono definiti i componenti principali
> dell'ECS,
> ossia Entity, Components e Systems, ed un World con l'essenziale per gestire il tutto.
> L'implementazione si è svolta seguendo la metodologia TDD con una copertura del 100% sul codice sviluppato.
> Lo sprint si è concluso con la creazione di una simulazione di base che mostra il funzionamento di questa versione
> del core del framework..
> Siamo rimasti nei tempi definiti ad inizio sprint. Tuttavia, abbiamo riscontrato delle problematiche relative alla
> configurazione di Laminar.

#### Simulazione implementata

> La simulazione implementata consiste in due entità che si muovono a velocità costante, in caso di collisione le due
> entità si fermano.

``` 
Tick 1
Entity 1: Position(1.0, 1.0), Speed(1.0, 1.0)
Entity 2: Position(9.0, 9.0), Speed(-1.0, -1.0)
-------------------
Tick 2
Entity 1: Position(2.0, 2.0), Speed(1.0, 1.0)
Entity 2: Position(8.0, 8.0), Speed(-1.0, -1.0)
-------------------
Tick 3
Entity 1: Position(3.0, 3.0), Speed(1.0, 1.0)
Entity 2: Position(7.0, 7.0), Speed(-1.0, -1.0)
-------------------
Tick 4
Entity 1: Position(4.0, 4.0), Speed(1.0, 1.0)
Entity 2: Position(6.0, 6.0), Speed(-1.0, -1.0)
-------------------
Tick 5
Entity 1: Position(5.0, 5.0), Speed(1.0, 1.0)
Entity 2: Position(5.0, 5.0), Speed(-1.0, -1.0)
-------------------
Tick 6
Collision detected between Entity 1 and Entity 2
Entity 1: Position(5.0, 5.0), Speed(0.0, 0.0)
Entity 2: Position(5.0, 5.0), Speed(0.0, 0.0)
-------------------
Tick 7
Collision detected between Entity 1 and Entity 2
Entity 1: Position(5.0, 5.0), Speed(0.0, 0.0)
Entity 2: Position(5.0, 5.0), Speed(0.0, 0.0)
-------------------
Tick 8
Collision detected between Entity 1 and Entity 2
Entity 1: Position(5.0, 5.0), Speed(0.0, 0.0)
Entity 2: Position(5.0, 5.0), Speed(0.0, 0.0)
-------------------
Tick 9
Collision detected between Entity 1 and Entity 2
Entity 1: Position(5.0, 5.0), Speed(0.0, 0.0)
Entity 2: Position(5.0, 5.0), Speed(0.0, 0.0)
-------------------
Tick 10
Collision detected between Entity 1 and Entity 2
Entity 1: Position(5.0, 5.0), Speed(0.0, 0.0)
Entity 2: Position(5.0, 5.0), Speed(0.0, 0.0)
-------------------
```

## Sprint 1 (2/10/2024)

### Sprint planning

> Nel primo sprint ufficiale ci concentreremo sull'iniziare a pensare come migliorare le prestazioni del core, come
> implementare un
> DSL di base per il core sviluppato nello sprint precedente e le possibili soluzioni per implementare la view grafica
> tramite Laminar.
> Per questo sprint ci prefiggiamo di iniziare gli item del backlog, completando almeno uno o più sprint task in modo
> tale da avere una visione più concreta
> su quello che sarà il framework finale. La durata prevista di questo sprint è quindi di una settimana.

| Product backlog item                                                | Sprint task                             | Assignee | Status      |
|---------------------------------------------------------------------|-----------------------------------------|----------|-------------|
| Studiare un modo per implementare la GUI                            | Studiare Laminar                        | Giannini | Iniziato    |
|                                                                     | Fare test implementativi                | Giannini | Finito      |
|                                                                     | Realizzare implementazione di base      | Giannini | Iniziato    |
| Come utente voglio poter definire la mia simulazione con semplicità | Definire DSL per il core base           | Vasiliu  | Iniziato    |
|                                                                     | Definire DSL per la view                | Vasiliu  | Da iniziare |
|                                                                     | Definire DSL per core avanzato          | Vasiliu  | Da iniziare |
| Migliorare il nucleo del framework                                  | Introdurre test di performance          | Bennici  | Iniziato    |
|                                                                     | Ricerca ed analisi dei possibili design | Bennici  | Iniziato    |
|                                                                     | Implementazione delle migliorie         | Bennici  | Da iniziare |

### Review

> In questo secondo sprint abbiamo iniziato a lavorare, in modo disgiunto, all'implementazione vera e propria del progetto,
> portando avanti in parallelo il core, il DSL e la view.  
> [core] -> Si è iniziato a lavorare sul miglioramento del core del framework, con particolare attenzione alle performance.
> Sono stati introdotti benchmark di base ed è stato individuato un possibile design architetturale.
> L'effettiva implementazione dello stesso, ancora incompleta, verrà riproposta nello sprint successivo.  
> [DSL]  -> È stato fatto da prima uno studio di quale poteva essera la sintassi del DSL prendendo in considerazione
> implementazioni esistendi di DSL. Dopo aver valutato sintassi come ad esempio "obj method param" o "method {param}",
> la scelta è ricaduta su una sintassi del tipo "method(param1, param2).method2(param1, param2)" in quanto valutata più
> coerente e con un'implementazione più semlice. Lo studio e la valutazione delle varie implementazioni hanno richiesto
> più tempo del previso, rimane quindi incompleta la task "Definire DSL per il core base". Nel prossimo sprint verrà scritta
> la logica per implementare il DSL anche per utilizzare i system e verrà riscritta la simulazione utilizzando il DSL implementato.   
> [view] -> È stato fatto uno studio su come implementare Laminar al progetto, inserendo le dependencies in modo tale
> che non interferiscano con la parte del core in quanto nello sprint precedente avevamo notato che le 
> dipendenze relative a JS interferivano con scalatest impendendo di eseguire i test. 
> Una volta fatto ciò è stato rifinito il file HTML necessario per l'esecuzione della view.
> È stata introdotta un'implementazione di base per verificare l'effettivo funzionamento del modulo view e, in seguito, è
> stata strutturata un'architettura essenziale che permetta di renderizzare la logica delle simulazioni definita nel modulo "examples".
> L'implementazione di base è ancora incompleta e sarà il punto di partenza dello Sprint 2.

#### Simulazione implementata

> La simulazione implementata consiste nella visualizzazione del world con due entità statiche posizionate in modo randomico  
> Lo scopo era verificare la corretta implementazione di Laminar all'interno del progetto.

![view_prototype](resources/view_prototype.png)


## Sprint 2 (9/10/2024)

### Sprint planning

> Nel secondo sprint continuiamo le attività rimanenti dal primo sprint.
> Si inizierà a ragionare sull'integrazione della view con la logica del core e del DSL.
> Verranno fatti dei refactor del codice per eliminare riddondanze per quanto rigarda component e system comuni in modo
> tale da semplificare la scrittura dei test e per fornire all'utente finale un set di strumenti per definire simulazioni
> base. Tali strumenti potrebbero essere utilizzati successivamente per la scrittura delle simulazioni di esempio.
> La durata prevista di questo sprint è di una settimana.

| Product backlog item                                                | Sprint task                             | Assignee | Status       |
|---------------------------------------------------------------------|-----------------------------------------|----------|--------------|
| Studiare un modo per implementare la GUI                            | Studiare Laminar                        | Giannini | Iniziato     |
|                                                                     | Realizzare implementazione di base      | Giannini | Iniziato     |
|                                                                     | Progettare struttura GUI per il DSL     | Giannini | Non Iniziato |
| Come utente voglio poter definire la mia simulazione con semplicità | Definire DSL per il core base           | Vasiliu  | Iniziato     |
|                                                                     | Definire DSL per la view                | Vasiliu  | Da iniziare  |
|                                                                     | Definire DSL per core avanzato          | Vasiliu  | Da iniziare  |
| Migliorare il nucleo del framework                                  | Ricerca ed analisi dei possibili design | Bennici  | Iniziato     |
|                                                                     | Implementazione delle migliorie         | Bennici  | Iniziato     |
