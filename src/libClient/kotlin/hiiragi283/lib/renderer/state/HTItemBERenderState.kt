package hiiragi283.lib.renderer.state

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.item.ItemStackRenderState

/**
 * Hiiragi Seriesで使用される，アイテムを描画する[BlockEntityRenderState]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
class HTItemBERenderState : BlockEntityRenderState() {
    val itemState: ItemStackRenderState = ItemStackRenderState()
}
