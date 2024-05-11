package io.github.d4isdavid.educhat.api.utils

import io.github.d4isdavid.educhat.BuildConfig
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.objects.PostObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.http.rest.RestClient
import kotlinx.coroutines.CoroutineScope
import org.json.JSONArray
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
    authorId: Int = 1,
    authorName: String = "MockUser",
    authorCreatedAt: Instant = Instant.now(),
    authorAdmin: Boolean = true,
    authorStudent: Boolean = true,
    authorTeacher: Boolean = true,
    reactions: JSONArray = JSONArray(),
): JSONObject {
    val author =
        mockUser(authorId, authorName, authorCreatedAt, authorAdmin, authorStudent, authorTeacher)
    val message = JSONObject()
        .put("id", id)
        .put("content", content)
        .put("createdAt", createdAt.toString())
        .put("editedAt", editedAt?.toString() ?: JSONObject.NULL)
        .put("pinned", pinned)
        .put("hidden", hidden)
        .put("author", author)
        .put("reactions", reactions)
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
    messageId: Int = 1,
    messageContent: String = "Hello, world!",
    messageCreatedAt: Instant = Instant.now(),
    messageEditedAt: Instant? = null,
    messagePinned: Boolean = true,
    messageHidden: Boolean = false,
    authorId: Int = 1,
    authorName: String = "MockUser",
    authorCreatedAt: Instant = Instant.now(),
    authorAdmin: Boolean = true,
    authorStudent: Boolean = true,
    authorTeacher: Boolean = true,
    messageReactions: JSONArray = JSONArray(),
    title: String = "Mock Post",
    locked: Boolean = true,
    question: Boolean = false,
    answerId: Int? = null,
    categoryId: Int = 1,
): JSONObject {
    val message = mockMessage(
        messageId,
        messageContent,
        messageCreatedAt,
        messageEditedAt,
        messagePinned,
        messageHidden,
        authorId,
        authorName,
        authorCreatedAt,
        authorAdmin,
        authorStudent,
        authorTeacher,
        messageReactions,
    )
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
    student: Boolean = true,
    teacher: Boolean = true,
): JSONObject {
    val user = JSONObject()
        .put("id", id)
        .put("name", name)
        .put("createdAt", createdAt.toString())
        .put("admin", admin)
        .put("student", student)
        .put("teacher", teacher)
    UserObject.getKey(user)
    users.cache.put(user, UserObject::getKey, ::UserObject)
    return user
}

fun createMockClient(scope: CoroutineScope, func: APIClient.() -> Unit): APIClient {
    val api = APIClient(RestClient(BuildConfig.API_BASE_URL, scope))
    api.func()
    return api
}
