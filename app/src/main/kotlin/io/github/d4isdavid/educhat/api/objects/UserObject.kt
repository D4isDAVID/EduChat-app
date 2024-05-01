package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.utils.getInstant
import org.json.JSONObject
import java.time.Instant
import kotlin.properties.Delegates

class UserObject : APIObject() {

    var id by Delegates.notNull<Int>()
        private set
    lateinit var name: String
        private set
    lateinit var createdAt: Instant
        private set
    var admin by Delegates.notNull<Boolean>()
        private set
    var student by Delegates.notNull<Boolean>()
        private set
    var teacher by Delegates.notNull<Boolean>()
        private set

    override fun getKey() = id

    override fun update(obj: JSONObject) {
        id = obj.getInt("id")
        name = obj.getString("name")
        createdAt = obj.getInstant("createdAt")
        admin = obj.getBoolean("admin")
        student = obj.getBoolean("student")
        teacher = obj.getBoolean("teacher")
    }

}
