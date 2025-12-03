# spring transaction propagation lab

[🇮🇷  فارسی ](./README.fa.md)


A Spring Framework project demonstrating advanced transaction management using @Transactional propagation behaviors (REQUIRED, REQUIRES_NEW, MANDATORY, SUPPORTS, NEVER, NESTED).
The project includes a Person–Car relational model (OneToMany / ManyToOne) with Cascade operations, Lazy Loading handling, and proper JOIN FETCH usage to avoid LazyInitializationException.

✔ Features:
- Spring @Transactional Propagation:
- REQUIRED
- REQUIRES_NEW
- MANDATORY
- SUPPORTS
- NEVER
- NESTED
- Entity relationship: Person (OneToMany) → Car
- CascadeType.ALL for automatic persistence of child entities
- Handling LazyInitializationException using JOIN FETCH
- Custom Repository implementation (EntityManager + JPQL)
- Real examples: audit logging, helper services, nested transactions
- Clean and readable architecture (Service, Repository, Entity layers)

---
