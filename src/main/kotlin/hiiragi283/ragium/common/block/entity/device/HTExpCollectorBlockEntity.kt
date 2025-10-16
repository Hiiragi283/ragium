package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.common.storage.fluid.tank.HTExpOrbTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Position
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTExpCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity(HTDeviceVariant.EXP_COLLECTOR, pos, state),
    HTFluidInteractable {
    private lateinit var tank: HTVariableFluidStackTank

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        tank = HTVariableFluidStackTank.output(listener, RagiumConfig.COMMON.deviceCollectorTankCapacity)
        return HTSimpleFluidTankHolder.output(null, tank)
    }

    override fun onRemove(level: Level, pos: Position) {
        super.onRemove(level, pos)
        ExperienceOrb(
            level,
            pos.x(),
            pos.y(),
            pos.z(),
            fluidAmountToExpValue(tank.getAmountAsInt()),
        )
    }

    private fun fluidAmountToExpValue(amount: Int): Int = amount / RagiumConfig.COMMON.expCollectorMultiplier.asInt

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 範囲内のExp Orbを取得する
        val expOrbs: List<ExperienceOrb> = level.getEntitiesOfClass(
            ExperienceOrb::class.java,
            pos.getRangedAABB(RagiumConfig.COMMON.deviceCollectorEntityRange.asDouble),
        )
        if (expOrbs.isEmpty()) return false
        // それぞれのExp Orbに対して回収を行う
        expOrbs
            .asSequence()
            .filter(ExperienceOrb::isAlive)
            .map(::HTExpOrbTank)
            .forEach { orbFluidTank: HTExpOrbTank -> HTStackSlotHelper.moveStack(orbFluidTank, tank) }
        return true
    }

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = interactWith(player, hand, tank)
}
