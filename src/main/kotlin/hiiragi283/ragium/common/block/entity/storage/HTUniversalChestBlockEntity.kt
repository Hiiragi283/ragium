package hiiragi283.ragium.common.block.entity.storage

import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.storage.HTHandlerProvider
import hiiragi283.core.common.block.entity.HTExtendedBlockEntity
import hiiragi283.ragium.common.item.HTUniversalChestManager
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable

class HTUniversalChestBlockEntity(pos: BlockPos, state: BlockState) :
    HTExtendedBlockEntity(RagiumBlockEntityTypes.UNIVERSAL_CHEST, pos, state),
    HTHandlerProvider {
    @DescSynced
    @Persisted
    var color: DyeColor = DyeColor.WHITE

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        componentInput.get(RagiumDataComponents.COLOR)?.let(::color::set)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumDataComponents.COLOR, color)
    }

    //    HTHandlerProvider    //

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable? {
        val server: MinecraftServer = HiiragiCoreAPI.getActiveServer() ?: return null
        return HTUniversalChestManager.getHandler(server, color)
    }

    override fun getFluidHandler(direction: Direction?): IFluidHandler? = null

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = null
}
