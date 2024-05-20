package io.github.d4isdavid.educhat.api.enums

import android.content.Context
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.http.request.HttpStatusCode

enum class APIError(val code: Int) {
    GENERIC(0),

    UNKNOWN_USER(1001),
    UNKNOWN_CATEGORY(1002),
    UNKNOWN_POST(1003),
    UNKNOWN_POST_REPLY(1004),

    INVALID_OBJECT(2001),
    BAD_USERNAME_FORMAT(2002),
    BAD_EMAIL_FORMAT(2003),
    BAD_PASSWORD_FORMAT(2004),
    INVALID_AUTHORIZATION(2005),
    INVALID_EMAIL(2006),
    INVALID_PASSWORD(2007),
    INVALID_CATEGORY_NAME(2008),
    INVALID_POST_TITLE(2009),

    NOT_STUDENT_OR_TEACHER(3001),
    USERNAME_UNAVAILABLE(3002),
    EMAIL_TAKEN(3003),
    BAD_USERNAME_LENGTH(3004),
    BAD_PASSWORD_LENGTH(3005),
    NEW_PASSWORD_IS_CURRENT(3006),
    BAD_CATEGORY_NAME_LENGTH(3007),
    BAD_CATEGORY_DESCRIPTION_LENGTH(3008),
    BAD_POST_TITLE_LENGTH(3009),
    BAD_MESSAGE_CONTENT_LENGTH(3010),

    NO_PERMISSION(4001),
    CATEGORY_LOCKED(4002),
    POST_LOCKED(4003);

    fun getMessage(context: Context, status: HttpStatusCode) = when (this) {
        GENERIC -> status.getMessage(context)
        UNKNOWN_USER -> context.resources.getString(R.string.api_error_unknown_user)
        UNKNOWN_CATEGORY -> context.resources.getString(R.string.api_error_unknown_category)
        UNKNOWN_POST -> context.resources.getString(R.string.api_error_unknown_post)
        UNKNOWN_POST_REPLY -> context.resources.getString(R.string.api_error_unknown_post_reply)
        INVALID_OBJECT -> context.resources.getString(R.string.api_error_invalid_object)
        BAD_USERNAME_FORMAT -> context.resources.getString(R.string.api_error_bad_username_format)
        BAD_EMAIL_FORMAT -> context.resources.getString(R.string.api_error_bad_email_format)
        BAD_PASSWORD_FORMAT -> context.resources.getString(R.string.api_error_bad_password_format)
        INVALID_AUTHORIZATION -> context.resources.getString(R.string.api_error_invalid_authorization)
        INVALID_EMAIL -> context.resources.getString(R.string.api_error_invalid_email)
        INVALID_PASSWORD -> context.resources.getString(R.string.api_error_invalid_password)
        INVALID_CATEGORY_NAME -> context.resources.getString(R.string.api_error_invalid_category_name)
        INVALID_POST_TITLE -> context.resources.getString(R.string.api_error_invalid_post_title)
        NOT_STUDENT_OR_TEACHER -> context.resources.getString(R.string.api_error_not_student_or_teacher)
        USERNAME_UNAVAILABLE -> context.resources.getString(R.string.api_error_username_unavailable)
        EMAIL_TAKEN -> context.resources.getString(R.string.api_error_email_taken)
        BAD_USERNAME_LENGTH -> context.resources.getString(R.string.api_error_bad_username_length)
        BAD_PASSWORD_LENGTH -> context.resources.getString(R.string.api_error_bad_password_length)
        NEW_PASSWORD_IS_CURRENT -> context.resources.getString(R.string.api_error_new_password_is_current)
        BAD_CATEGORY_NAME_LENGTH -> context.resources.getString(R.string.api_error_bad_category_name_length)
        BAD_CATEGORY_DESCRIPTION_LENGTH -> context.resources.getString(R.string.api_error_bad_category_description_length)
        BAD_POST_TITLE_LENGTH -> context.resources.getString(R.string.api_error_bad_post_title_length)
        BAD_MESSAGE_CONTENT_LENGTH -> context.resources.getString(R.string.api_error_bad_message_content_length)
        NO_PERMISSION -> context.resources.getString(R.string.api_error_no_permission)
        CATEGORY_LOCKED -> context.resources.getString(R.string.api_error_category_locked)
        POST_LOCKED -> context.resources.getString(R.string.api_error_post_locked)
    }

    companion object {
        fun from(code: Int) = entries.first { it.code == code }
    }
}
