package ru.glebik.core.widget.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.glebik.core.designsystem.theme.FintechTheme
import ru.glebik.core.widget.R
import ru.glebik.core.widget.clickableNoInteraction

@Composable
fun SearchTopBar(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    isSearchEnabled: Boolean,
    onSearchVisibleChange: () -> Unit,
    onSearch: (String) -> Unit,
) {

    var hasFocus by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    var cancelWidth by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(FintechTheme.colors.secondary)
            .height(56.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSearchEnabled) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .animateContentSize()
                    .fillMaxWidth()
            ) {

                val focusRequester = remember { FocusRequester() }

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }

                FintechTextField(
                    value = value,
                    hint = stringResource(id = R.string.search_textfield_placeholder),
                    hasFocus = hasFocus,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .animateContentSize()
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            hasFocus = it.hasFocus
                        },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch(value)
                        }
                    ),
                )
                AnimatedVisibility(
                    modifier = Modifier,
                    visible = hasFocus,
                    enter = slideInHorizontally(
                        initialOffsetX = { cancelWidth * 2 },
                        animationSpec = spring()
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { cancelWidth * 2 },
                        animationSpec = spring()
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = FintechTheme.typography.base.copy(
                            fontSize = 20.sp,
                            color = FintechTheme.colors.white
                        ),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .onSizeChanged { cancelWidth = it.width }
                            .clickableNoInteraction {
                                onSearchVisibleChange()
                                focusRequester.freeFocus()
                                focusManager.clearFocus()
                            }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = title,
                    style = FintechTheme.typography.base.copy(
                        fontSize = 24.sp,
                        color = FintechTheme.colors.white
                    ),
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                IconButton(
                    onClick = {
                        onSearchVisibleChange()
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = stringResource(
                            id = R.string.search
                        ),
                        tint = FintechTheme.colors.white
                    )
                }
            }
        }
    }
}
