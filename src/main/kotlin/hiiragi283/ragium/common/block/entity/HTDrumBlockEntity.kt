package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidTankHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.SimpleFluidContent

abstract class HTDrumBlockEntity(
    capacity: Int,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTBlockEntity(type, pos, state),
    HTFluidTankHandler {
    private val tank: HTFluidTank = HTFluidTank.create("tank", this) {
        this.capacity = capacity
    }

    //    Save & Load    //

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        tank.writeNbt(writer)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        tank.readNbt(reader)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        tank.replace(
            componentInput.getOrDefault(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.EMPTY).copy(),
            true,
        )
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.copyOf(tank.stack))
    }

    override fun reloadUpgrades() {
        super.reloadUpgrades()
        tank
    }

    //    Fluid    //

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.GENERIC

    override fun getFluidTank(tank: Int): HTFluidTank = this.tank

    override fun getTanks(): Int = 1

    //    Impl    //

    class Small(pos: BlockPos, state: BlockState) :
        HTDrumBlockEntity(RagiumConfig.COMMON.smallDrumCapacity.get(), RagiumBlockEntityTypes.SMALL_DRUM, pos, state)

    class Medium(pos: BlockPos, state: BlockState) :
        HTDrumBlockEntity(RagiumConfig.COMMON.mediumDrumCapacity.get(), RagiumBlockEntityTypes.MEDIUM_DRUM, pos, state)

    class Large(pos: BlockPos, state: BlockState) :
        HTDrumBlockEntity(RagiumConfig.COMMON.largeDrumCapacity.get(), RagiumBlockEntityTypes.LARGE_DRUM, pos, state)

    class Huge(pos: BlockPos, state: BlockState) :
        HTDrumBlockEntity(RagiumConfig.COMMON.hugeDrumCapacity.get(), RagiumBlockEntityTypes.HUGE_DRUM, pos, state)
}
