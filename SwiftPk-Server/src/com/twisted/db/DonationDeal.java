package com.twisted.db;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;
@Data
public final class DonationDeal {
    public final AtomicInteger item;
    public final AtomicInteger threshold;
    public DonationDeal(AtomicInteger item, AtomicInteger threshold) {
        this.item = item;
        this.threshold = threshold;
    }
}
