package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.read
import hiiragi283.core.api.serialization.value.write
import hiiragi283.core.api.storage.HTHandlerProvider
import hiiragi283.core.api.storage.item.HTItemHandler
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.block.entity.HTExtendedBlockEntity
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.common.item.HTUniversalChestManager
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTUniversalChestBlockEntity(pos: BlockPos, state: BlockState) :
    HTExtendedBlockEntity(RagiumBlockEntityTypes.UNIVERSAL_CHEST, pos, state),
    HTHandlerProvider,
    HTItemHandler {
    var color: HTDefaultColor = HTDefaultColor.WHITE

    //    Save & Load    //

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.write("color", HTDefaultColor.CODEC, color)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        input.read("color", HTDefaultColor.CODEC)?.let(::color::set)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        componentInput.get(HCDataComponents.COLOR)?.let(::color::set)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(HCDataComponents.COLOR, color)
    }

    override fun initReducedUpdateTag(output: HTValueOutput) {
        super.initReducedUpdateTag(output)
        output.write("color", HTDefaultColor.CODEC, color)
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        input.read("color", HTDefaultColor.CODEC)?.let(::color::set)
    }

    //    HTHandlerProvider    //

    override fun getItemHandler(direction: Direction?): HTItemHandler? {
        val server: MinecraftServer = HiiragiCoreAPI.getActiveServer() ?: return null
        return HTUniversalChestManager.getHandler(server, color)
    }

    override fun getFluidHandler(direction: Direction?): IFluidHandler? = null

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = null

    override fun getItemSlots(side: Direction?): List<HTItemSlot> = getItemHandler(side)?.getItemSlots(side) ?: listOf()
}
