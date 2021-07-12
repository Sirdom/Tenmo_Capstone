package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {

    Account getAccountByUserId(Integer id);

    Double getBalance(Integer id);

    Account depositAccount(Integer accountToId, double amount);

    Account withdrawalAccount(Integer accountFromId, double amount);



}
