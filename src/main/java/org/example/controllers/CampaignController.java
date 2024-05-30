package org.example.controllers;

import org.example.model.CampaignDefinition;
import org.example.services.CampaignExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/* Once a candidate is identified as a good fit, our system will create a personalised campaign definition
* and submit it to following endpoint exposed by your system: Campaign Executor.
We don’t expect you to design other parts of the system that is not Campaign Executor */

@RestController
@RequestMapping("/role/{roleId}/candidate/{candidateId}/campaign")
public class CampaignController {

    @Autowired
    private CampaignExecutor campaignExecutor;

    // `PUT role/:roleId/candidate/:candidateId/campaign`
    //    - Accepts a campaign definition and starts the campaign immediately.
    //    There can only be 1 campaign running for a given candidate.
    //    - Returns a campaign ID.
    @PutMapping
    public String startCampaign(@PathVariable String roleId,
                                @PathVariable Long candidateId,
                                @RequestBody CampaignDefinition campaignDefinition) {
        // TODO validation logic for input data - Spring’s validation framework
        // TODO implement comprehensive error handling with appropriate HTTP status codes.
        return campaignExecutor.startCampaign(roleId, candidateId, campaignDefinition);
    }

    //You can assume everything that is not part of the system CampaignExecutor is available
    // for reference here are some endpoints that may be useful to model your solution:
    // - GET /user/me → User
    // - `GET /role/:roleId -> Role`
    //- `GET /role -> Role[]`
    // GET /role/:roleId/candidate → Candidate[]
    // GET /channel → Channel[]
    // POST /channel/:channelId/chat/:candidateId/message
    // GET /channel/:channelId/chat/:candidateId/message → Message[]

}
