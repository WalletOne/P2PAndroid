package com.aronskiy_anton.p2p.models;

import java.math.BigDecimal;

/**
 * Created by aaronskiy on 05.09.2017.
 */

public class DealRequest {

    enum DealStateId {
        created, paymentProcessing, paid, canceling, canceled, paymentError, done, confirming, payoutProcessing, payoutProcessingError, completed;
    }

    private Deal deal;

    private Freelancer freelancer;

    private BigDecimal amount = new BigDecimal(0.0);

    private Integer freelancerCardId = 0;

    private DealStateId stateId = DealStateId.created;

    DealRequest (Deal deal, Freelancer freelancer, BigDecimal amount) {
        this.deal = deal;
        this.freelancer = freelancer;
        this.amount = amount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DealRequest that = (DealRequest) o;
        return that.deal == this.deal && that.freelancer == this.freelancer;
    }

}
