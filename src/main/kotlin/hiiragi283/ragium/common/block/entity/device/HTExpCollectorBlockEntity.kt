package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.common.storage.fluid.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTExpCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity(HTDeviceVariant.EXP_COLLECTOR, pos, state),
    HTFluidInteractable {
    private lateinit var tank: HTVariableFluidStackTank

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        tank = HTVariableFluidStackTank.output(listener, RagiumConfig.CONFIG.deviceCollectorTankCapacity)
        return HTSimpleFluidTankHolder.output(null, tank)
    }

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 範囲内のExp Orbを取得する
        val expOrbs: List<ExperienceOrb> = level.getEntitiesOfClass(
            ExperienceOrb::class.java,
            blockPos.getRangedAABB(RagiumConfig.CONFIG.deviceCollectorEntityRange.asDouble),
        )
        if (expOrbs.isEmpty()) return false
        // それぞれのExp Orbに対して回収を行う
        for (entity: ExperienceOrb in expOrbs) {
            val fluidAmount: Int = entity.value * RagiumConfig.CONFIG.expCollectorMultiplier.asInt
            val stack: FluidStack = RagiumFluidContents.EXPERIENCE.toStack(fluidAmount)
            if (tank.insert(stack, true, HTStorageAccess.INTERNAl).isEmpty) {
                tank.insert(stack, false, HTStorageAccess.INTERNAl)
                entity.discard()
            }
        }
        return true
    }

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = interactWith(player, hand, tank)
}
