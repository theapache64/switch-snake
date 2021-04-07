package com.theapache64.switchsnake.composable

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.theapache64.switchsnake.APPLE_COLOR
import com.theapache64.switchsnake.CellType
import com.theapache64.switchsnake.SNAKE_COLOR
import com.theapache64.switchsnake.model.Cell

@Composable
fun Node(
    x: Int,
    y: Int,
    snakeCells: List<Cell>,
    cellType: CellType,
    appleCell: Cell,
) {
    // Finding cell
    val snakeCell = snakeCells.find { it.x == x && it.y == y }
    val isSnakeCell = snakeCell != null
    val isHead = snakeCells.indexOf(snakeCell) == 0
    val isAppleCell = appleCell.x == x && appleCell.y == y

    // Switch
    val isChecked = isSnakeCell || isAppleCell

    when (cellType) {
        CellType.Switch -> {

            val snakeCellSwitchColor = SwitchDefaults.colors(checkedThumbColor = SNAKE_COLOR)
            val appleCellSwitchColor = SwitchDefaults.colors(checkedThumbColor = APPLE_COLOR)
            val defaultCellSwitchColor = SwitchDefaults.colors()

            Switch(
                checked = isChecked,
                onCheckedChange = null,
                colors = when {
                    isSnakeCell -> if (isHead) {
                        appleCellSwitchColor
                    } else {
                        snakeCellSwitchColor
                    }
                    isAppleCell -> appleCellSwitchColor
                    else -> defaultCellSwitchColor
                }
            )
        }
        CellType.CheckBox -> {
            val snakeCellCheckboxColor = CheckboxDefaults.colors(checkedColor = SNAKE_COLOR)
            val appleCellCheckboxColor = CheckboxDefaults.colors(checkedColor = APPLE_COLOR)
            val defaultCellCheckboxColor = CheckboxDefaults.colors()


            Checkbox(
                checked = isChecked,
                onCheckedChange = null,
                colors = when {
                    isSnakeCell -> if (isHead) {
                        appleCellCheckboxColor
                    } else {
                        snakeCellCheckboxColor
                    }
                    isAppleCell -> appleCellCheckboxColor
                    else -> defaultCellCheckboxColor
                }
            )
        }
        CellType.Radio -> {

            val snakeCellRadioColor = RadioButtonDefaults.colors(selectedColor = SNAKE_COLOR)
            val appleCellRadioColor = RadioButtonDefaults.colors(selectedColor = APPLE_COLOR)
            val defaultCellRadioColor = RadioButtonDefaults.colors()

            RadioButton(
                selected = isChecked,
                onClick = null,
                colors = when {
                    isSnakeCell -> if (isHead) {
                        appleCellRadioColor
                    } else {
                        snakeCellRadioColor
                    }
                    isAppleCell -> appleCellRadioColor
                    else -> defaultCellRadioColor
                }
            )
        }
    }
}