package com.sha.microservicecoursemanagement.controller;

import com.sha.microservicecoursemanagement.client.UserClient;
import com.sha.microservicecoursemanagement.model.Transaction;
import com.sha.microservicecoursemanagement.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/service")
public class CourseController {

    @Autowired
    private UserClient userClient;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private Environment env;

    @Value("${spring.application.name}")
    private String serviceId;

    @GetMapping("/port")
    public String getPort() {
        return "Service is running on port : " + env.getProperty("local.server.port");
    }

    @GetMapping("/instances")
    public ResponseEntity<?> getInstances() {
        return ResponseEntity.ok(discoveryClient.getInstances(serviceId));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCourses() {
        return ResponseEntity.ok(courseService.allCourses());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserTransactions(@PathVariable Long userId) {
        return ResponseEntity.ok(courseService.findTransactionsOfUser(userId));
    }

    @PostMapping("/enroll")
    public ResponseEntity<?> enrollUser(@RequestParam Long courseId,
                                        @RequestParam Long userId) {

        Transaction transaction = courseService.enrollUser(courseId, userId);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getStudentsOfCourse(@PathVariable Long courseId) {

        List<Transaction> transactions =
                courseService.findTransactionsOfCourse(courseId);

        if (CollectionUtils.isEmpty(transactions)) {
            return ResponseEntity.notFound().build();
        }

        List<Long> userIds = transactions.stream()
                .map(Transaction::getUserId)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userClient.getUserNames(userIds));
    }
}
