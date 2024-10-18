package com.example.myproject.controller;

import com.example.myproject.exception.InvalidTokenException;
import com.example.myproject.exception.NoTokenException;
import com.example.myproject.exception.PaymentFailedException;
import com.example.myproject.service.CheckoutService;
import com.example.myproject.utils.JWTutils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/stylish/api/1.0/order")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;
    @Autowired
    private JWTutils jwtUtils;

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout(@RequestHeader(value = "Authorization", required = false) String token,
                                                        @RequestBody Map<String, Object> body) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                throw new NoTokenException("No token provided");
            }

            String jwtToken = token.substring(7); // Remove "Bearer " prefix

            jwtUtils.parseJWT(jwtToken);

        } catch (NoTokenException nte) {
            throw new NoTokenException("No token found.");

        } catch (InvalidTokenException ite) {
            throw new InvalidTokenException("Invalid token.");
        }

        String prime = (String) body.get("prime");
        Map<String, Object> orderData = (Map<String, Object>) body.get("order");
        Map<String, Object> recipientData = (Map<String, Object>) orderData.get("recipient"); //save and set recipient data to database and dto, already paid

        Long orderId = checkoutService.setOrder(orderData); //save and set order data to database and dto, not pay yet

        Map<String, Object> tapPayData = new HashMap<>();
        tapPayData.put("partner_key", "partner_PHgswvYEk4QY6oy3n8X3CwiQCVQmv91ZcFoD5VrkGFXo8N7BFiLUxzeG");
        tapPayData.put("merchant_id", "AppWorksSchool_CTBC");
        tapPayData.put("prime", prime);
        tapPayData.put("amount", ((Number) orderData.get("total")).longValue());

        try {
            boolean isSuccess = checkoutService.sendTapPay(tapPayData, recipientData); //send to tap pay api and response success or not
//            log.info("isSuccess={}", isSuccess);
            if (isSuccess) {
                checkoutService.setPayment(true, orderId); //save and set payment data to database and dto, already paid
            } else {
                checkoutService.setPayment(false, orderId);
            }

        } catch (Exception e) {
            throw new PaymentFailedException("Error occurred while sending TapPay API.");
        }

        checkoutService.setRecipient(recipientData, orderId);
        List<Map<String, Object>> productList = (List<Map<String, Object>>) orderData.get("list"); //save and set productorder data to database and dto, already paid
        checkoutService.setItems(productList, orderId);

        Map<String, Object> responseData = new HashMap<>();
        Map<String, Object> order = new HashMap<>();
        order.put("number", orderId);
        responseData.put("data", order);

        return ResponseEntity.ok(responseData);

    }

}
