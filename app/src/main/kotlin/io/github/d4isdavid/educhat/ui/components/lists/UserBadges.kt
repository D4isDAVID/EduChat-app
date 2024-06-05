package io.github.d4isdavid.educhat.ui.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockUser
import io.github.d4isdavid.educhat.ui.components.chips.AdminBadge
import io.github.d4isdavid.educhat.ui.components.chips.HelperBadge
import io.github.d4isdavid.educhat.ui.components.chips.StudentBadge
import io.github.d4isdavid.educhat.ui.components.chips.TeacherBadge
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun UserBadges(
    user: UserObject,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (user.admin) {
            item {
                AdminBadge()
            }
        }
        if (user.helper) {
            item {
                HelperBadge()
            }
        }
        if (user.student) {
            item {
                StudentBadge()
            }
        }
        if (user.teacher) {
            item {
                TeacherBadge()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) { mockUser() }
        UserBadges(
            user = api.users.cache.get(1)!!,
        )
    }
}
