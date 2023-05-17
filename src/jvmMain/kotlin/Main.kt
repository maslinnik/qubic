import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import qubic.core.GameState
import qubic.core.PlayerId
import qubic.rendering.GameRenderer
import qubic.strategy.ForcedSequenceBot

enum class MenuState {
    MAIN,
    PVP,
    BOT_CHOICE,
    BOT,
    RED_WON,
    BLUE_WON,
    DRAW
}

fun main() = application {
    Window(
        state = WindowState(size = DpSize.Unspecified),
        title = "QUBIC",
        onCloseRequest = ::exitApplication,
        resizable = false,
        icon = painterResource("icon.png")
    ) {
        var helpMenuActive by remember { mutableStateOf(false) }
        var menuState by remember { mutableStateOf(MenuState.MAIN) }
        var state: GameState? by remember { mutableStateOf(null) }
        val bot = ForcedSequenceBot()
        val renderer = GameRenderer()

        when (menuState) {
            MenuState.BOT -> {
                renderer.selectable = true
                renderer.onClick = {
                    state = state!!.move(it)
                    if (!state!!.isEnded()) {
                        state = state!!.move(bot.move(state!!))
                    }
                }
            }

            MenuState.PVP -> {
                renderer.selectable = true
                renderer.onClick = {
                    state = state!!.move(it)
                }
            }

            else -> {
                renderer.selectable = false
                renderer.onClick = {}
            }
        }
        if (state != null && state!!.isEnded()) {
            renderer.onClick = {}
            menuState = when (state!!.winner()) {
                null -> MenuState.DRAW
                PlayerId.FIRST -> MenuState.RED_WON
                PlayerId.SECOND -> MenuState.BLUE_WON
            }
        }

        @Composable
        fun Menu() = Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            when (menuState) {
                MenuState.MAIN -> {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 20.sp,
                        text = "Привет! Выбери режим игры:"
                    )
                    Row (
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                menuState = MenuState.BOT_CHOICE
                            }
                        ) {
                            Text("Один игрок")
                        }
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                menuState = MenuState.PVP
                                state = GameState()
                            }
                        ) {
                            Text("Два игрока")
                        }
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                helpMenuActive = true
                            }
                        ) {
                            Text("Правила")
                        }
                    }
                }

                MenuState.BOT_CHOICE -> {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 20.sp,
                        text = "Кто будет ходить первым?"
                    )
                    Row (
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                menuState = MenuState.BOT
                                state = GameState()
                            }
                        ) {
                            Text("Я")
                        }
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                menuState = MenuState.BOT
                                state = GameState()
                                state = state!!.move(bot.move(state!!))
                            }
                        ) {
                            Text("Бот")
                        }
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                menuState = MenuState.MAIN
                            }
                        ) {
                            Text("Назад")
                        }
                    }
                }

                MenuState.BOT -> {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 20.sp,
                        text = "Режим игры с ботом"
                    )
                    Row (
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                menuState = MenuState.MAIN
                                state = null
                            }
                        ) {
                            Text("Назад")
                        }
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                helpMenuActive = true
                            }
                        ) {
                            Text("Правила")
                        }
                    }
                }

                MenuState.PVP -> {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 20.sp,
                        text = "Режим игры на двоих"
                    )
                    Row (
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                menuState = MenuState.MAIN
                                state = null
                            }
                        ) {
                            Text("Назад")
                        }
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                helpMenuActive = true
                            }
                        ) {
                            Text("Правила")
                        }
                    }
                }

                MenuState.RED_WON -> {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 20.sp,
                        color = Color.Red,
                        text = "Красные победили!"
                    )
                    Row (
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                menuState = MenuState.MAIN
                                state = null
                            }
                        ) {
                            Text("Назад")
                        }
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                helpMenuActive = true
                            }
                        ) {
                            Text("Правила")
                        }
                    }
                }

                MenuState.BLUE_WON -> {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 20.sp,
                        color = Color.Blue,
                        text = "Синие победили!"
                    )
                    Row (
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                menuState = MenuState.MAIN
                                state = null
                            }
                        ) {
                            Text("Назад")
                        }
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                helpMenuActive = true
                            }
                        ) {
                            Text("Правила")
                        }
                    }
                }

                MenuState.DRAW -> {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 20.sp,
                        text = "Ничья!"
                    )
                    Row (
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                menuState = MenuState.MAIN
                                state = null
                            }
                        ) {
                            Text("Назад")
                        }
                        Button(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {
                                helpMenuActive = true
                            }
                        ) {
                            Text("Правила")
                        }
                    }
                }
            }
        }

        @Composable
        fun HelpMenu() = Popup(
            alignment = Alignment.Center
        ) {
            Card (
                modifier = Modifier.wrapContentSize().shadow(10.dp).fillMaxWidth(0.8f)
            ) {
                Column (
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Qubic — это крестики-нолики 4x4x4. Чтобы выиграть, поставьте четыре фишки в ряд!\n" +
                                "С помощью мышки можно поворачивать игровое поле в пространстве.\n" +
                                "Чтобы установить фишку, кликните по пустому полю.\n" +
                                "В режиме «Один игрок» можно сыграть с ботом. Доказано, что выигрышную стратегию имеет первый игрок. Сможете её обнаружить?\n" +
                                "Режим «Два игрока» позволяет сыграть с другом. Выясните, кто из вас сильнее!"
                    )
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            helpMenuActive = false
                        }
                    ) {
                        Text("Назад")
                    }
                }
            }
        }

        if (helpMenuActive) {
            HelpMenu()
        }

        Column(
            modifier = Modifier
                .width(500.dp)
        ) {
            Menu()
            Divider()
            renderer.render(
                modifier = Modifier
                    .size(500.dp, 500.dp),
                state = state
            )
        }
    }
}
