package io.github.d4isdavid.educhat.api.utils

import io.github.d4isdavid.educhat.BuildConfig
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.enums.NotificationType
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.objects.NotificationObject
import io.github.d4isdavid.educhat.api.objects.PostObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.http.rest.RestClient
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject
import java.time.Instant

fun APIClient.mockCategory(
    id: Int = 1,
    name: String = "Mock Category",
    description: String? = null,
    pinned: Boolean = true,
    locked: Boolean = true,
    parentId: Int? = null,
): JSONObject {
    val category = JSONObject()
        .put("id", id)
        .put("name", name)
        .put("description", description ?: JSONObject.NULL)
        .put("pinned", pinned)
        .put("locked", locked)
        .put("parentId", parentId ?: JSONObject.NULL)
    categories.cache.put(category, CategoryObject::getKey, ::CategoryObject)
    return category
}

fun APIClient.mockMessage(
    id: Int = 1,
    content: String = "Hello, world!",
    createdAt: Instant = Instant.now(),
    editedAt: Instant? = null,
    pinned: Boolean = true,
    hidden: Boolean = false,
    parentId: Int? = null,
    author: JSONObject = mockUser(),
    votes: JSONObject = createMockVoteCount(),
): JSONObject {
    val message = JSONObject()
        .put("id", id)
        .put("content", content)
        .put("createdAt", createdAt.toString())
        .put("editedAt", editedAt?.toString() ?: JSONObject.NULL)
        .put("pinned", pinned)
        .put("hidden", hidden)
        .put("parentId", parentId ?: JSONObject.NULL)
        .put("author", author)
        .put("votes", votes)
    messages.cache.put(
        message,
        MessageObject::getKey,
        ::MessageObject,
        UserObject::getKey,
        ::UserObject
    )
    return message
}

fun APIClient.mockPost(
    message: JSONObject = mockMessage(),
    title: String = "Mock Post",
    locked: Boolean = true,
    question: Boolean = false,
    answerId: Int? = null,
    categoryId: Int = 1,
): JSONObject {
    val post = JSONObject()
        .put("message", message)
        .put("title", title)
        .put("locked", locked)
        .put("question", question)
        .put("answerId", answerId ?: JSONObject.NULL)
        .put("categoryId", categoryId)
    posts.cache.put(
        post,
        PostObject::getKey,
        ::PostObject,
        MessageObject::getKey,
        ::MessageObject,
        UserObject::getKey,
        ::UserObject
    )
    return post
}

fun APIClient.mockUser(
    id: Int = 1,
    name: String = "MockUser",
    createdAt: Instant = Instant.now(),
    admin: Boolean = true,
    helper: Boolean = true,
    student: Boolean = true,
    teacher: Boolean = true,
): JSONObject {
    val user = JSONObject()
        .put("id", id)
        .put("name", name)
        .put("createdAt", createdAt.toString())
        .put("admin", admin)
        .put("helper", helper)
        .put("student", student)
        .put("teacher", teacher)
    users.cache.put(user, UserObject::getKey, ::UserObject)
    return user
}

fun createMockVoteCount(
    count: Int = 47,
    me: Boolean? = null,
): JSONObject {
    val reaction = JSONObject()
        .put("count", count)
        .put("me", me ?: JSONObject.NULL)
    return reaction
}

fun APIClient.mockNotification(
    id: Int = 1,
    type: NotificationType = NotificationType.NewPostReply,
    createdAt: Instant = Instant.now(),
    read: Boolean = false,
    user: JSONObject = mockUser(),
    post: JSONObject = mockPost(),
    message: JSONObject = mockMessage(id = 2),
): JSONObject {
    val notification = JSONObject()
        .put("id", id)
        .put("type", type.num)
        .put("createdAt", createdAt.toString())
        .put("read", read)
        .put("user", user)
        .put("post", post)
        .put("message", message)
    notifications.cache.put(
        notification,
        NotificationObject::getKey,
        ::NotificationObject,
        UserObject::getKey,
        ::UserObject,
        PostObject::getKey,
        ::PostObject,
        MessageObject::getKey,
        ::MessageObject,
    )
    return notification
}

fun createMockClient(scope: CoroutineScope, func: APIClient.() -> Unit): APIClient {
    val api = APIClient(RestClient(BuildConfig.API_BASE_URL, scope))
    func(api)
    return api
}
