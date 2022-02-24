package com.citizen.springbatch.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * author : citizen103
 */
@Getter
@NoArgsConstructor
@Entity
public class PurchaseOrder {

    @Id
    @GeneratedValue
    private Long id;

    private String memo;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> productList;

    @Builder
    public PurchaseOrder(String memo, List<Product> productList) {
        this.memo = memo;
        this.productList = productList;
    }
}