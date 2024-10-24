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




