# Shopping Cart
This microservice has shopping cart detail.
* Rest call product information service -> get product
* Rest call delivery and campaign service -> get delivery, campaign, coupon

## Tech Stack
* Java JDK 11
* Maven 3.8.1
* Spring Boot v2.9.2
* Couchbase DB
* Swagger

## Running the Application
#### Build the app
`mvn clean install`

### Up Couchbase DB
`docker pull couchbase`

`docker run -d --name couchbasedb -p 8091-8094:8091-8094 -p 11210:11210 couchbase`

* Visit the admin client from http://localhost:8091/
* Follow setup a new cluster. You can give it any name.
* While configuring disk, memory and services uncheck search, analytics and eventing services and finish with other default configurations.
* Go to Buckets tab from the left navigation and click "ADD BUCKET" on top right of the page.
* Give it "cart" as name
* From Security tab, click "ADD USER" and define a username and password for our application.
* username : couchuser - password : pass123
* Run spring boot application
* Create Index
```
CREATE INDEX `cartIdIndex` ON `cart`(`id`)
CREATE INDEX `cartIsActiveIndex` ON `cart`(`isActive`)
CREATE INDEX `cartItemCartIdIndex` ON `cart`(`cartId`)
CREATE INDEX `cartItemProductIdIndex` ON `cart`(`productId`)
CREATE INDEX `cartUserIdIndex` ON `cart`(`userId`)
CREATE INDEX `idIndex` ON `cart`((meta().`id`))
```

## Application Details

Once application is up, api documentation can be seen at `http://localhost:8080/swagger-ui.html`

### Sample Endpoint

#### [GET] /cart/cartDiscount (getCartDiscount)

```
Body: 
{
  couponId = 1
  userId = 10
}
```

