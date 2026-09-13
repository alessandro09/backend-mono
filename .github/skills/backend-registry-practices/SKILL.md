---
name: backend-registry-practices
description: "Use for every backend change in backend-mono, especially Java, Spring Boot, domain entities, HTTP DTOs, repositories, JPA models, mappers, services, validation, and tests. Follow the registry module practices as the project's default architecture."
---

# Backend Registry Practices

Use this skill for all backend work in `backend-mono`. The `registry` module is the reference implementation for organizing business rules, HTTP contracts, persistence, and translations between those boundaries.

## Architecture

Keep the following boundaries explicit:

- `entities/`: domain objects and aggregate behavior. Keep business relationships and invariants here, without JPA or HTTP concerns.
- `repository/`: domain-facing repository contracts. Expose domain entities and business-oriented operations, not persistence models.
- `datasource/`: persistence adapters and JPA-specific models. Keep `*Model` classes, Spring Data repositories, and database concerns here.
- `entrypoint/http/`: controllers and HTTP contracts. Keep request/response DTOs and HTTP mappers here; do not expose JPA models.
- `iteractors/`: application use cases/services. Coordinate repositories and domain operations without leaking datasource details.
- `entities/enums/`: shared domain enumerations. Reuse these types across layers instead of duplicating string constants.

When a change does not fit one boundary, stop and identify the owning abstraction before adding code. Do not move persistence annotations, request DTO fields, or transport concerns into domain entities merely to reduce mapping work.

## Domain Aggregates

- Let the aggregate root control relationships with methods such as `addAddress`, `addContact`, `removeAddress`, `removeContact`, and `setFinancials`.
- Whenever a child is added, removed, or replaced, keep both sides of the association synchronized.
- Prefer aggregate methods over directly replacing relationship collections or setting the child back-reference from an adapter.
- Preserve collection initialization in aggregate roots so callers can safely read empty relationships.
- Keep `equals` and `hashCode` consistent with the entity identity strategy already used by the module.
- Do not copy lifecycle timestamps from an incoming request. Creation and update timestamps are controlled by the domain or persistence lifecycle.

## Mapping

Follow the explicit mapper style used by `registry`:

- Keep conversions in a dedicated mapper close to the boundary being translated.
- Use small, explicit mapping methods for each root and child type. Avoid reflection and implicit field copying.
- Return `null` when the input object is `null`, unless the caller's contract explicitly requires another behavior.
- Copy identifiers when translating persisted objects, but do not invent identifiers for new objects.
- Map every supported field in both directions and update the mapper when a model or entity field changes.
- Map child collections through the child mapper, then attach them through the aggregate root's add method.
- Map one-to-one children through the aggregate root setter so the inverse reference is restored.
- Keep HTTP DTO mapping separate from datasource mapping. A DTO must not be passed directly to a repository adapter.
- Treat `null` collections according to the existing boundary contract; do not silently turn missing data into a different semantic value without documenting it.

Before finishing a mapper change, compare the source and target types field by field, including nested objects, IDs, enums, metadata, and optional values.

## Persistence

- Keep `@Entity`, `@Table`, relationship annotations, column definitions, and persistence callbacks in datasource models.
- Use `cascade = CascadeType.ALL` and `orphanRemoval = true` only when the child lifecycle belongs to the aggregate, as it does for the customer children.
- Keep database queries in Spring Data repository interfaces or the datasource adapter, not in domain entities or controllers.
- Translate datasource models back to domain entities before returning from a repository adapter.
- Preserve `Optional`, `Page`, and `Pageable` semantics at the repository boundary.
- For updates, load the existing persistence object when identity or lifecycle state must be retained, then apply the requested changes without replacing unrelated identity data.
- Do not expose persistence models in application services, repository contracts, controllers, or HTTP responses.

## HTTP API

- Use request and response DTOs as the public API contract.
- Keep request DTOs focused on client input and response DTOs focused on output; do not reuse one type for both directions when their contracts differ.
- Convert DTOs at the HTTP mapper boundary and pass domain entities to application services.
- Keep validation close to the request contract and return meaningful validation errors through the established Spring validation flow.
- Do not serialize domain or persistence implementation details that are not part of the API contract.
- Preserve enum types in the contract when the API already uses them; avoid ad hoc string conversion.

## Java and Spring Style

- Use the existing Java and Spring Boot versions and dependencies from `pom.xml`; do not introduce a library for a simple local mapping or utility.
- Prefer constructor injection for required dependencies.
- Keep classes focused and methods short enough that the boundary or business rule is obvious.
- Use descriptive names; avoid one-letter variables and unexplained abbreviations.
- Keep imports explicit and remove unused imports after changes.
- Add comments only for non-obvious business or persistence behavior.
- Do not add generated boilerplate, Lombok, MapStruct, or a new abstraction unless the project adopts it consistently and the change has a clear benefit.

## Testing and Validation

For every backend change:

1. Add or update focused tests for the changed behavior, especially mapper round trips, null handling, relationship back-references, repository updates, and validation.
2. Run the narrowest relevant test or compile check first.
3. Run `./mvnw test` when the change crosses layers or affects shared backend behavior.
4. Confirm that a mapped aggregate preserves child contents and parent references in both directions.
5. Check that API responses do not leak datasource models and that persistence adapters do not leak HTTP DTOs.

For mapper tests, cover at least:

- `null` input;
- root scalar fields;
- nested one-to-many and one-to-one fields;
- identifier preservation;
- empty and absent collections according to the contract;
- inverse relationship restoration after mapping.

## Change Checklist

Before completing a backend task, verify:

- The change is in the correct layer.
- Domain relationships are maintained through aggregate methods.
- All mapper fields are copied explicitly in both directions.
- No HTTP or JPA type crosses into another boundary unintentionally.
- Nullability, `Optional`, pagination, and validation behavior remain consistent.
- Focused tests or compilation have been run, and broader tests are run when the change warrants them.