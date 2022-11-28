package com.sample.currencyrate.datastore.repository;

import com.sample.currencyrate.domain.entity.CurrencyRate;
import com.sample.currencyrate.domain.enumeration.Currency;
import com.sample.currencyrate.domain.repository.EuropeanCentralBankRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the repository "EuropeanCentralBank".
 */
public class EuropeanCentralBankRepositoryImpl implements EuropeanCentralBankRepository {

    private static final String ECB_EXCHANGE_RATES_ENDPOINT = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    @Override
    public List<CurrencyRate> findAllCurrencyRateBaseEuro() {

        final List<CurrencyRate> currencyRateList = new ArrayList<>();
        try {
            final URL url = new URL(ECB_EXCHANGE_RATES_ENDPOINT);
            final URLConnection urlConnection = url.openConnection();
            final InputStream inputStream = urlConnection.getInputStream();

            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final Document document = documentBuilder.parse(inputStream);

            final NodeList nodes = document.getElementsByTagName("Cube");
            for (int idx = 0; idx < nodes.getLength(); idx += 1) {
                final Node node = nodes.item(idx);

                if (node.getAttributes().getNamedItem("currency") != null) {
                    final Currency currency = Currency.valueOf(node.getAttributes().getNamedItem("currency").getNodeValue());
                    final BigDecimal rate = new BigDecimal(node.getAttributes().getNamedItem("rate").getNodeValue());

                    final CurrencyRate currencyRate = new CurrencyRate(
                        currency,
                        BigDecimal.ONE.divide(rate, 10, RoundingMode.HALF_DOWN),
                        rate);

                    currencyRateList.add(currencyRate);
                }
            }
        } catch (final ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        return currencyRateList;
    }
}
