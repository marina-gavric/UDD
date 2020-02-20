package com.example.demop.services;

import com.example.demop.model.Text;
import com.example.demop.repository.TextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextServiceImpl implements TextService{
    @Autowired
    private TextRepository textRepository;

    @Override
    public List<Text> getAll() {
        return textRepository.findAll();
    }

    @Override
    public Text save(Text text) {
        return textRepository.save(text);
    }

    @Override
    public void deleteText(Text text) {
        textRepository.delete(text);
    }
}
