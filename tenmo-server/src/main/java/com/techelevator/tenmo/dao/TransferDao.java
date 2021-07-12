package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.List;

public interface TransferDao {

    List<Transfer> listTransfers();

    Transfer viewTransferDetails(int id);

    int sendTransfer(Transfer newTransfer);
    //Transfer sendTransfer(Transfer transfer);

    List<Transfer> viewMyTransfers(Integer id);

    Transfer detailsOfTransfers(Integer transferId);




}
