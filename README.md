# Cobot Simulator
A Java desktop application that receives six numbers via a TCP connection and displays them as the angles of a six-joint robotic arm. 
The tester package includes a random number generator server (running on port 12345) for testing the Cobot Simulator.

There are two versions of the Cobot Simulator:

# Head Simulator
The Head Simulator is a Java desktop application where a head follows the mouse cursor. The head position is streamed to port 8888 and can be received by a client application. 
The tester package includes a client that receives the head position and displays it on the console.

There are two versions of the Cobot Simulator:

# Affect Simulator
The Affect Simulator is a Java desktop application that simulates the affective state of a user. 
The user can select the affective state from a group of sliders. 
The selected affective states are to be streamed to port <***> and can be received by a client application.

# Eye Simulator

The Eye Simulator is a Java desktop application that simulates the eye gaze of a user.
The user can select the eye gaze moving the mouse cursor.
The selected eye gaze is to be streamed to port <***> and can be received by a client application.

# Affect and Eye Tracker Dashboard

The Affect and Eye Tracker Dashboard is a Java desktop application that receives the affective states and eye gaze of a user from the Affect Simulator and Eye Simulator, respectively.
The dashboard displays the affective states and eye gaze of the user in real-time as circles in different colors.


<br>

# To-Do List

- [ ] :one: **Implement a Common Publisher**  
   - Ensure the publisher class works across all components.

- [ ] :two: **Implement a Common Subscriber**  
   - Ensure the subscriber class works across all components.

- [ ] :three: **Create a Blackboard with Delegates**  
   - Implement a set of blackboards following the singleton pattern for shared communication.

- [ ] :four: **Implement Standard Robot with Animation**  
   - Design a standard robot class and provide an example implementation for its animation functionality.

- [ ] :five: **Panels Observing the Blackboard**  
   - Ensure all panels observe the blackboard, displaying a default drawing and updating whenever an event is fired.

- [ ] :six: **Add Menus to Applications**  
   - Include a menu in all applications with options to start/stop sending or receiving data, i.e., all Main are "similar"

- [ ] :seven: **Code Review**  
   - Ensure all classes adhere to the Single Responsibility Principle (SRP). Make recomendations
   - Review and justify the use of any global variables. Make recomendations
   - Verify the accuracy of the class diagrams and collect them for documentation. Make recomendations

- [ ] :eight: **Use a Logger**
   - Keep or include the SFL4J library.
   - Mandatory for Exceptions
   - Consider debuging or tracing messages as needed. It is not **perfume** use when needed

- [ ] :nine: **Implement MQTT**
   - Create a publisher class that stream data to an MQTT Broker (Use PAHO library and Mosquitto Broker)
   - Create a subscriber class that stream data to an MQTT Broker (Use PAHO library and Mosquitto Broker)

- [ ] :ten: **Pull Request**
   - Pull request to update your code. Only your Module or the Library!  
