package com.example.thumb.hot.service;

import com.example.thumb.hot.vo.AddResult;
import com.example.thumb.hot.vo.Item;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public interface TopK {
    AddResult add(String key, int increment);
    List<Item> list();
    BlockingQueue<Item> expelled();
    void fading();
    long total();
}
