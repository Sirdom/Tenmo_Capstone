package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import io.cucumber.java.eo.Do;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.sound.midi.Soundbank;
import java.sql.PreparedStatement;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransferService {
    private String baseAPI;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;
    private String APPROVED_STATUS = "Approved";
    private String authToken = "";

    ;

    public TransferService(String baseAPI) {
        this.baseAPI = baseAPI;

    }


    public User[] getAllUsers() {
        User[] users = new User[]{};
        try {
            users = restTemplate.exchange(baseAPI + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        } catch (RestClientException e) {
            e.getMessage();
        }
        return users;
    }

//    public Transfer addTransfer(AuthenticatedUser currentUser, int accountTo, double amount) {
//        Transfer transfer = new Transfer();
//        transfer.setAccountFrom(currentUser.getUser().getId());
//        transfer.setAccountTo(accountTo);
//        transfer.setAmount(amount);
//        transfer.setTransferStatusDesc(APPROVED_STATUS);
//        return transfer;
//    }


    public Transfer sendBucks(AuthenticatedUser currentUser, Transfer transfer) {
        transfer = restTemplate.exchange(baseAPI + "transfers", HttpMethod.POST, transferAuthEntity(currentUser, transfer),
                Transfer.class).getBody();
        return transfer;
    }

//    public Transfer sendBucks(AuthenticatedUser currentUser, Integer accountTo, Double amount) {
//        Transfer newTransfer = new Transfer();
//        newTransfer = restTemplate.exchange(baseAPI + "transfers", HttpMethod.POST, transferAuthEntity(currentUser, accountTo, amount),
//                Transfer.class).getBody();
//        return newTransfer;
//    }


//    public void sendBucks() {
//        boolean isSuccessful = false;
//        Transfer transfer = new Transfer();
//
//        Scanner scanner = new Scanner(System.in);
//        RestTemplate rt = new RestTemplate();
//        this.restTemplate = rt;
//
//        User[] users = rt.exchange(baseAPI + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
//        transfer.setAccountFrom(currentUser.getUser().getId());
//        System.out.println("Please select the ID you would like to transfer funds to: ");
//            for (User u : users) {
//                System.out.println(u.getUsername() + " " + u.getId());
//         }
//        String accountToString = scanner.nextLine();
//        transfer.setAccountTo((int)Integer.parseInt(accountToString));
//        System.out.print("Enter amount: $");
//        String amount  = scanner.nextLine();
//        //Can't deserialize int from the setAmount..not sure why since transfer is an object?
//        transfer.setAmount(Double.parseDouble(amount));
//
////            try {
////              rt.exchange(baseAPI + "transfers", HttpMethod.POST, makeAuthEntity(), Transfer.class).getBody();
////            } catch (RestClientException e) {
////                System.out.println(e.getMessage());
////            }
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(currentUser.getToken());
//        HttpEntity<Transfer> entity = new HttpEntity<>(transfer,headers);
//        rt.postForObject(baseAPI + "transfers", entity, Transfer.class);
//        }

    public Transfer[] viewMyTransfer(Integer id){
        Transfer[] myTransfers = null;
        try {
            myTransfers = restTemplate.exchange(baseAPI + "account/transfers/" + id , HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
        } catch (RestClientException e) {
            e.getMessage();
        }
        return myTransfers;
    }

    public Transfer getTransferDetails(Integer id) {
        Transfer transfer = null;
        transfer = restTemplate.exchange(baseAPI + "transfers/" + id, HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
        return transfer;
    }

    public Transfer[] listAllTransfers(Integer id){
        Transfer[] allTransfers = new Transfer[]{};
        try {
            HttpEntity<Transfer[]> entity = restTemplate.exchange(baseAPI + "transfers/", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            entity.getBody();
        } catch (RestClientException e) {
            e.getMessage();
        }
        return allTransfers;
    }


    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity(headers);
    }

    private HttpEntity<Transfer> transferAuthEntity(AuthenticatedUser currentUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }
}

//    AuthenticatedUser currentUser, int accountTo, double amount){
//        Transfer transfer = new Transfer();
//        transfer.setAccountFrom(currentUser.getUser().getId());
//        transfer.setAccountTo(accountTo);
//        transfer.setAmount(amount);
//        transfer.setTransferStatusDesc(APPROVED_STATUS);
//        return transfer;
//}
