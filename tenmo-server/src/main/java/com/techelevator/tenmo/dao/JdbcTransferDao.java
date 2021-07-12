package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;
    private JdbcAccountDao accountDao;


    public JdbcTransferDao(JdbcTemplate jdbcTemplate, JdbcAccountDao accountDao){
            this.jdbcTemplate = jdbcTemplate;
            this.accountDao = accountDao;

    }

    @Override
    public List<Transfer> listTransfers(){
      String sql = "SELECT transfer_id, account_from, account_to, amount FROM transfers";
      List<Transfer> allTransfers = new ArrayList<>();
      SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
      while(results.next()){
           Transfer transfer = mapRowToTransfer(results);
           allTransfers.add(transfer);
      }
      return allTransfers;
    }

    //do we need to map or use queryforobject?
    @Override
    public Transfer viewTransferDetails(int id){
        Transfer transfer = null;
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if(results.next()){
            transfer = mapRowToTransfer(results);
        }
    return  transfer;
    }

// Keeping in different attempts to learn from, pay attention to what we were trying to pass in
//    @Override
//    public void sendTransfer(Account accountFrom, Account accountTo, double amount){
//        Transfer transfer = new Transfer();
//        String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
//                "VALUES(2, 2, ?, ?, ?)";
//        jdbcTemplate.update(sql, accountFrom.getAccountId(), accountTo.getAccountId(), amount);
//        accountDao.withdrawalAccount(accountFrom, amount);
//        accountDao.depositAccount(accountTo,amount);
//
//    }
//
//    @Override
//    public void sendTransfer(Integer accountFrom, Integer accountTo, double amount){
//        String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
//                "VALUES(2, 2, (SELECT account_id FROM accounts WHERE user_id = ?), (SELECT account_id FROM accounts WHERE user_id = ?), ?)";
//        jdbcTemplate.update(sql, accountFrom, accountTo, amount);
//        accountDao.withdrawalAccount(accountFrom, amount);
//        accountDao.depositAccount(accountTo,amount);

//    }

    @Override
    public int sendTransfer(Transfer newTransfer) {
        int transferId = 0;
        double amount = newTransfer.getAmount();
        Account accountFrom = accountDao.getAccountByUserId(newTransfer.getAccountFrom());
        Account accountTo = accountDao.getAccountByUserId(newTransfer.getAccountTo());

        if (accountFrom == accountTo) {
            System.out.println("Invalid transfer - same user from and to");
        }

        if (amount <= accountFrom.getBalance()) {
        //Need to catch nulls? Cancel through validation?
            String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES(2, 2, ?, ?, ?) RETURNING transfer_id;";
            transferId = jdbcTemplate.queryForObject(sql, Integer.class,
                    accountFrom.getAccountId(), accountTo.getAccountId(), amount);

            accountDao.withdrawalAccount(newTransfer.getAccountFrom(), amount);
            accountDao.depositAccount(newTransfer.getAccountTo(), amount);
        }
        else {
            System.out.println("Insufficient funds");
        }
        return transferId;
    }

//    @Override
//    public List<Transfer> viewMyTransfers(Integer id) {
//        List<Transfer> myTransfers = new ArrayList<>();
//        String sql = "SELECT t.* FROM transfers t" +
//        " JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id" +
//        " JOIN transfer_statuses ts ON ts.transfer_status_id = t.transfer_status_id" +
//        " JOIN accounts a ON t.account_from = a.account_id" +
//        " JOIN accounts b ON t.account_to = b.account_id" +
//        " JOIN users u ON u.user_id = a.user_id" +
//        " JOIN users v ON v.user_id = b.user_id" +
//        " WHERE a.user_id = ? OR b.user_id = ?";
//
//        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id, id);
//        if (rowSet.next()){
//            Transfer transfer = mapRowToTransfer(rowSet);
//            myTransfers.add(transfer);
//        }
//        return myTransfers;
//    }
@Override
public List<Transfer> viewMyTransfers(Integer id) {
    List<Transfer> myTransfers = new ArrayList<>();
    String sql = "SELECT t.* FROM transfers t" +
            " JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id" +
            " JOIN transfer_statuses ts ON ts.transfer_status_id = t.transfer_status_id" +
            " JOIN accounts a ON t.account_from = a.account_id" +
            " JOIN accounts b ON t.account_to = b.account_id" +
            " JOIN users u ON u.user_id = a.user_id" +
            " JOIN users v ON v.user_id = b.user_id" +
            " WHERE a.user_id = ? OR b.user_id = ?";

    SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id, id);
    if (rowSet.next()){
        Transfer transfer = mapRowToTransfer(rowSet);
        myTransfers.add(transfer);
    }
    return myTransfers;
}
    //didn't have time to connect
    @Override
    public Transfer detailsOfTransfers(Integer transferId) {
         Transfer transfer = new Transfer();
        String sql = "SELECT t.* FROM transfers t" +
                " JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id" +
                " JOIN transfer_statuses ts ON ts.transfer_status_id = t.transfer_status_id" +
                " JOIN accounts a ON t.account_from = a.account_id" +
                " JOIN accounts b ON t.account_to = b.account_id" +
                " JOIN users u ON u.user_id = a.user_id" +
                " JOIN users v ON v.user_id = b.user_id" +
                " WHERE t.transfer_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);
        transfer = mapRowToTransfer(rowSet);
        return transfer;
    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setStatusId (rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getDouble("amount"));
        return transfer;
    }



}
