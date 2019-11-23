package hello.elan.jpa.auth;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.elan.jpa.auth.entity.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryService {
    private final JPAQueryFactory jpaQueryFactory;

    public QueryService(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    /**
     * 通过用户名查找manager <br/>
     * 对标{@link AuthService#findByUsername}
     */
    public Manager findByUsername(String username) {
        QManager manager = QManager.manager;
        return jpaQueryFactory.selectFrom(manager).where(manager.username.eq(username)).fetchOne();
    }

    /**
     * 查找用户拥有的角色 <br/>
     * 对标{@link AuthService#findRolesByAccount}
     */
    public List<Role> findRolesByAccount(String username) {
        QRole role = QRole.role;
        QManager manager = QManager.manager;
        return jpaQueryFactory.selectFrom(role)
                              .leftJoin(role.managers, manager)
                              .where(manager.username.eq(username)).fetch();
    }

    /**
     * 查询用户拥有的资源 <br/>
     * 对标{@link AuthService#findResourceByAccount}
     */
    public List<Resource> findResourceByAccount(String username) {
        QResource resource = QResource.resource;
        QRole role = QRole.role;
        QManager manager = QManager.manager;
        return jpaQueryFactory.selectFrom(resource)
                              .leftJoin(resource.roles, role)
                              .leftJoin(role.managers, manager)
                              .where(manager.username.eq(username)).fetch();
    }
}
