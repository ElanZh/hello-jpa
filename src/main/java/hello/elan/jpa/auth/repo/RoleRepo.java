package hello.elan.jpa.auth.repo;

import hello.elan.jpa.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ElanZh
 * @date 2019/5/8 15:41
 * @Package com.hello.ocean.jpa.biz.auth.repo
 * @Description
 */
@Repository
public interface RoleRepo extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    Long countByRoleName(String roleName);

    Role findByRoleName(String roleName);

    /**
     * 查找
     * @param roleName
     * @param roleId
     * @return
     */
    Integer countByRoleNameAndAndIdIsNot(String roleName, Integer roleId);

    @Query("select new Role(r.id, r.roleName) from Role r where r.status = 'EFFECTIVE'")
    List<Role> findIdAndRoleNameAllEffective();
}
