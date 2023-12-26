package com.app.servlets;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/paypal-checkout")
public class PayPalCheckoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String clientId = "AZqmOo1jE68HiVm2lR9PO2y3A9jXOUD3jQYy5dBoyAZT39_ZEJb5lBfJvi7HEziABqul7uD8ZGwnD5MB";
        String clientSecret = "ENOTGw4KQkeTw9v0cDV9ZU7o4jKs56iezts0P8blT5R_Bfoa4ivNirGOQimvzx5pJjPfXvfAPiWTRHjw";

        APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");

        // Set up payment details
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal("10.00"); // Set the total amount

        Transaction transaction = new Transaction();
        transaction.setDescription("Payment description");
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // Set up redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/your-app/cancel");
        redirectUrls.setReturnUrl("http://localhost:8080/your-app/success");

        // Create payment
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);

        try {
            Payment createdPayment = payment.create(apiContext);

            // Get the approval URL from the created payment
//            String approvalUrl = createdPayment.getLinks().stream()
//                    .filter(link -> "approval_url".equals(link.getRel()))
//                    .findFirst().get().getHref();

            // Send the approval URL back to the front end
//            resp.getWriter().write("{\"approvalUrl\": \"" + approvalUrl + "\"}");

        } catch (PayPalRESTException e) {
            e.printStackTrace();
            resp.getWriter().write("{\"error\": \"Error during PayPal payment creation\"}");
        }
    }
}
