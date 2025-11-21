package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * [Fluid]とその[TagKey]を保持する[HTHolderLike]の拡張インターフェース
 */
interface HTFluidHolderLike : HTHolderLike {
    companion object {
        @JvmField
        val WATER: HTFluidHolderLike = fromFluid(Fluids.WATER, Tags.Fluids.WATER)

        @JvmField
        val LAVA: HTFluidHolderLike = fromFluid(Fluids.LAVA, Tags.Fluids.LAVA)

        @JvmField
        val MILK: HTFluidHolderLike = fromHolder(NeoForgeMod.MILK, Tags.Fluids.MILK)

        @Suppress("DEPRECATION")
        @JvmStatic
        fun fromFluid(fluid: Fluid, tagKey: TagKey<Fluid>): HTFluidHolderLike = object : HTFluidHolderLike {
            override fun getFluid(): Fluid = fluid

            override fun getFluidTag(): TagKey<Fluid> = tagKey

            override fun getId(): ResourceLocation = fluid.builtInRegistryHolder().idOrThrow
        }

        @JvmStatic
        fun fromHolder(holder: DeferredHolder<Fluid, *>, tagKey: TagKey<Fluid>): HTFluidHolderLike = object : HTFluidHolderLike {
            override fun getFluid(): Fluid = holder.get()

            override fun getFluidTag(): TagKey<Fluid> = tagKey

            override fun getId(): ResourceLocation = holder.id
        }
    }

    /**
     * 保持している液体を返します。
     */
    fun getFluid(): Fluid

    /**
     * 保持している[TagKey]を返します。
     */
    fun getFluidTag(): TagKey<Fluid>

    fun isOf(stack: FluidStack): Boolean = stack.`is`(getFluidTag())

    fun isOf(holder: Holder<Fluid>): Boolean = holder.`is`(getFluidTag())

    fun isOf(stack: ImmutableFluidStack?): Boolean = stack?.isOf(getFluidTag()) ?: false

    fun toStack(amount: Int): FluidStack = FluidStack(getFluid(), amount)

    fun toImmutableStack(amount: Int): ImmutableFluidStack? = toStack(amount).toImmutable()
}
