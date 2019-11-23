package hello.elan.jpa.auth.repo;

import hello.elan.jpa.auth.entity.Resource;
import hello.elan.jpa.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface ResourceRepo extends JpaRepository<Resource, Integer>, JpaSpecificationExecutor<Resource> {
    Resource findByName(String name);

    @Query("select r from Resource r where r.name= 'root' and r.parent is null order by r.sort")
    Resource findRoot();

    Set<Resource> findByRoles(Set<Role> roles);

    Set<Resource> findAllByIdIn(Set<Integer> ids);

    @Query("select count(r) from Resource r " +
            "where r.parent = :#{#param.parent} and r.sort = :#{#param.sort}")
    int countParentAndSort(@Param("param") Resource param);

    @Query("select count(r) from Resource r " +
            "where r.parent = :#{#param.parent} and r.id <> :#{#param.id} and r.sort = :#{#param.sort}")
    int countParentAndSortAndNotIncludeSelf(@Param("param") Resource param);
}
