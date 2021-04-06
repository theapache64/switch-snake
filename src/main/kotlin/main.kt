import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


private const val COLUMNS = 30
private const val ROWS = 15
private const val SWITCH_WIDTH = 40
private const val SWITCH_HEIGHT = 29
private const val GAME_LOOP_DELAY = 120L

private val APPLE_COLOR = Color(0xffff0800)
private val SNAKE_COLOR = Color(0xff53B32C)
const val WINDOW_PADDING = 60
const val WINDOW_WIDTH = (SWITCH_WIDTH * COLUMNS) + WINDOW_PADDING
const val WINDOW_HEIGHT = (SWITCH_HEIGHT * ROWS) + WINDOW_PADDING

val ORIGIN = arrayOf(
    Cell(2, 0),
    Cell(1, 0),
    Cell(0, 0),
)


enum class Direction {
    RIGHT, LEFT, UP, DOWN, IDLE
}

enum class CellType {
    Switch, CheckBox, Radio
}

private val DEFAULT_DIRECTION = Direction.RIGHT


fun main() {

    Window(
        title = "Compose Switch Snake 🐍",
        size = IntSize(WINDOW_WIDTH, WINDOW_HEIGHT)
    ) {

        SwitchSnakeTheme {

            val snakeCellSwitchColor = SwitchDefaults.colors(checkedThumbColor = SNAKE_COLOR)
            val appleCellSwitchColor = SwitchDefaults.colors(checkedThumbColor = APPLE_COLOR)
            val defaultCellSwitchColor = SwitchDefaults.colors()


            val snakeCellCheckboxColor = CheckboxDefaults.colors(checkedColor = SNAKE_COLOR)
            val appleCellCheckboxColor = CheckboxDefaults.colors(checkedColor = APPLE_COLOR)
            val defaultCellCheckboxColor = CheckboxDefaults.colors()

            val snakeCellRadioColor = RadioButtonDefaults.colors(selectedColor = SNAKE_COLOR)
            val appleCellRadioColor = RadioButtonDefaults.colors(selectedColor = APPLE_COLOR)
            val defaultCellRadioColor = RadioButtonDefaults.colors()


            val snakeCells = remember { mutableStateListOf(*ORIGIN) }
            var appleCell by remember { mutableStateOf(getRandomAppleCell()) }
            var activeDirection by remember { mutableStateOf(DEFAULT_DIRECTION) }
            var isGameOver by remember { mutableStateOf(false) }
            var cellType by remember { mutableStateOf(CellType.Switch) }
            var score by remember { mutableStateOf(0) }

            println("Rendering ...")
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .focusable()
                    .onKeyEvent {
                        if (it.nativeKeyEvent.id == NativeKeyEvent.KEY_PRESSED) {
                            val newDirection = when (it.nativeKeyEvent.keyCode) {
                                NativeKeyEvent.VK_UP -> Direction.UP
                                NativeKeyEvent.VK_DOWN -> Direction.DOWN
                                NativeKeyEvent.VK_LEFT -> Direction.LEFT
                                NativeKeyEvent.VK_RIGHT -> Direction.RIGHT
                                else -> null
                            }

                            if (newDirection != null) {
                                val isOpposite =
                                    (activeDirection == Direction.LEFT && newDirection == Direction.RIGHT) ||
                                            (activeDirection == Direction.RIGHT && newDirection == Direction.LEFT) ||
                                            (activeDirection == Direction.UP && newDirection == Direction.DOWN) ||
                                            (activeDirection == Direction.DOWN && newDirection == Direction.UP)

                                if (!isOpposite) {
                                    println("New direction is $newDirection")
                                    activeDirection = newDirection
                                } else {
                                    println("Declined direction -> $newDirection")
                                }
                            }
                        }
                        false
                    }
            ) {

                // Modes
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp)
                ) {

                    // Switch
                    CustomRadioButton(
                        selected = cellType == CellType.Switch,
                        onClick = {
                            cellType = CellType.Switch
                        },
                        text = "Switch"
                    )

                    // Checkbox
                    CustomRadioButton(
                        selected = cellType == CellType.CheckBox,
                        onClick = {
                            cellType = CellType.CheckBox
                        },
                        text = "CheckBox"
                    )

                    // Radio
                    CustomRadioButton(
                        selected = cellType == CellType.Radio,
                        onClick = {
                            cellType = CellType.Radio
                        },
                        text = "Radio"
                    )

                }

                // Score
                Text(
                    text = "SCORE: $score",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                )


                Row {
                    repeat(COLUMNS) { x ->
                        Column {
                            repeat(ROWS) { y ->

                                // Finding cell
                                val isSnakeCell = snakeCells.find { it.x == x && it.y == y } != null
                                val isAppleCell = appleCell.x == x && appleCell.y == y

                                // Switch
                                when (cellType) {
                                    CellType.Switch -> {
                                        Switch(
                                            checked = isSnakeCell || isAppleCell,
                                            onCheckedChange = null,
                                            colors = when {
                                                isSnakeCell -> snakeCellSwitchColor
                                                isAppleCell -> appleCellSwitchColor
                                                else -> defaultCellSwitchColor
                                            }
                                        )
                                    }
                                    CellType.CheckBox -> {
                                        Checkbox(
                                            checked = isSnakeCell || isAppleCell,
                                            onCheckedChange = null,
                                            colors = when {
                                                isSnakeCell -> snakeCellCheckboxColor
                                                isAppleCell -> appleCellCheckboxColor
                                                else -> defaultCellCheckboxColor
                                            }
                                        )
                                    }
                                    CellType.Radio -> {
                                        RadioButton(
                                            selected = isSnakeCell || isAppleCell,
                                            onClick = null,
                                            colors = when {
                                                isSnakeCell -> snakeCellRadioColor
                                                isAppleCell -> appleCellRadioColor
                                                else -> defaultCellRadioColor
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (isGameOver) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("GAME OVER!", fontSize = 50.sp)
                        Button(
                            onClick = {
                                snakeCells.clear()
                                snakeCells.addAll(ORIGIN)
                                activeDirection = Direction.RIGHT
                                score = 0
                                appleCell = getRandomAppleCell()
                                isGameOver = false
                            }
                        ) {
                            Text("RESTART")
                        }
                    }
                }
            }

            LaunchedEffect(isGameOver) {
                println("Game loop started")


                while (!isGameOver) {
                    delay(GAME_LOOP_DELAY)

                    val currentHead = snakeCells.first()

                    // Finding heading path
                    val newHead = when (activeDirection) {
                        Direction.IDLE -> null
                        Direction.RIGHT -> currentHead.copy(x = currentHead.x + 1)
                        Direction.LEFT -> currentHead.copy(x = currentHead.x - 1)
                        Direction.UP -> currentHead.copy(y = currentHead.y - 1)
                        Direction.DOWN -> currentHead.copy(y = currentHead.y + 1)
                    }


                    // Adding new head
                    if (newHead != null) {
                        snakeCells.removeAt(snakeCells.size - 1)
                        snakeCells.add(0, newHead)
                        val newCells = snakeCells.toMutableList()
                        snakeCells.clear()
                        snakeCells.addAll(newCells)
                    }


                    // Collision detection
                    val snakeHead = snakeCells.first()
                    if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x >= COLUMNS || snakeHead.y >= ROWS) {
                        println("Over: head is at $snakeHead")
                        isGameOver = true
                    }

                    // is ate apple?
                    if (snakeHead.x == appleCell.x && snakeHead.y == appleCell.y) {
                        // apple ate
                        appleCell = getRandomAppleCell()

                        // Increase snake length
                        val tail = snakeCells.last()
                        val newTail = when (activeDirection) {
                            Direction.LEFT -> {
                                tail.copy(x = tail.x + 1)
                            }
                            Direction.RIGHT -> {
                                tail.copy(x = tail.x - 1)
                            }
                            Direction.UP -> {
                                tail.copy(y = tail.y + 1)
                            }
                            Direction.DOWN -> {
                                tail.copy(y = tail.y - 1)
                            }
                            Direction.IDLE -> null
                        }
                        // Adding new tail
                        if (newTail != null) {
                            snakeCells.add(newTail)
                        }

                        // Increase score
                        score += 2
                    }
                }

            }
        }
    }
}

private val xEngine = (0 until COLUMNS)
private val yEngine = (0 until ROWS)
fun getRandomAppleCell(): Cell {
    return Cell(
        xEngine.random(),
        yEngine.random()
    )
}
