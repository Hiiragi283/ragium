package hiiragi283.lib.gui

import net.minecraft.world.item.ItemStack

/**
 * GUIを表すインターフェースです。
 *
 * 参照 : [Mekanism - IGuiWrapper](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/client/gui/IGuiWrapper.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTGuiAccess {
    /**
     * カーソル上の[アイテム][ItemStack]を取得します。
     */
    val carried: ItemStack get() = ItemStack.EMPTY

    /**
     * このGUIの始点のx座標を取得します。
     */
    fun getLeftPos(): Int

    /**
     * このGUIの始点のy座標を取得します。
     */
    fun getTopPos(): Int

    /**
     * このGUIの幅を取得します。
     */
    fun getImageWidth(): Int

    /**
     * このGUIの高さを取得します。
     */
    fun getImageHeight(): Int
}
