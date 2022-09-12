package com.fosss.eduservice.client.impl;

import com.fosss.eduservice.client.OrderClient;
import org.springframework.stereotype.Component;

@Component
public class OrderClientImpl implements OrderClient {
    @Override
    public boolean isBought(String courseId, String memberId) {
        return false;
    }
}
