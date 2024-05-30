/* campaign executor that manages and executes outreach campaigns */
package org.example.services;

import lombok.Getter;
import lombok.Setter;
import org.example.model.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Getter
@Setter
public class CampaignExecutor {

    private Map<Long, Campaign> activeCampaigns;
    private ScheduledExecutorService executorService;
    private final Long senderId = 12345L; // TODO would use - GET /user/me → User

    public CampaignExecutor() {
        this.activeCampaigns = new ConcurrentHashMap<>();
        this.executorService = Executors.newScheduledThreadPool(10);
    }

    @Scheduled(fixedRate = 1000)
    public void sendCurrentCampaigns() {

        // TODO notificationService
    }


    public String startCampaign(String roleId, Long candidateId, CampaignDefinition campaignDefinition) {
        if (activeCampaigns.containsKey(candidateId)) {
            throw new IllegalStateException("A campaign is already running for this candidate.");
        }

        String campaignId = UUID.randomUUID().toString();
        Campaign campaign = new Campaign(campaignId, roleId, candidateId, campaignDefinition, false);
        activeCampaigns.put(candidateId, campaign);

        scheduleCampaign(campaign, candidateId);

        // Campaigns end automatically after 30 days or upon candidate response.
        executorService.schedule(() -> stopCampaignIfNoResponse(campaign), 30, TimeUnit.DAYS);

        return campaignId;
    }

    private void scheduleCampaign(Campaign campaign, Long candidateId) {
        List<Node> steps = campaign.getCampaignDefinition().getNodes();
        long cumulativeDelay = 0;

        for (Node node : steps) {
            if (node.getType().equals(CampaignType.DELAY_STEP)) {
                cumulativeDelay += (node.getDelaySeconds());
            } else if (node.getType().equals(CampaignType.MESSAGE)) {
                Random random = new Random();
                Message message = new Message(random.nextLong(), senderId, candidateId, node.getSubjectLine(), node.getMessage(), null, null);
                executorService.schedule(() -> sendMessage(campaign, message), cumulativeDelay, TimeUnit.SECONDS);
            }
        }
    }

    private void sendMessage(Campaign campaign, Message message) {
        System.out.println("Sending message to candidate " + campaign.getCandidateId() + " from " + message.getFrom() + " to " + message.getTo() + " with subject " + message.getSubject() + ": " + message.getBody());

        // TODO would call NotificationService to trigger message

        message.setSentAt(new Date()); // set sent time

        if (checkForResponse(campaign)) {
            // TODO would recive response back from NotificationService (plus ChannelStatus)
            stopCampaign(campaign.getCandidateId());
        }
    }

    private boolean checkForResponse(Campaign campaign) {
        return campaign.isResponded();
        // TODO would call NotificationService to check for response
    }

    public void stopCampaignIfNoResponse(Campaign campaign) {
        if (!campaign.isResponded()) {
            stopCampaign(campaign.getCandidateId());
        }
    }

    public void stopCampaign(Long candidateId) {
        Campaign campaign = activeCampaigns.remove(candidateId);
        if (campaign != null) {
            campaign.setResponded(true);
            System.out.println("Campaign stopped for candidate " + candidateId);
        }
    }

    public boolean isCampaignRunning(Long campaignId) {
        return activeCampaigns.containsKey(campaignId);
    }

    public Campaign getActiveCampaign(Long campaignId) {
        return activeCampaigns.get(campaignId);
    }

}
