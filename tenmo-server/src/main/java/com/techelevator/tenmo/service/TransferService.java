package com.techelevator.tenmo.service;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TransferService {

private JdbcTransferDao jdbcTransferDao;
private JdbcAccountDao jdbcAcccountDao;

// injecting the jdbcTransferDao into constructor causes server error - BeanCreationException
    //UPDATE - Fixed by creating public default cons
//public TransferService(){};
//need to get account by accountid
public TransferService(JdbcTransferDao jdbcTransferDao){
    this.jdbcTransferDao = jdbcTransferDao;
};

public void sendTransfer(Transfer newTransfer){
    jdbcTransferDao.sendTransfer(newTransfer);
}

//public void sendTransfer(Transfer newTransfer){
//
//}

public List<Transfer> viewMyTransfers(Integer id){
    return jdbcTransferDao.viewMyTransfers(id);
}

public Transfer detailsOfTransfers(Integer id) {
    return jdbcTransferDao.detailsOfTransfers(id);
}

}
