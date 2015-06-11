package com.loki.sdk;

import com.loki.sdk.ILokiService;

interface ILokiClient {
    void publishService(int version, ILokiService service);
}