# SQS CONFIGURATION

awslocal --endpoint-url=http://localhost:4566 sqs create-queue --queue-name event_notifications_sqs --attributes DelaySeconds=10,VisibilityTimeout=60

awslocal --endpoint-url=http://localhost:4566 sqs list-queues

awslocal --endpoint-url=http://localhost:4566 sqs send-message --region=eu-west-1 --queue-url http://localhost:4566/000000000000/event_notifications_sqs --message-body '{"events":[{"event_id":"EVT001","event_type":"credit_card_payment","content":"Credit card payment received for $150.00"}]}'

