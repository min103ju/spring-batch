package com.citizen.springbatch.repository;

import com.citizen.springbatch.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author : citizen103
 */
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

}
