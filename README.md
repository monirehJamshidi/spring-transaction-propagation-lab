# spring transaction propagation lab

[🇮🇷 logicaldelete فارسی_درباره ](./README.fa.md)
[🇮🇷 recordversion فارسی_درباره ](./README_recordversion.fa.md)
[🇮🇷 pessimisticLocking فارسی_درباره ](./README_pessimisticLocking.fa.md)

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
It includes a basic Person entity and provides examples of how to:

- Configure Hibernate via persistence.xml

- Perform insert, update, delete, and select operations with EntityManager

- Use the @Version annotation for optimistic locking, preventing concurrent update conflicts

- Handle common Hibernate configuration issues such as JAXBException in modern Java versions

This project is intended as a learning example to compare plain JDBC implementation with Hibernate-based ORM.

## 🛠️ Technologies Used
- Java 8

- Hibernate ORM (JPA)

- H2 Database (in-memory)

