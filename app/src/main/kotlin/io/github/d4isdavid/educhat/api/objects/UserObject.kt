package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.utils.getInstant
import org.json.JSONObject
import java.time.Instant
import kotlin.properties.Delegates

class UserObject() : APIObject() {

    companion object {
        @Suppress("unused")
        fun getKey(obj: JSONObject) = obj.getInt("id")
    }

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

    constructor(
        name: String,
        createdAt: Instant = Instant.now(),
        admin: Boolean = false,
        student: Boolean = true,
        teacher: Boolean = false,
    ) : this() {
        this.name = name
        this.createdAt = createdAt
        this.admin = admin
        this.student = student
        this.teacher = teacher
    }

    override fun update(obj: JSONObject) {
        id = obj.getInt("id")
        name = obj.getString("name")
        createdAt = obj.getInstant("createdAt")
        admin = obj.getBoolean("admin")
        student = obj.getBoolean("student")
        teacher = obj.getBoolean("teacher")
    }

}
