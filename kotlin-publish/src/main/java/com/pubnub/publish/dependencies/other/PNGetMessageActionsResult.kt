package com.pubnub.publish.dependencies.other

import com.google.gson.annotations.SerializedName
import com.pubnub.publish.dependencies.forPubNubObject.PNBoundedPage
import com.pubnub.publish.dependencies.models.PNMessageAction

/**
 * Result for the [PubNub.getMessageActions] API operation.
 *
 * @property actions List of message actions for a certain message in a certain channel.
 * @property page Information about next page. When null there's no next page.
 */
class PNGetMessageActionsResult internal constructor(
    @SerializedName("data")
    val actions: List<PNMessageAction>,
    @SerializedName("more")
    val page: PNBoundedPage?
)
