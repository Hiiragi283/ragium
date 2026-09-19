package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.storage.base.HTBaseTankBlockEntity
import hiiragi283.ragium.common.transfer.fluid.HTCreativeFluidTank
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.transfer.fluid.FluidResource

class HTCreativeTankBlockEntity(pos: BlockPos, state: BlockState) :
    HTBaseTankBlockEntity(RagiumBlockEntityTypes.CREATIVE_TANK.get(), pos, state) {
    override val tank: HTCreativeFluidTank = HTCreativeFluidTank()

    override fun initTank(listener: Runnable) {}

    //    Sync    //

    override fun applyImplicitComponents(components: DataComponentGetter) {
        super.applyImplicitComponents(components)
        components.get(RagiumDataComponents.FLUID)
            ?.copy()
            ?.let(FluidResource::of)
            ?.let(tank::resource::set)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        val content: SimpleFluidContent = tank.resource.toStack(1).let(SimpleFluidContent::copyOf)
        if (!content.isEmpty) {
            components.set(RagiumDataComponents.FLUID, content)
        }
    }
}
