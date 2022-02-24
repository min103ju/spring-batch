package com.citizen.springbatch.repository;

import com.citizen.springbatch.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author : citizen103
 */
public interface HistoryRepository extends JpaRepository<History, Long> {

}
