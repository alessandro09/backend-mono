package br.com.alessandro.backend.registry.datasource.customer;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alessandro.backend.registry.datasource.customer.model.CustomerMasterModel;
import br.com.alessandro.backend.registry.entities.enums.ClientStatusType;
import jakarta.transaction.Transactional;

@Repository
public interface CustomerRepositoryJpa extends JpaRepository<CustomerMasterModel, Long> {
    Optional<CustomerMasterModel> findByTaxId(String taxId);

    boolean existsByTaxId(String taxId);

    @Query(SERCH_BY_NAME_QUERY)
    Page<CustomerMasterModel> search(
        @Param("searchTerm") String searchTerm,
        @Param("status") ClientStatusType status,
        Pageable pageable
    );

    @Transactional
    @Modifying
    @Query("UPDATE CustomerMasterModel u SET u.status = :status WHERE u.id = :id")
    int updateStatusById(@Param("id") Long id, @Param("status") ClientStatusType status);

    String SERCH_BY_NAME_QUERY = """
        SELECT c
          FROM CustomerMasterModel c
         WHERE (LOWER(c.legalName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.tradeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
           AND (:status is null OR c.status = :status)
    """;
}
