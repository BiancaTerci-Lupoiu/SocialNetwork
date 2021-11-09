package project;

import project.factory.Factory;
import project.repository.RepositoryTypes;

public class Main {

    public static void main(String[] args) {
        Factory factory = new Factory(RepositoryTypes.Database);
        try(var ui = factory.getUI())
        {
            ui.start();
        }
    }
}
