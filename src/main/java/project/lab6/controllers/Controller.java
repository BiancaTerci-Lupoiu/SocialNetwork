package project.lab6.controllers;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class Controller {
    public abstract String getViewPath();
    private Parent root;

    /**
     * Sets the root node of this controller
     * This method should not be called
     * @param root The Parent node
     */
    public void setRoot(Parent root) {
        this.root = root;
    }

    /**
     * Returns the root node
     */
    public Parent getRoot()
    {
        return root;
    }

    /**
     * @return The stage on which the controller is on. The stage is not available in the initialize method
     */
    public Stage getStage(){
        if(root==null)
            return null;
        return (Stage) root.getScene().getWindow();
    }
}
