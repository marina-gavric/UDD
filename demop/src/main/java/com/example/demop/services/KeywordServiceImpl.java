package com.example.demop.services;

import com.example.demop.model.Keyword;
import com.example.demop.repository.KeyWordRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class KeywordServiceImpl implements KeywordService{

    @Autowired
    private KeyWordRepository keywordRepository;

    @Override
    public List<Keyword> getAll() {
        return keywordRepository.findAll();
    }

    @Override
    public Keyword save(Keyword keyword) {
        return keywordRepository.save(keyword);
    }

    @Override
    public Keyword findKeywordById(long id) {
        return keywordRepository.findOneById(id);
    }

    @Override
    public Keyword findKeywordByName(String name) {
        return keywordRepository.findOneByName(name);
    }
}
