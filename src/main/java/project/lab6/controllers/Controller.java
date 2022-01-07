package project.lab6.controllers;

import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class Controller {
    private Parent root;

    public abstract String getViewPath();

    /**
     * Returns the root node
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * Sets the root node of this controller
     * This method should not be called
     *
     * @param root The Parent node
     */
    public void setRoot(Parent root) {
        this.root = root;
    }

    /**
     * @return The stage on which the controller is on. The stage is not available in the initialize method
     */
    public Stage getStage() {
        if (root == null)
            return null;
        return (Stage) root.getScene().getWindow();
    }
}
