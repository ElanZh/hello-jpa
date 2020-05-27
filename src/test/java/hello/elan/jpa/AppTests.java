package hello.elan.jpa;

import hello.elan.jpa.prod.ProdService;
import hello.elan.jpa.prod.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTests {
    @Autowired
    ProdService prodService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testBatchInsert() {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
			Product p = new Product();
			p.setName(UUID.randomUUID().toString());
			list.add(p);
        }
        prodService.batchInsert(list);
    }
}
