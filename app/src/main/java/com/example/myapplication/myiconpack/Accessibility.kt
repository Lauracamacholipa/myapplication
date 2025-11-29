package com.example.myapplication.myiconpack

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.MyIconPack
import kotlin.Unit

public val MyIconPack.Accessibility: ImageVector
    get() {
        if (_accessibility != null) {
            return _accessibility!!
        }
        _accessibility = Builder(name = "Accessibility", defaultWidth = 800.0.dp, defaultHeight =
                800.0.dp, viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = EvenOdd) {
                moveTo(16.0f, 8.0f)
                curveTo(16.0f, 12.418f, 12.418f, 16.0f, 8.0f, 16.0f)
                curveTo(3.582f, 16.0f, 0.0f, 12.418f, 0.0f, 8.0f)
                curveTo(0.0f, 3.582f, 3.582f, 0.0f, 8.0f, 0.0f)
                curveTo(12.418f, 0.0f, 16.0f, 3.582f, 16.0f, 8.0f)
                close()
                moveTo(9.25f, 3.75f)
                curveTo(9.25f, 4.44f, 8.69f, 5.0f, 8.0f, 5.0f)
                curveTo(7.31f, 5.0f, 6.75f, 4.44f, 6.75f, 3.75f)
                curveTo(6.75f, 3.06f, 7.31f, 2.5f, 8.0f, 2.5f)
                curveTo(8.69f, 2.5f, 9.25f, 3.06f, 9.25f, 3.75f)
                close()
                moveTo(12.0f, 8.0f)
                horizontalLineTo(9.419f)
                lineTo(11.205f, 13.0f)
                horizontalLineTo(9.081f)
                lineTo(8.0f, 9.973f)
                lineTo(6.919f, 13.0f)
                horizontalLineTo(4.795f)
                lineTo(6.581f, 8.0f)
                horizontalLineTo(4.0f)
                verticalLineTo(6.0f)
                horizontalLineTo(12.0f)
                verticalLineTo(8.0f)
                close()
            }
        }
        .build()
        return _accessibility!!
    }

private var _accessibility: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = MyIconPack.Accessibility, contentDescription = "")
    }
}
