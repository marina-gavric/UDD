package com.example.demop.services;

import com.example.demop.model.Text;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TextService {
    public List<Text> getAll();
    public Text save(Text text);
    public void deleteText(Text text);

}
