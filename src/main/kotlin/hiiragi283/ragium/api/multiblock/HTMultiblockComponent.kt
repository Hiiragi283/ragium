package hiiragi283.ragium.api.multiblock

import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.network.chat.Component
import net.minecraft.world.level.block.state.BlockState

/**
 * マルチブロックを構成する要素を表すインターフェース
 */
interface HTMultiblockComponent {
    /**
     * 指定した[definition]からこの要素の名前を返します。
     */
    fun getBlockName(definition: HTControllerDefinition): Component

    /**
     * 指定した[definition]と[pos]から，この要素が条件を満たしているか判定します。
     * @param pos この要素の座標
     */
    fun checkState(definition: HTControllerDefinition, pos: BlockPos): Boolean

    /**
     * 指定した[definition]から，この要素がコマンドから設置可能かどうか判定します。
     */
    fun getPlacementState(definition: HTControllerDefinition): BlockState?

    /**
     * 指定した値からデータを処理します。
     * @param pos この要素の座標
     * @param builder データを渡す先
     */
    fun collectData(definition: HTControllerDefinition, pos: BlockPos, builder: DataComponentMap.Builder) {}
}
