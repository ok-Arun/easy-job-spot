# Phase 06 — Service Layer (Business Logic)

## Objective
Centralize all business decisions.

## Services

### AuthService
- User registration
- Password encryption
- Authentication
- JWT generation

### JobService
- Create / update / delete jobs
- Pagination and filtering
- Job validation

### ApplicationService
- Apply to job
- Prevent duplicate applications
- Fetch user applications

## Rules
- Controllers must be thin
- All decisions live in services
- JWT identity required

Status: ✅ Completed
