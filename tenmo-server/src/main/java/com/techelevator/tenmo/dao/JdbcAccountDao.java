package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

// should the ifs be while?
    @Override
    public Double getBalance(Integer userId) {
        String sqlString = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlString, userId);
        Double balance = 0.00;
        try {
            if (results.next()) {
                balance = results.getDouble("balance");
            }
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return balance;
    }

    @Override
    public Account getAccountByUserId(Integer userId) {
        Account userAccount;
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        if(rowSet.next()){
            userAccount = mapRowToAccount(rowSet);
        }else {
            userAccount = null;
        }
        return  userAccount;
    }

//
//    @Override
//    public Account depositAccount(Account account, Double amount) {
//        Account updatedAccount;
//        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
//        jdbcTemplate.update(sql, amount, account.getUserId());
//        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?;",
//                account.getUserId());
//        if(rowSet.next()) {
//            updatedAccount = mapRowToAccount(rowSet);
//        } else {
//            updatedAccount = null;
//        }
//        return updatedAccount;
//
//    }

    @Override
    public Account depositAccount(Integer accountToId, double amount) {
        Account updatedAccount;
        String sql = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?";
        jdbcTemplate.update(sql, amount, accountToId);
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?;",
                accountToId);
        if(rowSet.next()) {
            updatedAccount = mapRowToAccount(rowSet);
        } else {
            updatedAccount = null;
        }
   return updatedAccount;

    }
//    @Override
//    public Account withdrawalAccount(Account account, Double amount) {
//        Account updatedAccount;
//        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
//        jdbcTemplate.update(sql, amount, account.getUserId());
//        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?;",
//                account.getUserId());
//        if(rowSet.next()) {
//            updatedAccount = mapRowToAccount(rowSet);
//        } else {
//            updatedAccount = null;
//        }
//        return updatedAccount;
//
//    }
@Override
public Account withdrawalAccount(Integer accountFromId, double amount) {
    Account updatedAccount;
    String sql = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?";
    jdbcTemplate.update(sql, amount, accountFromId);
    SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?;",
            accountFromId);
    if (rowSet.next()) {
        updatedAccount = mapRowToAccount(rowSet);
    } else {
        updatedAccount = null;
    }
   return updatedAccount;
}

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setBalance(rs.getDouble("balance"));
        account.setUserId (rs.getInt("user_id"));
        return account;
    }

}
