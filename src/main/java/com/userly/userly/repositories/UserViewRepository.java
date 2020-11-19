package com.userly.userly.repositories;

import com.userly.userly.enitities.UserView;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserViewRepository extends PagingAndSortingRepository<UserView, Long> {
    List<UserView> findAllByViewedIdEqualsAndViewedAtGreaterThanOrderByViewedAtDesc(Pageable paging, long viewedId, long viewedAt);

    Optional<UserView> findFirstByOrderByIdAsc();

    void deleteAllByViewedAtLessThanEqual(long viewedAt);
}
