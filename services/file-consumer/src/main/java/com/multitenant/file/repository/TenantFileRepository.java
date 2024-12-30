package com.multitenant.file.repository;

import com.multitenant.file.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantFileRepository extends JpaRepository<FileData, Long> {

    public Optional<FileData> findByDeviceId (Integer deviceId);
}
