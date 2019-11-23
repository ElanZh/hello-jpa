package hello.elan.jpa.auth.repo;

import hello.elan.jpa.auth.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepo extends JpaRepository<Manager, Integer>, JpaSpecificationExecutor<Manager> {

    Manager findByUsername(String username);

    int countByUsername(String admin);
}
