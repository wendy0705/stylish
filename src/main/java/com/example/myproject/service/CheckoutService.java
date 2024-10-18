package com.example.myproject.service;

import com.example.myproject.dto.OrderDTO;
import com.example.myproject.dto.PaymentDTO;
import com.example.myproject.dto.ProductOrderDTO;
import com.example.myproject.dto.RecipientDTO;
import com.example.myproject.repository.OrderRepository;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CheckoutService {

    @Autowired
    OrderRepository orderRepository;

    public Long setOrder(Map<String, Object> orderData) {
        Long id;
        OrderDTO order = new OrderDTO();
        order.setShipping((String) orderData.get("shipping"));
        order.setPayment((String) orderData.get("payment"));
        order.setSubtotal(new BigDecimal((Integer) orderData.get("subtotal")).longValue());
        order.setFreight(new BigDecimal((Integer) orderData.get("freight")).longValue());
        order.setTotal(new BigDecimal((Integer) orderData.get("total")).longValue());
        id = orderRepository.saveOrder(order); //save and get orderId
        order.setId(id);
        return id;
    }

    public boolean sendTapPay(Map<String, Object> tapPayData, Map<String, Object> recipientData) {

        boolean isSuccess = false;

        JSONObject jsonBody = new JSONObject(tapPayData);

        JSONObject cardholder = new JSONObject();
        cardholder.put("phone_number", (String) recipientData.get("phone"));
        cardholder.put("name", (String) recipientData.get("name"));
        cardholder.put("email", (String) recipientData.get("email"));
        cardholder.put("address", (String) recipientData.get("address"));

        jsonBody.put("cardholder", cardholder);
        jsonBody.put("details", "TapPay Test");

        HttpResponse<String> response = Unirest.post("https://sandbox.tappaysdk.com/tpc/payment/pay-by-prime")
                .header("Content-Type", "application/json")
                .header("x-api-key", "partner_PHgswvYEk4QY6oy3n8X3CwiQCVQmv91ZcFoD5VrkGFXo8N7BFiLUxzeG")
                .body(jsonBody.toString())
                .asString();
//        log.info(response.getBody());
//        log.info(response.getStatusText());

        if (response.getStatus() == 200) {
            JSONObject responseBody = new JSONObject(response.getBody());
            int status = responseBody.getInt("status");

            if (status == 0) {
                isSuccess = true;
            } else {
                isSuccess = false;
            }
        }

        return isSuccess;
    }

    public void setPayment(boolean status, Long orderId) {
        PaymentDTO payment = new PaymentDTO();
        payment.setSuccessful(status);
        payment.setOrderId(orderId);

        orderRepository.savePayment(payment);
    }

    public void setRecipient(Map<String, Object> recipientData, Long orderId) {

        RecipientDTO recipient = new RecipientDTO();
        recipient.setName((String) recipientData.get("name"));
        recipient.setPhone((String) recipientData.get("phone"));
        recipient.setEmail((String) recipientData.get("email"));
        recipient.setAddress((String) recipientData.get("address"));
        recipient.setTime((String) recipientData.get("time"));
        recipient.setOrderId(orderId);
        orderRepository.saveRecipient(recipient);
    }

    public void setItems(List<Map<String, Object>> orderData, Long orderId) {
        List<Map<String, Object>> itemList = orderData;
        List<ProductOrderDTO> items = new ArrayList<>();

        for (Map<String, Object> itemData : itemList) {
            log.info("itemData = {}", itemData);
            Long productId = Long.parseLong(itemData.get("id").toString());
            Long quantity = new BigDecimal((Integer) itemData.get("qty")).longValue();

            ProductOrderDTO item = new ProductOrderDTO();
            item.setProductId(productId);
            item.setQuantity(quantity);
            item.setOrderId(orderId);

            items.add(item);
        }

        orderRepository.saveOrderList(items, orderId);
    }
}
