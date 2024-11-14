## Cobot Simulator One

Authors: Neeraja Beesetti, Jack Ortega, Saanvi Dua

### Build the Project
In root (CSC5090):
- Run to make sure library (javiergs:library:jar:1.0-SNAPSHOT) is generated
```bash
mvn clean install
```

In cobotSimulatorOne:
```bash
mvn clean package
```

### Running the Application
In cobotSimulatorOne:
- run tester.Main to start server (aka publisher, sends angles)
- run cobot.Main to start client (aka subscriber, receives angles to move Cobot)
