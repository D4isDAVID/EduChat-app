package io.github.d4isdavid.educhat.api.enums

import android.content.Context
import io.github.d4isdavid.educhat.R

enum class NotificationType(val num: Int) {
    NewPostReply(0),
    NewMessageVote(1),
    HelperGranted(2),
    HelperRevoked(3),
    AdminGranted(4),
    AdminRevoked(5);

    fun getMessage(context: Context) = when (this) {
        NewPostReply -> context.getString(R.string.new_post_reply)
        NewMessageVote -> context.getString(R.string.new_message_vote)
        HelperGranted -> context.getString(R.string.helper_granted)
        HelperRevoked -> context.getString(R.string.helper_revoked)
        AdminGranted -> context.getString(R.string.admin_granted)
        AdminRevoked -> context.getString(R.string.admin_revoked)
    }

    fun getDescription(context: Context, user: String) = when (this) {
        NewPostReply -> context.getString(R.string.new_post_reply_description, user)
        NewMessageVote -> context.getString(R.string.new_message_vote_description, user)
        HelperGranted -> context.getString(R.string.helper_granted_description, user)
        HelperRevoked -> context.getString(R.string.helper_revoked_description, user)
        AdminGranted -> context.getString(R.string.admin_granted_description, user)
        AdminRevoked -> context.getString(R.string.admin_revoked_description, user)
    }

    companion object {
        fun from(num: Int) = NotificationType.entries.first { it.num == num }
    }
}
