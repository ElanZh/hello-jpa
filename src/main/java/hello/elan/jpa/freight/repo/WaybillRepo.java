package hello.elan.jpa.freight.repo;

import hello.elan.jpa.freight.entity.Waybill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WaybillRepo extends JpaRepository<Waybill, Integer>, JpaSpecificationExecutor<Waybill>, QuerydslPredicateExecutor<Waybill>{
}
