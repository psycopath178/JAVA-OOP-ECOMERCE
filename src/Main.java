package src; // Package declaration — tells Java this class is in the 'src' folder

import src.ui.LoginFrame; // Import LoginFrame class from the UI package

public class Main { // Main class, program entry point
    public static void main(String[] args) { // Main method, JVM starts here
        new LoginFrame(); // Creates and shows the login window. Constructor runs automatically
    }
}
