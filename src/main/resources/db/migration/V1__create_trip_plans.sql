CREATE TABLE trip_plans (
    id UUID PRIMARY KEY,
    origin VARCHAR(120) NOT NULL,
    destination VARCHAR(120) NOT NULL,
    request_json TEXT NOT NULL,
    plan_markdown TEXT NOT NULL,
    model VARCHAR(80) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_trip_plans_created_at ON trip_plans(created_at DESC);
