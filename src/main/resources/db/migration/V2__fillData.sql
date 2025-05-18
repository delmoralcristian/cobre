-- Insert clients
INSERT INTO client (id, name, webhook_url, active) VALUES
(1, 'Client A', 'https://clienta.example.com/webhook', true),
(2, 'Client B', 'https://clientb.example.com/webhook', true);

-- Insert notification events linked to clients
INSERT INTO notification_event (event_id, event_type, content, delivery_date, delivery_status, client_id) VALUES
('EVT001', 'credit_card_payment', 'Credit card payment received for $150.00', '2024-03-15', 'COMPLETED', 1),
('EVT002', 'debit_card_withdrawal', 'ATM withdrawal of $200.00', '2024-03-15', 'COMPLETED', 1),
('EVT003', 'credit_transfer', 'Bank transfer received from Account #4567 for $1,500.00', '2024-03-15', 'FAILED', 2),
('EVT004', 'debit_automatic_payment', 'Monthly utility bill payment of $85.50', '2024-03-15', 'COMPLETED', 2),
('EVT005', 'credit_refund', 'Refund processed for order #789 for $45.99', '2024-03-15', 'FAILED', 1),
('EVT006', 'debit_transfer', 'Money transfer sent to Account #8901 for $500.00', '2024-03-15', 'COMPLETED', 1),
('EVT007', 'credit_deposit', 'Direct deposit received from Employer XYZ for $2,500.00', '2024-03-15', 'COMPLETED', 2),
('EVT008', 'debit_purchase', 'Point of sale purchase at Store ABC for $75.25', '2024-03-15', 'COMPLETED', 2),
('EVT009', 'credit_cashback', 'Cashback reward credited for $25.00', '2024-03-15', 'FAILED', 1),
('EVT010', 'debit_subscription', 'Monthly streaming service payment of $14.99', '2024-03-15', 'COMPLETED', 1);
