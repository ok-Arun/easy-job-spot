# Phase 07 — Security Core

## Objective
Enforce authentication and role-based authorization.

## Features
- JWT-based authentication
- USER / ADMIN roles
- Method-level authorization
- Stateless session policy

## Rules
- No role checks inside controllers
- No session-based authentication
- No entity exposure

## Validation
- 401 for unauthenticated access
- 403 for unauthorized access

Status: ✅ Completed
