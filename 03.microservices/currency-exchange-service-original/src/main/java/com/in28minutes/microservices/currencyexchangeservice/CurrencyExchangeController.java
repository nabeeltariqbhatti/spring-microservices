package com.in28minutes.microservices.currencyexchangeservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author is Nabeel Tariq Bhatti
 * @created at 3/19/2022
 * @Time at 10:19 PM
 */
@RestController
public class CurrencyExchangeController {

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    @Autowired
    private Environment environment;
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to){
//        ExchangeValue currencyExchange = new ExchangeValue(10000L, "USD", "INR", new BigDecimal(65));
//        currencyExchange

        ExchangeValue byFromAndTo = currencyExchangeRepository.findByFromAndTo(from, to);
        if(byFromAndTo == null) throw new RuntimeException("not found");
        byFromAndTo.setPort(Integer.parseInt(environment.getProperty("server.port")));
        return byFromAndTo;
    }


}
