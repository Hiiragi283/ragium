package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.recipe.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class HTMultiMachineBlockEntity(
    pos: BlockPos,
    state: BlockState,
) : BlockEntity(HTMachineType.Variant.MULTI.blockEntityType, pos, state) {
}