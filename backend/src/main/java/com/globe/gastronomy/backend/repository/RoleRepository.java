package com.globe.gastronomy.backend.repository;

import com.globe.gastronomy.backend.model.Role;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findRoleByRoleName(String roleName);

    @Query("update Role SET roleName =?2 where roleName=?1")
    @Transactional
    @Modifying
    //MODIFYING EKLEMEDIGIMIZDE QUERY NIN SELECT OLDUGUNU DUSUNUYOR -> MODIFYING UPDATE VE DELETE ISLEMLERI ICIN SART
    void updateRoleByRoleName(@NotNull String roleName,@NotNull String newRoleName);



}
