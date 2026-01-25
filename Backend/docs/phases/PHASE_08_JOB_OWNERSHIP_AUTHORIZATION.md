# Phase 08 — Job Ownership & Authorization

## Tag
PHASE_08

## Objective
Introduce ownership-based business authorization.

## Ownership Model
Each job contains:
- createdBy (UUID)

## Rules
- createdBy set from JWT context
- Never accepted from request body
- Ownership enforced only in service layer

## Authorization Rules

### JOB_PROVIDER
- Create jobs
- Update own jobs
- Delete own jobs
- View applicants of own jobs

### ADMIN
- Full access to all jobs
- Ownership bypass allowed

## Restrictions
- No controller-level authorization logic
- No repository-level authorization logic

Status: ✅ Completed
