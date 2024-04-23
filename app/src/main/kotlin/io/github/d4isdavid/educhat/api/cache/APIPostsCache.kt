package io.github.d4isdavid.educhat.api.cache

import io.github.d4isdavid.educhat.api.objects.PostObject

open class APIPostsCache : APICache<PostObject>() {

    override fun put(obj: PostObject) {
        cache[obj.message.id] = obj
    }

}
