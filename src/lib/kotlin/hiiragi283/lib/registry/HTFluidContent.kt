package hiiragi283.lib.registry

import hiiragi283.lib.resource.SimpleSupplierWithKey
import hiiragi283.lib.util.getOrThrow
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType

/**
 * 液体に関する要素を束ねたクラスです。
 * @param typeHolder 液体の種類の[HTDeferredHolder]
 * @param sourceHolder 液体源の[HTDeferredHolder]
 * @param bucketHolder 液体入りバケツの[HTDeferredHolder]
 * @param fluidTag 液体の共通タグ
 * @param bucketTag 液体入りバケツの共通タグ
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
sealed class HTFluidContent(
    val typeHolder: HTDeferredFluidType<FluidType>,
    val sourceHolder: HTDeferredHolder<Fluid, *>,
    val bucketHolder: HTSimpleDeferredItem,
    val fluidTag: TagKey<Fluid>,
    val bucketTag: TagKey<Item>,
) : SimpleSupplierWithKey<Fluid> by sourceHolder {
    /**
     * 液体の種類を取得します。
     */
    fun getFluidType(): FluidType = typeHolder.get()

    /**
     * 新しい[FluidStackTemplate]のインスタンスを作成します。
     */
    fun toTemplate(amount: Int = FluidType.BUCKET_VOLUME, patch: DataComponentPatch = DataComponentPatch.EMPTY): FluidStackTemplate = sourceHolder.getResult().map { FluidStackTemplate(it, amount, patch) }.getOrThrow()

    /**
     * 新しい[FluidStack]のインスタンスを作成します。
     */
    fun toStack(amount: Int = FluidType.BUCKET_VOLUME, patch: DataComponentPatch = DataComponentPatch.EMPTY): FluidStack = when {
        sourceHolder.isBound -> FluidStack(sourceHolder, amount, patch)
        else -> FluidStack.EMPTY
    }

    /**
     * 基本的な[HTFluidContent]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    class Virtual(
        typeHolder: HTDeferredFluidType<FluidType>,
        sourceHolder: HTDeferredHolder<Fluid, *>,
        bucketHolder: HTSimpleDeferredItem,
        fluidTag: TagKey<Fluid>,
        bucketTag: TagKey<Item>,
    ) : HTFluidContent(typeHolder, sourceHolder, bucketHolder, fluidTag, bucketTag)

    /**
     * [FlowingFluid]に基づいた[HTFluidContent]の実装クラスです。
     * @param flowingHolder 液体流の[HTDeferredHolder]
     * @param blockHolder 液体ブロックの[HTDeferredHolder]
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    class Flowing(
        typeHolder: HTDeferredFluidType<FluidType>,
        sourceHolder: HTDeferredHolder<Fluid, FlowingFluid>,
        bucketHolder: HTSimpleDeferredItem,
        fluidTag: TagKey<Fluid>,
        bucketTag: TagKey<Item>,
        val flowingHolder: HTDeferredHolder<Fluid, FlowingFluid>,
        val blockHolder: HTDeferredBlock<LiquidBlock>?,
    ) : HTFluidContent(typeHolder, sourceHolder, bucketHolder, fluidTag, bucketTag)
}
