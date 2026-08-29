package hiiragi283.lib.gui

import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.renderer.Rect2i

//    Rect2i    //

/**
 * この[HTBounds][this]を[Rect2i]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun HTBounds.toRec2i(): Rect2i = Rect2i(this.x, this.y, this.width, this.height)

/**
 * この[Rect2i][this]を[HTBounds]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun Rect2i.toBounds(): HTBounds = HTBounds(this.x, this.y, this.width, this.height)

//    ScreenRectangle    //

/**
 * この[HTBounds][this]を[ScreenRectangle]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun HTBounds.toRectangle(): ScreenRectangle = ScreenRectangle(this.x, this.y, this.width, this.height)

/**
 * この[ScreenRectangle][this]を[HTBounds]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ScreenRectangle.toBounds(): HTBounds = HTBounds(this.left(), this.top(), this.width, this.height)

/**
 * この[LayoutElement][this]から[HTBounds]を取得します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val LayoutElement.bounds: HTBounds get() = this.rectangle.toBounds()
