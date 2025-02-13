package hiiragi283.ragium.api.multiblock

import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.network.chat.Component
import net.minecraft.world.level.block.state.BlockState

/**
 * マルチブロックを構成する要素を表すインターフェース
 */
interface HTMultiblockComponent {
    fun getType(): Type<*>

    /**
     * 指定した[controller]からこの要素の名前を返します。
     */
    fun getBlockName(controller: HTControllerDefinition): Component

    /**
     * 指定した[controller]と[pos]から，この要素が条件を満たしているか判定します。
     * @param pos この要素の座標
     */
    fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean

    /**
     * 指定した[controller]から，この要素がコマンドから設置可能かどうか判定します。
     */
    fun getPlacementState(controller: HTControllerDefinition): BlockState?

    /**
     * 指定した値からデータを処理します。
     * @param pos この要素の座標
     * @param builder データを渡す先
     */
    fun collectData(controller: HTControllerDefinition, pos: BlockPos, builder: DataComponentMap.Builder) {}

    //    Type    //

    interface Type<T : HTMultiblockComponent>
}
