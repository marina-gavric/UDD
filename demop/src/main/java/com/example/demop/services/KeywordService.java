package com.example.demop.services;

import com.example.demop.model.Keyword;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KeywordService {
    public List<Keyword> getAll();
    public Keyword save(Keyword keyword);
    public Keyword findKeywordById(long id);
    public Keyword findKeywordByName(String name);

}
