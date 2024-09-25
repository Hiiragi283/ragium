package hiiragi283.ragium.common.block.entity.machine.heat

import hiiragi283.ragium.common.block.entity.machine.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.RagiumMachineConditions
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.machine.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTBrickAlloyFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(HTMachineType.Single.ALLOY_FURNACE, RagiumBlockEntityTypes.BRICK_ALLOY_FURNACE, pos, state) {
    override val condition: (World, BlockPos) -> Boolean = RagiumMachineConditions.HEAT

    override fun getDisplayName(): Text = HTMachineType.Single.ALLOY_FURNACE.text
}
