package com.dicicip.starter.repository;

import com.dicicip.starter.model.Production;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductionRepository extends CrudRepository<Production, Long> {

    @Query("SELECT production FROM Production production WHERE production.payment_id = :payment_id")
    Optional<Production> findByPaymentId(@Param("payment_id") Integer payment_id);
}
