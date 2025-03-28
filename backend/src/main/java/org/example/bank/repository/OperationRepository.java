package org.example.bank.repository;

import org.example.bank.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    Operation findFirstByOrderByDateDesc();
    List<Operation> findByUserId(Long userId);

}
