package hiiragi283.ragium.api.multiblock

import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import net.minecraft.block.BlockState
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

/**
 * マルチブロックを構成する要素を表すインターフェース
 */
interface HTMultiblockComponent {
    fun getBlockName(controller: HTControllerDefinition): Text

    fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean

    fun getPlacementState(controller: HTControllerDefinition, pos: BlockPos): BlockState?

    fun collectData(controller: HTControllerDefinition, pos: BlockPos, holder: HTMutablePropertyHolder) {}
}
