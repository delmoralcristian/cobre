# SQS CONFIGURATION

awslocal --endpoint-url=http://localhost:4566 sqs create-queue --queue-name event_notifications_sqs --attributes DelaySeconds=10,VisibilityTimeout=60

awslocal --endpoint-url=http://localhost:4566 sqs list-queues

awslocal --endpoint-url=http://localhost:4566 sqs send-message --region=eu-west-1 --queue-url http://localhost:4566/000000000000/event_notifications_sqs --message-body '{"event_id":"EVT012","client_id":"1","event_type":"credit_card_payment","content":"Credit card payment received for $150.00"}'

awslocal --endpoint-url=http://localhost:4566 sqs receive-message --region=eu-west-1 --queue-url http://localhost:4566/000000000000/event_notifications_sqs

awslocal --endpoint-url=http://localhost:4566 sqs purge-queue --region=eu-west-1 --queue-url http://localhost:4566/000000000000/event_notifications_sqs
