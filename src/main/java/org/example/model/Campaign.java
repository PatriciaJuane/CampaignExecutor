package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {

    private String campaignId;
    private String roleId;
    private Long candidateId;
    private CampaignDefinition campaignDefinition;
    private boolean responded;
}
