package hello.elan.jpa.prod;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author 张一然
 * @date 2020/5/27 16:33
 */
@Service
public class ProdService {
    @PersistenceContext
    private EntityManager em;

    @Transactional(rollbackOn = Exception.class)
    public void batchInsert(List<Product> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Product p = list.get(i);
            p.setId(null);
            em.persist(p);
            if (i % 1000 == 0 || i == (size - 1)) {
                // 每1000条数据执行一次，或者最后不足1000条时执行
                em.flush();
                em.clear();
            }
        }
    }
}
