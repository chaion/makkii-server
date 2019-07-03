package com.chaion.makkiiserver.repository;

import com.chaion.makkiiserver.model.EthToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EthTokenRepository extends MongoRepository<EthToken, String> {
    @Query("{'$or': [{'name': {$regex: '?0', $options: 'i'}}, {'symbol': {$regex: '?0', $options: 'i'}}]}")
    List<EthToken> findByName(String name, Pageable pageable);

    @Query("{'contractAddress': '?0'}")
    List<EthToken> findByContractAddress(String address, Pageable pageable);
}