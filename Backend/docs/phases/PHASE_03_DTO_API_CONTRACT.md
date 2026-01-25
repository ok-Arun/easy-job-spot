# Phase 03 — DTO Design (API Contract)

## Objective
Define a stable and frontend-safe API contract.

## DTO Structure

dto/
- request/
    - RegisterRequest
    - LoginRequest
    - JobCreateRequest
    - JobUpdateRequest
- response/
    - AuthResponse
    - JobResponse
    - ApplicationResponse
    - ErrorResponse

## Rules
- Entities must never leave backend
- Controllers accept request DTOs only
- Controllers return response DTOs only
- Validation annotations required
- User identity comes from JWT only

## Outcome
- API contract becomes stable
- Frontend integration becomes predictable

Status: ✅ Completed
