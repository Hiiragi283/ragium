package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.common.storage.energy.battery.HTBasicEnergyBattery
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

open class HTBufferBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(blockHolder, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlocks.BUFFER, pos, state)

    lateinit var battery: HTBasicEnergyBattery
        private set
    lateinit var tanks: List<HTBasicFluidTank>
        private set
    lateinit var slots: List<HTBasicItemSlot>
        private set
    
    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        tanks = arrayOf(0, 1, 2).map { i: Int ->
            builder.addSlot(
                HTSlotInfo.BOTH,
                HTBasicFluidTank.create(listener, 4000, filter = { stack: ImmutableFluidStack ->
                    for (j: Int in tanks.indices) {
                        if (i == j) continue
                        val tankIn: HTBasicFluidTank = tanks[j]
                        if (tankIn.isSameStack(stack)) {
                            return@create false
                        }
                    }
                    true
                })
            )
        }
    }

    //    Ticking    //

    private val oldScales: Array<Fraction> = Array(4) { Fraction.ZERO }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val list: List<() -> Fraction> = listOf(
            battery::getStoredLevel,
            tanks[0]::getStoredLevel,
            tanks[1]::getStoredLevel,
            tanks[2]::getStoredLevel,
        )
        var needSync = false
        for (i: Int in list.indices) {
            val scale: Fraction = list[i]()
            if (scale != this.oldScales[i]) {
                this.oldScales[i] = scale
                needSync = true
            }
        }
        return needSync
    }
}
