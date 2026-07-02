package org.globex.retail.ai.complaint.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.globex.retail.ai.complaint.agent.ComplaintAgent;

@ApplicationScoped
public class ComplaintService {

    @Inject
    ComplaintAgent complaintAgent;

    public String process(String userMessage, String memoryId) {
        return complaintAgent.process(userMessage, memoryId);
    }

}
