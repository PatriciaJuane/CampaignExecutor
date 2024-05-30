package org.example.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;
import java.util.concurrent.*;

class CampaignExecutorTest {

    @InjectMocks
    private CampaignExecutor campaignExecutor;

    @Mock
    private ScheduledExecutorService executorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        campaignExecutor = new CampaignExecutor();
        campaignExecutor.setExecutorService(executorService);
    }
    // These tests cover the basic functionality of starting, scheduling, and stopping campaigns.

    private CampaignDefinition createCampaignDefinition() {
        // A helper method to create a CampaignDefinition object with a few steps for testing.
        CampaignDefinition campaignDefinition = new CampaignDefinition();
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(1L, CampaignType.MESSAGE, ChannelType.EMAIL, "Hi Milan, \nI'm interested in your background as Senior Software Engineer at Showpad. I'm looking for people like you for our role of Software Engineer with Startup Experience. \nWould love to connect.", "Subject Example", null));
        nodes.add(new Node (2L, CampaignType.DELAY_STEP, null, null, null, 172800L));
        nodes.add(new Node(3L, CampaignType.MESSAGE, ChannelType.EMAIL, "Hi Milan,\nI wanted to follow up on my previous email regarding the Software Engineer position at our company. We're eager to find a candidate with your startup experience and believe you could be a great fit. Could we arrange a short call to discuss this further?\n\n\nBest,Andreas", "Subject Example 2", null));
        campaignDefinition.setNodes(nodes);
        return campaignDefinition;
    }

    @Test
    void testStartCampaign() {
        // Test that starting a campaign works and a campaign ID is returned.
        CampaignDefinition campaignDefinition = createCampaignDefinition();
        String campaignId = campaignExecutor.startCampaign("role1", 1L, campaignDefinition);

        assertNotNull(campaignId);
        assertTrue(campaignExecutor.isCampaignRunning(1L));
    }

    @Test
    void testScheduleCampaign() {
        // verify that the messages are scheduled correctly.
        CampaignDefinition campaignDefinition = createCampaignDefinition();
        String campaignId = campaignExecutor.startCampaign("role1", 1L, campaignDefinition);

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(executorService, times(2)).schedule(runnableCaptor.capture(), anyLong(), eq(TimeUnit.SECONDS));

        List<Runnable> scheduledTasks = runnableCaptor.getAllValues();
        assertEquals(2, scheduledTasks.size());
    }

    @Test
    void testStopCampaign() {
        // test that stopping a campaign works and the campaign is no longer running.
        CampaignDefinition campaignDefinition = createCampaignDefinition();
        String campaignId = campaignExecutor.startCampaign("role1", 1L, campaignDefinition);

        campaignExecutor.stopCampaign(1L);

        assertFalse(campaignExecutor.isCampaignRunning(1L));
    }

    @Test
    void testStopCampaignIfNoResponse() throws InterruptedException {
        // simulate 30 days passing to check if the campaign stops as expected without a response.
        CampaignDefinition campaignDefinition = createCampaignDefinition();
        String campaignId = campaignExecutor.startCampaign("role1", 1L, campaignDefinition);

        // simulate 30 days passing
        Runnable stopCampaignTask = () -> campaignExecutor.stopCampaignIfNoResponse(campaignExecutor.getActiveCampaign(1L));
        stopCampaignTask.run();

        assertFalse(campaignExecutor.isCampaignRunning(1L));
    }

}
