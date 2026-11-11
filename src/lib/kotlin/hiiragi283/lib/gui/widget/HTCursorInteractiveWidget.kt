package hiiragi283.lib.gui.widget

import net.neoforged.neoforge.transfer.access.ItemAccess

/**
 * カーソル上のアイテムと相互作用できるウィジェットを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
interface HTCursorInteractiveWidget {
    /**
     * サーバー側の[アイテム][ItemAccess]と相互作用します。
     */
    fun interactWithCursor(access: ItemAccess)
}
