package io.github.d4isdavid.educhat.api.enums

enum class ApiError(val code: Int) {
    Generic(0),

    UnknownUser(1001),
    UnknownCategory(1002),
    UnknownPost(1003),
    UnknownPostReply(1004),

    InvalidObject(2001),
    BadUsernameFormat(2002),
    BadEmailFormat(2003),
    BadPasswordFormat(2004),
    InvalidAuthorization(2005),
    InvalidUsername(2006),
    InvalidPassword(2007),
    InvalidCategoryName(2008),
    InvalidPostTitle(2009),

    NotStudentOrTeacher(3001),
    UsernameUnavailable(3002),
    EmailTaken(3003),
    BadUsernameLength(3004),
    BadPasswordLength(3005),
    NewPasswordIsCurrent(3006),
    BadCategoryNameLength(3007),
    BadCategoryDescriptionLength(3008),
    BadPostTitleLength(3009),
    BadMessageContentLength(3010),

    NoPermission(4001),
    CategoryLocked(4002),
    PostLocked(4003),
}
