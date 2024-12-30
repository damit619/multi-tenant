# Multi tenant Application

## Stack
* Java 21
* Spring Boot 3.4
* Flyway
* Microservices
* Kafka
* AWS

##  Multi Tenant Spring Boot application

* Consume events triggered by a Kafka topic when a file is added to an S3 bucket.
* The application should retrieve the file from tenant specific S3 bucket
* Execute necessary processing on the file content and persist the data into tenant specific database.
* Expose REST APIs to facilitate the retrieval of ingested data where input of API should be tenant_id and device_id
* Ensure that the S3 file is deleted after successful processing, and implement robust handling for failure scenarios, such as errors during S3 file processing or issues during database ingestion.

## To Run this Assignment

* Run all the docker compose files eg. minio, kafka, postgres
* Run producer that will send message to kafka topic after uploading file to s3
* Run consumer that will consume kafka message and will download process file to DB
* You can fetch data using rest endpoint eg.

''' curl --location 'http://localhost:8080/api/v1/tenant?device-id=20' \
--header 'X-TenantId: manipal01' '''