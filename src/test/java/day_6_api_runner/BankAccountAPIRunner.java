package day_6_api_runner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import pojo.CustomResponse;
import pojo.RequestBody;
import utilities.APIRunner;
import utilities.CashwiseAuthorization;
import utilities.Config;

import static utilities.CashwiseAuthorization.getToken;

public class BankAccountAPIRunner {

    @Test
    public void test_1_getListOfBankAccounts() throws JsonProcessingException {
        // https://backend.cashwise.us  /api/myaccount/bankaccount

        String url = Config.getProperty("baseUrl") + "/api/myaccount/bankaccount";

        Response response = RestAssured.given()
                .auth().oauth2(getToken())
                .get(url);

        //  response.prettyPrint();
        ObjectMapper mapper = new ObjectMapper();

        CustomResponse[] customResponses = mapper.readValue(response.asString(), CustomResponse[].class);

        int sizeOfBankAccounts = customResponses.length;

        for (int i = 0; i < sizeOfBankAccounts; i++) {

            System.out.println("Bank ID" + customResponses[i].getId());
            Assert.assertNotNull(customResponses[i].getId());
            System.out.println("Bank account name: " + customResponses[i].getBank_account_name());
            Assert.assertNotNull(customResponses[i].getBank_account_name());

        }
    }


        @Test
        public void test_2_getListOfBankAccounts_ApiRunner(){
            String path = "/api/myaccount/bankaccount";
            APIRunner.runGET(path );
            CustomResponse[] customResponses =  APIRunner.getCustomResponseArray();

           // System.out.println(   APIRunner.getCustomResponseArray()[0].getId() );

            int sizeOfArray = customResponses.length;

            for (int i=0; i<sizeOfArray; i++){
                System.out.println(  "ID: " +  customResponses[i].getId()   );
                Assert.assertNotNull(  customResponses[i].getId()   );

                System.out.println(  "Bank account name: " + customResponses[i].getBank_account_name()  );
                Assert.assertNotNull( customResponses[i].getBank_account_name()   );


            }
    }


    @Test
    public void test_3_createNewBankAccount() throws JsonProcessingException {
        Faker faker = new Faker();
        // https://backend.cashwise.us   /api/myaccount/bankaccount    // STEP -==> set up your URL
        String url = Config.getProperty("baseUrl") + "/api/myaccount/bankaccount";

        RequestBody requestBody = new RequestBody();
        requestBody.setType_of_pay("CASH");
        requestBody.setBank_account_name(  faker.company().name()+ " bank"  );
        requestBody.setDescription( faker.commerce().department()+ " company" );
        requestBody.setBalance( faker.number().numberBetween(200, 15000)  );

        Response response = RestAssured.given()
                .auth().oauth2(   getToken()  )
                .contentType( ContentType.JSON )
                .body( requestBody )
                 .post(url );

        ObjectMapper mapper = new ObjectMapper();
        CustomResponse customResponse = mapper.readValue(  response.asString(), CustomResponse.class  );

        String bankId = customResponse.getId();
        response.prettyPrint();

    }









}
