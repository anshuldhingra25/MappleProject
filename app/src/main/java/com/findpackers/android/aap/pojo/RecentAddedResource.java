package com.findpackers.android.aap.pojo;

/**
 * Created by narendra on 2/20/2018.
 */

public class RecentAddedResource {
    String txnbalance;
    String txndate;
    String txnaddedby;
    String txnId;
    String avlablebalance;
    String rchrgAmount;
    public String getRchrgAmount() {
        return rchrgAmount;
    }

    public void setRchrgAmount(String rchrgAmount) {
        this.rchrgAmount = rchrgAmount;
    }



    public String getTxnbalance() {
        return txnbalance;
    }

    public void setTxnbalance(String txnbalance) {
        this.txnbalance = txnbalance;
    }

    public String getTxndate() {
        return txndate;
    }

    public void setTxndate(String txndate) {
        this.txndate = txndate;
    }

    public String getTxnaddedby() {
        return txnaddedby;
    }

    public void setTxnaddedby(String txnaddedby) {
        this.txnaddedby = txnaddedby;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getAvlablebalance() {
        return avlablebalance;
    }

    public void setAvlablebalance(String avlablebalance) {
        this.avlablebalance = avlablebalance;
    }
}