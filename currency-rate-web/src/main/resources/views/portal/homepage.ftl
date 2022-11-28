<#import "../layout/defaultLayout.ftl" as layout>
<@layout.defaultLayout>

    <div class="columns mt-6">
        <div class="column is-half is-offset-one-quarter">
            <form method="get">
                <div class="card p-2" style="border: black solid 1px">
                    <div class="columns">
                        <div class="column is-one-third has-text-centered">
                            <h2 class="title is-5">From</h2>
                            <div class="select">
                                <select id="fromCurrency" name="from">
                                    <#list currencyList as currency>
                                        <option value="${currency}" <#if (fromCurrency == currency)>selected</#if>>
                                            ${currency}
                                        </option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="column is-one-third has-text-centered">
                            <h2 class="title is-5">Amount</h2>
                            <input id="value" name="value" class="input" type="number" step="0.01" value="${value?string.computer}"
                                   min="0.00">
                        </div>
                        <div class="column is-one-third has-text-centered">
                            <h2 class="title is-5">To</h2>
                            <div class="select">
                                <select id="toCurrency" name="to">
                                    <#list currencyList as currency>
                                        <option value="${currency}" <#if (toCurrency == currency)>selected</#if>>
                                            ${currency}
                                        </option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="columns">
                        <div class="column is-one-third"></div>
                        <div class="column is-one-third"></div>
                        <div class="column is-one-third has-text-right pr-5">
                            <button class="button is-info " id="submit">Convert</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <#if (exchangeInformation??)>
        <div class="has-text-centered mt-6">
            <h2 class="title is-2" id="result">
                ${exchangeInformation.fromValue()?string["0.00"]} ${exchangeInformation.fromCurrency()}
                =
                ${exchangeInformation.toValue()?string["0.000000"]} ${exchangeInformation.toCurrency()}
            </h2>
        </div>
    </#if>

</@layout.defaultLayout>
