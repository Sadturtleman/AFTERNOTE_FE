package com.kuit.afternote.core.ui.component.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.uimodel.Step
import com.kuit.afternote.feature.onboarding.presentation.component.StepProgressBar
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = "",
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontFamily = Sansneo
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = "",
    onBackClick: () -> Unit,
    onActionClick: () -> Unit,
    actionText: String = "등록"
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontFamily = Sansneo
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        actions = {
            TextButton(onClick = onActionClick) {
                Text(
                    text = actionText,
                    fontSize = 14.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray5
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontFamily = Sansneo
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    step: Step,
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Sansneo
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
        StepProgressBar(
            step = step.value,
            totalStep = 4
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        title = {
            // 타이틀 없음 (빈 공간)
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "뒤로가기",
                    modifier = Modifier.size(
                        width = 6.dp,
                        height = 12.dp
                    )
                )
            }
        },
        actions = {
            IconButton(onClick = onEditClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = "편집",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

/**
 * TopBar with optional custom [navigationIcon] and [titleContent] slots.
 * Use when the screen must keep its existing top bar UI (e.g. X icon, recipient dropdown).
 *
 * @param title Default title text when [titleContent] is null
 * @param onBackClick Back/close click callback
 * @param onActionClick Action button click (right side). Shown only when [actionText] is not empty
 * @param actionText Action button label (e.g. "등록", "편집", "완료")
 * @param navigationIcon Custom left icon; when null, default back arrow is used
 * @param titleContent Custom center content; when null, [title] is shown as Text
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = "",
    onBackClick: () -> Unit,
    onActionClick: () -> Unit = {},
    actionText: String = "",
    navigationIcon: (@Composable () -> Unit)? = null,
    titleContent: (@Composable () -> Unit)? = null
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        title = {
            if (titleContent != null) {
                titleContent()
            } else {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Sansneo
                )
            }
        },
        navigationIcon = {
            if (navigationIcon != null) {
                navigationIcon()
            } else {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        actions = {
            if (actionText.isNotEmpty()) {
                TextButton(onClick = onActionClick) {
                    Text(
                        text = actionText,
                        fontSize = 14.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal,
                        color = Gray5
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Preview(showBackground = true, name = "Material3 - 뒤로가기 있음")
@Composable
private fun TopBarWithBackPreview() {
    AfternoteTheme {
        TopBar("메인 화면") { }
    }
}

@Preview(showBackground = true, name = "Material3 - 뒤로가기 없음")
@Composable
private fun TopBarWithoutBackPreview() {
    AfternoteTheme {
        TopBar("메인 화면")
    }
}

@Preview(showBackground = true, name = "Material3 - Step 포함")
@Composable
private fun TopBarWithStepPreview() {
    AfternoteTheme {
        TopBar(
            title = "회원가입",
            step = object : Step {
                override val value: Int = 1

                override fun previous(): Step? = null
            },
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Material3 - 타이틀만")
@Composable
private fun TopBarTitleOnlyPreview() {
    AfternoteTheme {
        TopBar(title = "애프터노트")
    }
}

@Preview(showBackground = true, name = "Material3 - 뒤로가기 + 액션")
@Composable
private fun TopBarWithActionPreview() {
    AfternoteTheme {
        TopBar(
            onBackClick = {},
            onActionClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Material3 - 뒤로가기 + 타이틀 + 액션")
@Composable
private fun TopBarWithTitleAndActionPreview() {
    AfternoteTheme {
        TopBar(
            title = "뒤로가기+액션",
            onBackClick = {},
            onActionClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Material3 - 뒤로가기 + 타이틀")
@Composable
private fun TopBarWithBackAndTitlePreview() {
    AfternoteTheme {
        TopBar(
            title = "추모 플레이리스트",
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Material3 - 뒤로가기 + 편집")
@Composable
private fun TopBarWithEditPreview() {
    AfternoteTheme {
        TopBar(
            onBackClick = {},
            onEditClick = {}
        )
    }
}
