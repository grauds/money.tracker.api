package org.clematis.mt.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.clematis.mt.model.NamedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class NameFilteringRepository<T extends NamedEntity, ID extends Serializable>
    extends SimpleJpaRepository<T, ID>
    implements PagingAndSortingAndFilteringByNameRepository<T, ID> {

    public NameFilteringRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Override
    public Page<T> findAll(Pageable pageable) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            String nameParam = attributes.getRequest().getParameter("name");

            // If the frontend passed ?name=, automatically apply case-insensitive 'contains' matching
            if (nameParam != null && !nameParam.trim().isEmpty()) {
                Specification<T> nameSpec = (root, query, cb) -> cb.like(
                    cb.lower(root.get("name")),
                    "%" + nameParam.toLowerCase() + "%"
                );
                return super.findAll(nameSpec, pageable);
            }
        }

        // Default behavior if no ?name= parameter is provided
        return super.findAll(pageable);
    }
}
