# crypto-portfolio-value
Calculates the value by position and total of a portfolio of cryptocurrencies provided as a text file

## Run the application
### maven exec
A maven POM file was added for convenience. To run the application directly with maven, please execute the following command:

    mvn compile test exec:java -Dexec.mainClass="com.spouyet.crypto.CryptoPortfolioValue"
    
To use other parameters than the defaults (bobs_crypto.txt and EUR), you may invoke the following command:

    mvn compile test exec:java -Dexec.mainClass="com.spouyet.crypto.CryptoPortfolioValue" -Dexec.args="alices_crypto.txt USD"
    
### maven package and run with java
Alternatively to create a JAR file and execute it:
    
    mvn package
    java -jar target/crypto-portfolio-value-1.0-SNAPSHOT-jar-with-dependencies.jar

To override the default parameters (bobs_crypto.txt and EUR), you may invoke the following command:

    mvn package
    java -jar target/crypto-portfolio-value-1.0-SNAPSHOT-jar-with-dependencies.jar alices_crypto.txt USD


## Implementation notes
### API endpoint
Bob found a REST API that returns the price for one cryptocurrency with the following request:
  
    https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=EUR

The API provider however offers another endpoint which allows to query multiple symbols at once, as shown below:

    https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH,XRP&tsyms=EUR
    
The application uses the latter endpoint because it is more efficient (especially for large portfolios) and provides prices
from the same timestamp. As a consequence, the JSON parsing needed to be a bit more robust than with the originally planned endpoint.



