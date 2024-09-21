package hiiragi283.ragium.client.integration.rei.category

import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.common.display.Display

interface HTDisplayCategory<T : Display> : DisplayCategory<T> {
    fun getPoint(bounds: Rectangle, x: Int, y: Int): Point = Point(bounds.x + 5 + x * 18, bounds.y + 5 + y * 18)

    fun getPoint(bounds: Rectangle, x: Double, y: Double): Point = Point(bounds.x + 5 + x * 18, bounds.y + 5 + y * 18)
}
