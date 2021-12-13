package project.lab6.setter_interface;

import project.lab6.service.ServiceFriends;

/**
 * interface for controllers which have an idLoggedUser data member
 */
public interface SetterServiceFriends {
    /**
     * sets the service to serviceFriends
     * @param serviceFriends
     */
    void setServiceFriends(ServiceFriends serviceFriends);
}
