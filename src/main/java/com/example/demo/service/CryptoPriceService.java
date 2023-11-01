package com.example.demo.service;

import com.example.demo.entity.CryptoPrice;
import com.example.demo.exception.CryptoPairNotFoundException;
import com.example.demo.repository.CryptoPriceRepository;
import com.example.demo.response.BinancePrices;
import com.example.demo.response.HuobiPrices;
import com.example.demo.response.HuobiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class CryptoPriceService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CryptoPriceRepository cryptoPriceRepository;

    @Scheduled(fixedRate = 10000)
    public void fetchAndStoreCryptoPrices() {
        log.info("running scheduled");
        try {
            List<BinancePrices> binancePrices = fetchPricesFromBinance();
            List<HuobiPrices> huobiPrices = fetchPricesFromHuobi();

            CryptoPrice bestEthPrice = calculateBestPrice(binancePrices, huobiPrices, "ETHUSDT");
            saveOrUpdateCryptoPrice(bestEthPrice);

            CryptoPrice bestBtcPrice = calculateBestPrice(binancePrices, huobiPrices, "BTCUSDT");
            saveOrUpdateCryptoPrice(bestBtcPrice);

        } catch (Exception e) {
            log.error(e);
        }
    }

    public void saveOrUpdateCryptoPrice(CryptoPrice cryptoPrice) {
        Optional<CryptoPrice> existingPrice = cryptoPriceRepository.findByCryptoPair(cryptoPrice.getCryptoPair());
        if (existingPrice.isPresent()) {
            CryptoPrice existingCryptoPrice = existingPrice.get();
            existingCryptoPrice.setBidPrice(cryptoPrice.getBidPrice());
            existingCryptoPrice.setAskPrice(cryptoPrice.getAskPrice());
            cryptoPriceRepository.save(existingCryptoPrice);
        } else {
            cryptoPriceRepository.save(cryptoPrice);
        }
    }


    public List<BinancePrices> fetchPricesFromBinance() {
        String binanceUrl = "https://api.binance.com/api/v3/ticker/bookTicker";
        ResponseEntity<List<BinancePrices>> binanceResponse = restTemplate.exchange(
                binanceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        return binanceResponse.getBody();
    }

    public List<HuobiPrices> fetchPricesFromHuobi() {
        String huobiUrl = "https://api.huobi.pro/market/tickers";
        ResponseEntity<HuobiResponse> huobiResponse = restTemplate.exchange(
                huobiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        return Objects.requireNonNull(huobiResponse.getBody()).getData();
    }

    public CryptoPrice calculateBestPrice(List<BinancePrices> binancePrices, List<HuobiPrices> huobiPrices, String cryptoPair) {
        CryptoPrice bestPrice = new CryptoPrice();
        bestPrice.setCryptoPair(cryptoPair);

        BinancePrices binancePrice = binancePrices.stream()
                .filter(p -> cryptoPair.equalsIgnoreCase(p.getSymbol()))
                .findFirst()
                .orElse(null);

        HuobiPrices huobiPrice = huobiPrices.stream()
                .filter(p -> cryptoPair.equalsIgnoreCase(p.getSymbol()))
                .findFirst()
                .orElse(null);

        // if found in both, compare
        if (binancePrice != null && huobiPrice != null) {
            double bestBid = Math.max(binancePrice.getBidPrice(), huobiPrice.getBid());
            double bestAsk = Math.min(binancePrice.getAskPrice(), huobiPrice.getAsk());
            bestPrice.setBidPrice(bestBid);
            bestPrice.setAskPrice(bestAsk);
        } else if (binancePrice != null) { // only found in binance (unlikely)
            bestPrice.setBidPrice(binancePrice.getBidPrice());
            bestPrice.setAskPrice(binancePrice.getAskPrice());
        } else if (huobiPrice != null) { // only found in huobi (unlikely)
            bestPrice.setBidPrice(huobiPrice.getBid());
            bestPrice.setAskPrice(huobiPrice.getAsk());
        }
        return bestPrice;
    }


    public CryptoPrice getLatestPrice(String cryptoPair) {
        return cryptoPriceRepository.findByCryptoPair(cryptoPair.toUpperCase())
                .orElseThrow(() -> new CryptoPairNotFoundException(cryptoPair.toUpperCase()));
    }
}
