package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.HTMultiMachineBlockEntity
import hiiragi283.ragium.common.recipe.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class HTMultiMachineBlock(val recipeType: HTMachineType) : HTBlockWithEntity(Settings.create()) {

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        HTMultiMachineBlockEntity(pos, state)

}