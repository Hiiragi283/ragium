package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.extension.getRangedAABB
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.item.HTSlotProvider
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.util.variant.HTDeviceVariant
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
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
    HTFluidInteractable,
    HTSlotProvider.Empty {
    private val tank = HTFluidStackTank(Int.MAX_VALUE, this)

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConst.TANK, tank)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConst.TANK, tank)
    }

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 範囲内のExp Orbを取得する
        val expOrbs: List<ExperienceOrb> = level.getEntitiesOfClass(
            ExperienceOrb::class.java,
            blockPos.getRangedAABB(RagiumAPI.getConfig().getEntityCollectorRange()),
        )
        if (expOrbs.isEmpty()) return false
        // それぞれのExp Orbに対して回収を行う
        for (entity: ExperienceOrb in expOrbs) {
            val fluidAmount: Int = entity.value * RagiumAPI.getConfig().getExpCollectorMultiplier()
            val stack: FluidStack = RagiumFluidContents.EXPERIENCE.toStack(fluidAmount)
            if (tank.canFill(stack, true)) {
                tank.fill(stack, false)
                entity.discard()
            }
        }
        return true
    }

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler = HTFilteredFluidHandler(tank, HTFluidFilter.DRAIN_ONLY)

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult =
        interactWith(player, hand, getFluidHandler(null))
}
