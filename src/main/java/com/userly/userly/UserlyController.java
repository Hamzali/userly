package com.userly.userly;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.userly.userly.enitities.User;
import com.userly.userly.repositories.UserRepository;
import com.userly.userly.repositories.UserViewRepository;
import com.userly.userly.enitities.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserlyController {

    @Autowired
    private UserViewRepository userViewRepository;
    @Autowired
    private UserRepository userRepository;

    // utilities
    private int getPageSize() {
        return 20;
    }

    private long getExpireDate() {
        return ZonedDateTime.now().minusSeconds(30).toInstant().toEpochMilli();
    }

    private long getNow() {
        return ZonedDateTime.now().toInstant().toEpochMilli();
    }

    // cache simulation for tracking the oldest user view.
    private Optional<UserView> oldestUserView = Optional.empty();

    @Async
    protected void updateOldestViewAndClear() {
        if (oldestUserView.isEmpty()) {
            oldestUserView = userViewRepository.findFirstByOrderByIdAsc();
        }

        long expireDate = getExpireDate();
        if (oldestUserView.isPresent() && oldestUserView.get().getViewedAt() < expireDate) {
            userViewRepository.deleteAllByViewedAtLessThanEqual(expireDate);
            oldestUserView = userViewRepository.findFirstByOrderByIdAsc();
        }
    }

    @GetMapping("/views/{userId}")
    public List<UserView> userViews(@PathVariable long userId, @RequestParam(value = "page", defaultValue = "0") int page) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid user id provided");
        }

        Pageable paging = PageRequest.of(page, getPageSize());

        return userViewRepository.findAllByViewedIdEqualsAndViewedAtGreaterThanOrderByViewedAtDesc(
                paging,
                userId,
                getExpireDate()
        );
    }

    @GetMapping("/view/{userId}")
    public User viewUser(@RequestHeader("X-User-Id") long viewerId, @PathVariable long userId) throws InterruptedException {
        Optional<User> viewedUser = userRepository.findById(userId);

        if (viewedUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid user id provided");
        }

        if (!userRepository.existsById(viewerId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid viewer id provided");
        }

        CompletableFuture.runAsync(this::updateOldestViewAndClear);

        userViewRepository.save(
                new UserView(
                        viewerId,
                        userId,
                        getNow()
                )
        );

        return viewedUser.get();
    }
}