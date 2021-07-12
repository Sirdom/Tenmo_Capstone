package com.techelevator.tenmo.services;

//Probably could just put this in Transfer service
import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class ApiService {

    private String baseAPI;
    private RestTemplate restTemplate;
    private AuthenticatedUser currentUser;

    public ApiService(String url, AuthenticatedUser currentUser){
        this.restTemplate = new RestTemplate();
        this.currentUser = currentUser;
        this.baseAPI = url;
    }

    public double getBalance() {
        Double balance = null;
        try {
            balance = restTemplate.exchange(baseAPI + "balance/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Double.class).getBody();
            System.out.println("Your current account balance is: $" + balance);
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
        }
        return balance;
//        Double balance = 0.00;
//        try {
//            balance = restTemplate.getForObject(BASE_API + "/accounts/{id}", double.class);
//        }
//        catch (RestClientResponseException e) {
//            System.out.println(e.getMessage());
//        }
//        return balance;
    }

    private HttpEntity makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }

}
