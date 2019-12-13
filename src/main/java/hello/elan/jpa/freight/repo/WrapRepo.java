package hello.elan.jpa.freight.repo;

import hello.elan.jpa.freight.entity.Wrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WrapRepo extends JpaRepository<Wrap, Integer>, JpaSpecificationExecutor<Wrap>, QuerydslPredicateExecutor<Wrap> {
}
