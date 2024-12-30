package com.multitenant.config.database;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SequenceHandler {

    private final EntityManager entityManager;

    public SequenceHandler(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * @param sequenceName String
     * @return ID as String , It can be parse any type of number (Long ,Integer)
     */

    @Transactional
    public Long generateId(String sequenceName) {
        String id = entityManager
                .createNativeQuery("select nextval('" + sequenceName + "')")
                .getSingleResult().toString();
        return Long.valueOf(id);
    }
}
