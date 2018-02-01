package com.walletone.p2p.models;

import java.math.BigDecimal;

/**
 * Created by aaronskiy on 05.09.2017.
 */

public class DealRequest {

    public enum DealStateId {
        created, paymentProcessing, paymentHold, paid, canceling, canceled, paymentError, done, confirming, payoutProcessing, payoutProcessingError, completed
    }

    private String dealId;

    private Freelancer freelancer;

    private BigDecimal amount = new BigDecimal(0.0);

    private Integer freelancerPaymentToolId = 0;

    private DealStateId stateId = DealStateId.created;

    public DealRequest (String dealId, Freelancer freelancer, BigDecimal amount) {
        this.dealId = dealId;
        this.freelancer = freelancer;
        this.amount = amount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DealRequest that = (DealRequest) o;
        return that.dealId.equals(this.dealId) && that.freelancer == this.freelancer;
    }

    public String getDealId() {
        return dealId;
    }

    public Freelancer getFreelancer() {
        return freelancer;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getFreelancerPaymentToolId() {
        return freelancerPaymentToolId;
    }

    public DealStateId getStateId() {
        return stateId;
    }

    public void setFreelancerPaymentToolId(Integer freelancerPaymentToolId) {
        this.freelancerPaymentToolId = freelancerPaymentToolId;
    }

    public void setStateId(DealStateId stateId) {
        this.stateId = stateId;
    }
}
