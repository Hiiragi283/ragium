package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.machine.HTMachineTier
import net.minecraft.block.BlockState
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTBrickAlloyFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(
        RagiumBlockEntityTypes.BRICK_ALLOY_FURNACE,
        pos,
        state,
        RagiumMachineTypes.Single.ALLOY_FURNACE,
        HTMachineTier.PRIMITIVE,
    ) {
    override val condition: (World, BlockPos) -> Boolean = RagiumMachineConditions.HEAT

    override fun getDisplayName(): Text = RagiumMachineTypes.Single.ALLOY_FURNACE.text
}
