package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.machine.HTMachineTier
import net.minecraft.block.BlockState
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

class HTBrickAlloyFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(
        RagiumBlockEntityTypes.BRICK_ALLOY_FURNACE,
        pos,
        state,
        RagiumMachineTypes.Single.ALLOY_FURNACE,
        HTMachineTier.PRIMITIVE,
    ) {
    override fun getDisplayName(): Text = RagiumMachineTypes.Single.ALLOY_FURNACE.text
}
