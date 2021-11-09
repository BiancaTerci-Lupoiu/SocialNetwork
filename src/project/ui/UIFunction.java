package project.ui;

import java.io.IOException;

@FunctionalInterface
interface UIFunction
{
    void execute() throws IOException;
}
