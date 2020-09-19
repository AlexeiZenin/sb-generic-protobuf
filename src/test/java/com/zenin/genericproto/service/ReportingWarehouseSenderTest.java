package com.zenin.genericproto.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportingWarehouseSenderTest {

    private ReportingWarehouseSender warehouseSender;

    @BeforeEach
    public void setup() {
        warehouseSender = new ReportingWarehouseSender();
    }

    @Test
    void sendToWarehouse() {
        warehouseSender.sendToWarehouse("{\"test\":123}");
    }
}