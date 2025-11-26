package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.registry.impl.HTDeferredFluid
import hiiragi283.ragium.api.registry.impl.HTDeferredFluidType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredOnlyBlock
import hiiragi283.ragium.api.tag.createCommonTag
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidType

typealias HTBasicFluidContent = HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BucketItem>

/**
 * @see mekanism.common.registration.impl.FluidRegistryObject
 */
data class HTFluidContent<
    TYPE : FluidType,
    STILL : Fluid,
    FLOW : Fluid,
    BLOCK : LiquidBlock,
    BUCKET : Item,
>(
    val type: HTDeferredFluidType<TYPE>,
    val still: HTDeferredFluid<STILL>,
    val flowing: HTDeferredFluid<FLOW>,
    val commonTag: TagKey<Fluid>,
    val block: HTDeferredOnlyBlock<BLOCK>,
    val bucket: HTDeferredItem<BUCKET>,
    val bucketTag: TagKey<Item>,
) : HTFluidHolderLike {
    constructor(
        type: HTDeferredFluidType<TYPE>,
        still: HTDeferredFluid<STILL>,
        flowing: HTDeferredFluid<FLOW>,
        block: HTDeferredOnlyBlock<BLOCK>,
        bucket: HTDeferredItem<BUCKET>,
    ) : this(
        type,
        still,
        flowing,
        Registries.FLUID.createCommonTag(still.id.path),
        block,
        bucket,
        Registries.ITEM.createCommonTag("buckets", still.id.path),
    )

    fun getType(): TYPE = type.get()

    override fun getFluid(): Fluid = still.get()

    override fun getFluidTag(): TagKey<Fluid> = commonTag

    override fun getId(): ResourceLocation = still.id
}
