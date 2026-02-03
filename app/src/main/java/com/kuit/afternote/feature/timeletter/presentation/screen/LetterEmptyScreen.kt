package com.kuit.afternote.feature.timeletter.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar

@Composable
fun LetterEmptyScreen(
    onNavigateBack: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxWidth(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LEFT
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.vector),
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .size(width = 6.dp, height = 12.dp)
                            .clickable { onNavigateBack() }
                    )
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "타임레터",
                        color = Color(0xFF212121),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.size(48.dp))
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = BottomNavItem.TIME_LETTER,
                onItemSelected = { }
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clickable { onAddClick() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "add",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(287.38.dp))

            Column {
                Image(
                    painter = painterResource(id = R.drawable.letter),
                    contentDescription = "편지이미지",
                    modifier = Modifier
                        .padding(horizontal = 150.dp)
                        .width(88.dp)
                        .height(57.57.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "아직 등록된 타임 레터가 없습니다.\n타임 레터를 작성하여\n소중한 사람에게 마음을 전하세요",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        fontFamily = FontFamily(Font(R.font.sansneoregular)),
                        color = Color(0xFF9E9E9E),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun LetterEmptyScreenPreview() {
    LetterEmptyScreen(
        onNavigateBack = {},
        onAddClick = {}
    )
}
