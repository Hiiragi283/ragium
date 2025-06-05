package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemVariant
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState

class HTAutoBeehiveBlockEntity(pos: BlockPos, state: BlockState) : HTTickAwareBlockEntity(TODO(), pos, state) {
    private val inputSlot: HTItemSlot = HTItemSlot.create(RagiumConstantValues.INPUT_SLOT, this) {
        validator = { variant: HTItemVariant -> variant.isOf(RagiumItems.BOTTLED_BEE) }
    }
    private val outputSlot: HTItemSlot = HTItemSlot.create(RagiumConstantValues.OUTPUT_SLOT, this)

    private val outputTank: HTFluidTank = HTFluidTank.create(RagiumConstantValues.OUTPUT_TANK, this)

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        inputSlot.writeNbt(writer)
        outputSlot.writeNbt(writer)

        outputTank.writeNbt(writer)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        inputSlot.readNbt(reader)
        outputSlot.readNbt(reader)

        outputTank.readNbt(reader)
    }

    //    Ticking    //

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        TODO("Not yet implemented")
    }

    override val maxTicks: Int = 200
}
