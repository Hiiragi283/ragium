package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import net.minecraft.block.BlockState
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

object HTMachineMultiblockComponent : HTMultiblockComponent {
    override fun getBlockName(controller: HTControllerDefinition): Text = Text.literal("Any Machines")

    override fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean = controller.world.getMachineEntity(pos) != null

    override fun getPlacementState(controller: HTControllerDefinition, pos: BlockPos): BlockState? = null

    override fun collectData(controller: HTControllerDefinition, pos: BlockPos, holder: HTMutablePropertyHolder) {
        val child: HTMachineBlockEntityBase = controller.world.getMachineEntity(pos) ?: return
        holder[HTMachinePropertyKeys.EXTENDED_CHILD] = child.machineKey
    }
}
