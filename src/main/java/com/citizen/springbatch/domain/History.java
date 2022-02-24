package com.citizen.springbatch.domain;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * author : citizen103
 */
@Getter
@NoArgsConstructor
@Entity
public class History {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long purchaseOrderId;

    @Column
    private String productIdList;

    @Builder
    public History(Long purchaseOrderId, List<Product> productList) {
        this.purchaseOrderId = purchaseOrderId;
        this.productIdList = productList.stream()
            .map(product -> String.valueOf(product.getId()))
            .collect(Collectors.joining(","));
    }
}
