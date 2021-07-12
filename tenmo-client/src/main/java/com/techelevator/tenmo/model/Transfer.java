package com.techelevator.tenmo.model;

public class Transfer {
    private Integer transferId;
    private Integer transferTypeId;
    private String transferStatusDesc;
    private String transferTypeDesc;
    private Integer statusId;
    private Integer accountFrom;
    private Integer accountTo;
    private double amount;

    public Transfer(){}

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }

    public Transfer(Integer transferId, String transferStatusDesc, String transferTypeDesc, Integer transferTypeId, Integer statusId, Integer accountFrom, Integer accountTo, double amount) {
        this.transferId = transferId;
        this.transferStatusDesc = transferStatusDesc;
        this.transferTypeDesc = transferTypeDesc;
        this.transferTypeId = transferTypeId;
        this.statusId = statusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Integer getTransferId() {
        return transferId;
    }

    public void setTransferId(Integer transferId) {
        this.transferId = transferId;
    }

    public Integer getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(Integer transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Integer accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Integer getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Integer accountTo) {
        this.accountTo = accountTo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
