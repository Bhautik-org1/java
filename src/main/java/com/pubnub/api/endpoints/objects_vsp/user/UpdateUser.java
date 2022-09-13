package com.pubnub.api.endpoints.objects_vsp.user;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.objects_api.CompositeParameterEnricher;
import com.pubnub.api.endpoints.objects_api.ObjectApiEndpoint;
import com.pubnub.api.endpoints.objects_api.utils.Include.HavingCustomInclude;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.objects_vsp.user.User;
import com.pubnub.api.models.server.objects_api.EntityEnvelope;
import com.pubnub.api.models.server.objects_vsp.user.UpdateUserPayload;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class UpdateUser extends ObjectApiEndpoint<EntityEnvelope<User>, User> implements HavingCustomInclude<UpdateUser> {
    @Setter
    private UserId userId;
    @Setter
    private String name;
    @Setter
    private String email;
    @Setter
    private String profileUrl;
    @Setter
    private String externalId;
    @Setter
    private Map<String, Object> custom;
    @Setter
    private String status;
    @Setter
    private String type;

    UpdateUser(
            final PubNub pubNub,
            final TelemetryManager telemetryManager,
            final RetrofitManager retrofitManager,
            final CompositeParameterEnricher compositeParameterEnricher,
            final TokenManager tokenManager) {
        super(pubNub, telemetryManager, retrofitManager, compositeParameterEnricher, tokenManager);
    }

    public static UpdateUser create(
            final PubNub pubNub,
            final TelemetryManager telemetryManager,
            final RetrofitManager retrofitManager,
            final TokenManager tokenManager) {
        final CompositeParameterEnricher compositeParameterEnricher = CompositeParameterEnricher.createDefault();
        return new UpdateUser(pubNub, telemetryManager, retrofitManager, compositeParameterEnricher, tokenManager);
    }

    @Override
    protected Call<EntityEnvelope<User>> executeCommand(Map<String, String> effectiveParams) throws PubNubException {
        //This is workaround to accept custom maps that are instances of anonymous classes not handled by gson
        final HashMap<String, Object> customHashMap = new HashMap<>();
        if (custom != null) {
            customHashMap.putAll(custom);
        }

        final UpdateUserPayload updateUserPayload = new UpdateUserPayload(name, email, externalId, profileUrl, customHashMap, status, type);

        String subscribeKey = getPubnub().getConfiguration().getSubscribeKey();
        return getRetrofit()
                .getUserService()
                .updateUser(subscribeKey, effectiveUserId().getValue(), updateUserPayload, effectiveParams);
    }

    @Override
    protected User createResponse(Response<EntityEnvelope<User>> input) throws PubNubException {
        if (input.body() != null) {
            return input.body().getData();
        } else {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_INTERNAL_ERROR).build();
        }
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNUpdateUserOperation;
    }

    @Override
    public CompositeParameterEnricher getCompositeParameterEnricher() {
        return super.getCompositeParameterEnricher();
    }

    private UserId effectiveUserId() {
        try {
            return (userId != null) ? userId : getPubnub().getConfiguration().getUserId();
        } catch (PubNubException e) {
            throw new RuntimeException(e);
        }
    }
}