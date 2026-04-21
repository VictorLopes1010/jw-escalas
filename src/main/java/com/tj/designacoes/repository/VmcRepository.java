package com.tj.designacoes.repository;

import com.tj.designacoes.entity.Vmc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VmcRepository extends JpaRepository<Vmc, Integer> {
}
