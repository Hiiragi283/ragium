package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.storage.fluid.HTFluidTank
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.SimpleFluidContent

abstract class HTDrumBlockEntity(
    capacity: Int,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTBlockEntity(type, pos, state) {
    private val tank = HTFluidTank(capacity, this::setChanged)

    //    Save & Load    //

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConstantValues.TANK, tank)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConstantValues.TANK, tank)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        tank.fluid = componentInput.getOrDefault(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.EMPTY).copy()
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.copyOf(tank.fluid))
    }

    override fun reloadUpgrades() {
        super.reloadUpgrades()
        tank
    }

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler = HTFilteredFluidHandler(
        listOf(tank),
        HTFluidFilter.ALWAYS,
    )

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
