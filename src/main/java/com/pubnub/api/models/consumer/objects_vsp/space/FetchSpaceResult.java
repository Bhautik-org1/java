package com.pubnub.api.models.consumer.objects_vsp.space;

import com.pubnub.api.models.server.objects_api.EntityEnvelope;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FetchSpaceResult extends SpaceResult {
    public FetchSpaceResult(EntityEnvelope<Space> entityEnvelope) {
        super(entityEnvelope);
    }
}
