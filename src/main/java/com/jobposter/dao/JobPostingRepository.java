package com.jobposter.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.jobposter.entity.JobPosting;

@Repository
public interface JobPostingRepository extends PagingAndSortingRepository<JobPosting, Long> {

}
