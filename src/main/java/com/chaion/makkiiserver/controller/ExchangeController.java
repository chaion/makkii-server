package com.chaion.makkiiserver.controller;

import com.chaion.makkiiserver.model.MarketPrice;
import com.chaion.makkiiserver.services.exchange.ExchangePool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Api(value="Exchange Market APIs", description="crypto currency, fiat currency exchange rates")
@RestController
@RequestMapping("market")
public class ExchangeController {

    @Autowired
    private ExchangePool pool;

    @ApiOperation(value="Get exchange rate: crypto currency->fiat currency")
    @GetMapping(value="/price")
    public MarketPrice getPrice(
            @ApiParam(value="crypto currency symbol")
            @RequestParam(value = "crypto") String crypto,
            @ApiParam(value="fiat currency symbol")
            @RequestParam(value = "fiat") String fiat) {
        BigDecimal price = pool.getPrice(crypto, fiat);
        if (price != null) {
            MarketPrice mp = new MarketPrice();
            mp.setCrypto(crypto);
            mp.setFiat(fiat);
            mp.setPrice(price);
            return mp;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                crypto + "<->" + fiat + " exchange rate not found.");
    }

    @ApiOperation(value="Get batches of exchange rates: crypto currencies -> fiat currencies")
    @GetMapping(value="/prices")
    public List<MarketPrice> getPrices(
            @ApiParam(value="crypto currency symbols delimited by ','")
            @RequestParam(value = "cryptos") String cryptoCurrencies,
            @ApiParam(value="fiat currency symbol")
            @RequestParam(value = "fiat") String fiat) {
        List<MarketPrice> list = new ArrayList<>();
        String[] cryptos = cryptoCurrencies.split(",");
        for (String crypto: cryptos) {
            BigDecimal price = pool.getPrice(crypto, fiat);
            if (price != null) {
                MarketPrice mp = new MarketPrice();
                mp.setCrypto(crypto);
                mp.setFiat(fiat);
                mp.setPrice(price);
                list.add(mp);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                crypto + "<->" + fiat + " exchange rate not found.");
            }
        }
        return list;
    }
}