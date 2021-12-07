package project.lab6.repository;

import project.lab6.domain.User;

public interface RepositoryUser extends Repository<Long, User>{
    User findByEmail(String email);
}
