package project.lab6.repository.repointerface;

import project.lab6.domain.entities.User;

public interface RepositoryUser extends Repository<Long, User> {
    User findByEmail(String email);
}
