package hello.elan.jpa.auth;

import hello.elan.jpa.AppTests;
import hello.elan.jpa.auth.entity.Resource;
import hello.elan.jpa.auth.entity.Role;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class QueryServiceTest extends AppTests {

    @Autowired
    QueryService queryService;
    @Test
    public void findByUsername() {
    }

    @Test
    public void findRolesByAccount() {
        Set<Role> admin = queryService.findRolesByAccount("admin");
        System.out.println(admin);
    }

    @Test
    public void findResourceByAccount(){
        Set<Resource> admin = queryService.findResourceByAccount("admin");
        System.out.println(admin);
    }
}