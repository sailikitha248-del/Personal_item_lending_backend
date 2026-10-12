
package com.example.personal_item_lending.repository;

import com.example.personal_item_lending.entity.ReturnRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnRepository extends JpaRepository<ReturnRecord, Long> {
}
