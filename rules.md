# CLAUDE.md — Personal Global Java Contract (Spring Boot)

This document defines mandatory development rules for all Java/Spring Boot work.

These rules override default AI behavior. Violation of these constraints means the change will be reverted.

---

## 1. WORK SCOPE RULES

- Complete ALL methods and classes explicitly requested in the prompt.
- NEVER modify existing completed methods without explicit approval.

### If modification is required:
1. Explain why.
2. Show the impact.
3. Wait for approval.

No silent edits.

---

## 2. SPRING BOOT ARCHITECTURE RULES

Strict layered architecture:

**Controller → Service → Repository**

### Rules:
- Controllers handle request/response only.
- Business logic belongs ONLY in Service layer.
- Repository layer handles database interaction only.
- No database calls from Controllers.
- No business logic in Controllers.
- No direct Entity exposure in Controllers (use DTOs).

Prefer constructor injection. No field injection.

---

## 3. NEW CLASSES / METHODS

### Before creating:
- New class
- New interface
- New helper
- New utility
- New configuration

### You MUST:
1. Inform me.
2. Explain its responsibility.
3. Explain why existing code cannot be reused.
4. Explain where it will be used.

Unauthorized additions will be reverted.

---

## 4. DEPENDENCIES

- Never add new dependencies without approval.

### Before suggesting one:
- Explain what it does.
- Explain why it is required.
- Explain why current stack cannot solve it.
- Explain potential security impact.

---

## 5. SOLID & DESIGN

### Mandatory:
- Single Responsibility Principle
- Open/Closed Principle
- Interface Segregation
- Dependency Inversion

### Prefer:
- Constructor injection
- Interfaces for Services
- Immutable objects where possible
- Stateless services

### Avoid:
- God classes
- Utility dumping grounds
- Static abuse
- Circular dependencies

---

## 6. PERFORMANCE CONSTRAINTS

- Time Complexity ≤ O(n)
- Space Complexity ≤ O(n)

### If not achievable:
- Clearly explain why.
- Provide alternatives.
- Justify trade-offs.

### Avoid:
- Nested loops without justification
- N+1 query problems
- Inefficient stream chaining

### Use:
- Proper indexing awareness
- Batch operations when needed

---

## 7. CLASS & METHOD SIZE LIMITS

- Class size ≤ 200 lines
- Cognitive Complexity ≤ 15
- Methods should be short and focused
- Prefer early returns over deep nesting
- Avoid more than 3 levels of nesting

Must be SonarQube compliant.

---

## 8. LOGGING STANDARDS

### Use:
- info
- warn
- error

### Rarely:
- debug

### Rules:
- Never log inside loops.
- Never log sensitive data.
- Log meaningful contextual data.
- Log failures with clear error cause.
- Use parameterized logging (avoid string concatenation).

**Example:**
```java
log.info("User {} created successfully", userId);

---

## 9. CONSTANTS & CLEAN CODE

- Repeated String > 2 times → extract to constant.
- No magic numbers.
- Descriptive variable names.
- Avoid overly long methods.
- Avoid unnecessary abstraction.
- Keep code readable and maintainable.

---

## 10. SPRING SECURITY & VALIDATION

### Mandatory:
- Validate all request inputs.
- Use `@Valid` where applicable.
- Never trust client input.
- Avoid exposing internal exceptions directly.
- Handle authentication and authorization properly.
- No hardcoded secrets.
- No insecure deserialization.
- Avoid exposing stack traces in APIs.

### Check for:
- SQL Injection
- NullPointerExceptions
- Broken access control
- Mass assignment vulnerabilities

---

## 11. DATABASE RULES (JPA / Hibernate)

- Avoid EAGER fetching unless required.
- Prevent N+1 issues.
- Use DTO projection when needed.
- Never expose Entities directly in API.
- Validate transactional boundaries.
- Use `@Transactional` appropriately.

### Never:
- Modify schema without explaining impact.
- Add queries without explaining performance implication.

---

## 12. ERROR HANDLING

- Use meaningful custom exceptions when needed.
- Centralized exception handling (`@ControllerAdvice` preferred).
- Do not swallow exceptions.
- Provide actionable error messages internally.
- Avoid leaking internal details externally.

---

## 13. OUTPUT CONTRACT (MANDATORY)

After completing any task, ALWAYS provide:

1. Summary of changes made  
2. Why those changes were necessary  
3. Time & Space complexity analysis  
4. Security review confirmation  
5. Performance considerations  
6. Any assumptions made  
7. Potential risks  

Do not skip this section.

---

## 14. SECURITY REVIEW (MANDATORY)

After implementation, explicitly confirm:

- Input validation checked  
- No injection risk  
- No sensitive data exposed  
- Proper authentication/authorization respected  
- Logging is safe  
- No performance regression  
- No Sonar violations  

---

## 15. NON-NEGOTIABLES

- No modifying completed code without approval  
- No new dependencies without approval  
- No security shortcuts  
- No performance degradation without justification  
- No architecture violations  

If in doubt → Ask before implementing.

---

## 16. REVIEW

- Always run a sub-agent review like a senior engineer.
- Identify issues, risks, and improvements.

### Apply fixes ONLY AFTER:
1. Explaining the issue  
2. Explaining the fix  
3. Explaining why the fix works  

---
