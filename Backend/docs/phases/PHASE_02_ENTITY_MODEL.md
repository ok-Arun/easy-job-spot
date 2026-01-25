# Phase 02 — Entity Model (Database Truth)

## Objective
Define entities that represent database structure only.

## Core Principle
Entities represent persistence, not API design.

## Entities

### User
- id (UUID)
- name
- email (unique)
- password
- role
- createdAt

### Job
- id (UUID)
- title
- company
- category
- location
- jobType
- description
- createdAt

### Application
- id (UUID)
- user (ManyToOne)
- job (ManyToOne)
- appliedAt
- status

## Rules
- UUID as primary key
- EnumType.STRING for enums
- No Lombok @Data
- No bidirectional JSON
- No entity exposure in controllers

## Validation
- Tables auto-created
- Foreign keys verified
- No infinite JSON serialization

Status: ✅ Completed
