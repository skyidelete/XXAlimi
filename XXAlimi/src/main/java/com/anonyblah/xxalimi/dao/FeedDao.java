package com.anonyblah.xxalimi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anonyblah.xxalimi.vo.Feed;

/**
 * Feed Data Access Object, JPA Repository(Data의 유형, Data의 Primary Key의 유형)을 상속받음
 * @see Feed
 */
public interface FeedDao extends JpaRepository<Feed, String> {

}