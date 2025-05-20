package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

class HTFluidContent<TYPE : FluidType, STILL : Fluid, FLOW : Fluid>(
    val typeHolder: DeferredHolder<FluidType, TYPE>,
    val stillHolder: DeferredHolder<Fluid, STILL>,
    val flowHolder: DeferredHolder<Fluid, FLOW>,
    val blockHolder: DeferredBlock<*>,
    val bucketHolder: DeferredItem<*>,
) : Supplier<STILL> by stillHolder {
    val id: ResourceLocation = stillHolder.id

    val commonTag: TagKey<Fluid> = fluidTagKey(commonId(id.path))

    val bucketTag: TagKey<Item> = itemTagKey(commonId("buckets/${id.path}"))

    fun getType(): TYPE = typeHolder.get()

    fun getBlock(): Block = blockHolder.get()

    fun getBucket(): Item = bucketHolder.get()

    fun toStack(amount: Int): FluidStack = FluidStack(stillHolder, amount)
}
