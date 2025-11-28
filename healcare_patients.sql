-- Select all.
SELECT *
FROM healthcare_patients;

-- Groups patients by the month of their first admission and tracks visits over time.
WITH first_visit AS (
    SELECT
        name,
        MIN(date_of_admission::date) AS first_admission_date,
        date_trunc('month', MIN(date_of_admission::date))::date AS cohort_month
    FROM healthcare_patients
    GROUP BY name
),
visits_with_cohort AS (
    SELECT
        p.name,
        p.date_of_admission::date AS date_of_admission,
        date_trunc('month', p.date_of_admission::date)::date AS visit_month,
        f.cohort_month,
        EXTRACT(
            MONTH FROM age(
                date_trunc('month', p.date_of_admission::date),
                f.cohort_month
            )
        ) AS months_since_cohort
    FROM healthcare_patients p
    JOIN first_visit f
      ON p.name = f.name
)
SELECT
    cohort_month,
    months_since_cohort,
    COUNT(DISTINCT name) AS active_customers
FROM visits_with_cohort
GROUP BY cohort_month, months_since_cohort
ORDER BY cohort_month, months_since_cohort;



-- 3. Customer Lifetime Value (CLV) and revenue metrics
-- Aggregates patient-level revenue and visit behavior.
CREATE OR REPLACE VIEW v_customer_ltv AS
WITH customer_agg AS (
    SELECT
        name,
        COUNT(*) AS total_visits,
        SUM(billing_amount) AS total_revenue,
        MIN(date_of_admission) AS first_admission,
        MAX(date_of_admission) AS last_admission
    FROM healthcare_patients
    GROUP BY name
),
customer_ltv AS (
    SELECT
        name,
        total_visits,
        total_revenue,
        first_admission,
        last_admission,
        CASE
            WHEN total_visits = 0 THEN 0
            ELSE total_revenue / total_visits
        END AS avg_revenue_per_visit
    FROM customer_agg
)
SELECT
    name,
    total_visits,
    total_revenue,
    avg_revenue_per_visit,
    first_admission,
    last_admission,
    RANK() OVER (ORDER BY total_revenue DESC) AS revenue_rank
FROM customer_ltv;


-- SELECT * FROM v_customer_ltv ORDER BY total_revenue DESC LIMIT 20;

SELECT * FROM v_customer_ltv ORDER BY total_revenue DESC LIMIT 20;

-- 4. Churn and revenue at risk
-- Flags patients who have not returned for more than N days and estimates revenue at risk.

CREATE OR REPLACE VIEW v_churn_risk AS
WITH customer_agg AS (
    SELECT
        name,
        COUNT(*) AS total_visits,
        SUM(billing_amount::numeric) AS total_revenue,
        MIN(date_of_admission::date) AS first_admission,
        MAX(date_of_admission::date) AS last_admission
    FROM healthcare_patients
    GROUP BY name
),
churn_flags AS (
    SELECT
        name,
        total_visits,
        total_revenue,
        first_admission,
        last_admission,
        CURRENT_DATE AS as_of_date,
        CURRENT_DATE - last_admission AS days_since_last_visit,
        CASE
            WHEN CURRENT_DATE - last_admission > 365 THEN 'Churned'
            WHEN CURRENT_DATE - last_admission BETWEEN 181 AND 365 THEN 'At Risk'
            ELSE 'Active'
        END AS churn_status
    FROM customer_agg
)
SELECT
    name,
    total_visits,
    total_revenue,
    first_admission,
    last_admission,
    as_of_date,
    days_since_last_visit,
    churn_status,
    CASE
        WHEN churn_status IN ('Churned', 'At Risk')
            THEN total_revenue
        ELSE 0
    END AS revenue_at_risk
FROM churn_flags;


SELECT churn_status,
        COUNT(*) AS customers,
       SUM(revenue_at_risk) AS total_revenue_at_risk
 FROM v_churn_risk
 GROUP BY churn_status;


-- Segmentation by value and basic ranking
-- Segments customers into High/Medium/Low based on lifetime revenue.
CREATE OR REPLACE VIEW v_customer_segments AS
WITH customer_metrics AS (
    SELECT
        name,
        COUNT(*) AS total_visits,
        SUM(billing_amount::numeric) AS total_revenue,
        MIN(date_of_admission::date) AS first_admission,
        MAX(date_of_admission::date) AS last_admission
    FROM healthcare_patients
    GROUP BY name
),
segmented AS (
    SELECT
        name,
        total_visits,
        total_revenue,
        first_admission,
        last_admission,
        CASE
            WHEN total_revenue >= 50000 THEN 'High Value'
            WHEN total_revenue >= 20000 THEN 'Medium Value'
            ELSE 'Low Value'
        END AS value_segment
    FROM customer_metrics
)
SELECT
    name,
    total_visits,
    total_revenue,
    first_admission,
    last_admission,
    value_segment,
    ROW_NUMBER() OVER (
        PARTITION BY value_segment
        ORDER BY total_revenue DESC
    ) AS rank_within_segment
FROM segmented;


SELECT * FROM v_customer_segments
WHERE value_segment = 'High Value'
ORDER BY rank_within_segment;