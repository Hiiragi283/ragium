package hiiragi283.ragium.common.block.entity.collect

import hiiragi283.ragium.api.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemVariant
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState

class HTHoneyCollector(pos: BlockPos, state: BlockState) : HTTickAwareBlockEntity(TODO(), pos, state) {
    private val inputSlot: HTItemSlot = HTItemSlot.create("input_slot", this) {
        validator = { variant: HTItemVariant -> variant.isOf(RagiumItems.BOTTLED_BEE) }
    }
    private val outputSlot: HTItemSlot = HTItemSlot.create("output_slot", this)

    private val outputTank: HTFluidTank = HTFluidTank.create("output_tank", this)

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        inputSlot.writeNbt(nbt, registryOps)
        outputSlot.writeNbt(nbt, registryOps)

        outputTank.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        inputSlot.readNbt(nbt, registryOps)
        outputSlot.readNbt(nbt, registryOps)

        outputTank.readNbt(nbt, registryOps)
    }

    //    Ticking    //

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        TODO("Not yet implemented")
    }
}
