package org.example.services;


import org.example.models.responses.DeleteSuspiciousIPResponse;
import org.example.models.responses.SuspiciousIPResponse;

import java.util.List;

public interface IPService {
    SuspiciousIPResponse addSuspiciousIP(String ip);

    boolean ipIsBlacklisted(String ip);

    DeleteSuspiciousIPResponse delete(String ip);

    List<SuspiciousIPResponse> getAll();
}
