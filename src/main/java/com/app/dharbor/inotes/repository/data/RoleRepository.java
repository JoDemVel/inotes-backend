package com.app.dharbor.inotes.repository.data;

import com.app.dharbor.inotes.domain.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    List<RoleEntity> findRoleByRoleEnumIn(List<String> roles);
}
