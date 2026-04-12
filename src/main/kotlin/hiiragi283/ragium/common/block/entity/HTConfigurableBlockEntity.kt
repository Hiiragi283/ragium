package hiiragi283.ragium.common.block.entity

import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.component.HTSlotInfoComponent
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.common.storge.holder.HTSlotInfoProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState

/**
 * 搬入出の面を制御可能な[HTBlockEntity]の拡張クラス
 * @see mekanism.common.tile.prefab.TileEntityConfigurableMachine
 */
abstract class HTConfigurableBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(
        type,
        pos,
        state,
    ),
    HTSlotInfoProvider {
    //    HTSlotInfoProvider    //

    override fun initializeVariables() {
        super.initializeVariables()
        machineSlot = HTSlotInfoComponent(this)
    }

    lateinit var machineSlot: HTSlotInfoComponent
        private set

    final override fun getSlotInfo(side: Direction): HTSlotInfo = machineSlot.getSlotInfo(side)
}
