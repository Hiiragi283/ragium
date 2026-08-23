package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.context.ContextMap
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.display.ForFluidStacks

/**
 * [Fluid]向けの[HTIngredient]の実装クラスです。
 *
 * 参照 : [Mekanism - FluidStackIngredient](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/recipes/ingredients/FluidStackIngredient.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTFluidIngredient(val unsized: FluidIngredient, val amount: Int) :
    HTIngredient<FluidInstance>,
    HTStackPreview<FluidStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidIngredient> = HTCodecs.record { instance ->
            instance.group(
                NeoForgeExtraCodecs.aliasedFieldOf(FluidIngredient.CODEC, HTConstants.FLUIDS, HTConstants.INGREDIENT).forGetter(HTFluidIngredient::unsized),
                HTCodecs.POSITIVE_INT.fieldOf(HTConstants.AMOUNT).forGetter(HTFluidIngredient::amount),
            ).apply(instance, ::HTFluidIngredient)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidIngredient> = StreamCodec.composite(
            FluidIngredient.STREAM_CODEC,
            HTFluidIngredient::unsized,
            ByteBufCodecs.VAR_INT,
            HTFluidIngredient::amount,
            ::HTFluidIngredient,
        )
    }

    override fun test(instance: FluidInstance): Boolean = testOnlyType(instance) && instance.amount() >= amount

    override fun testOnlyType(instance: FluidInstance): Boolean = HTIngredientHelper.unwrap(instance).let(unsized::test)

    override fun getRequiredAmount(instance: FluidInstance): Int = when (testOnlyType(instance)) {
        true -> amount
        false -> 0
    }

    override fun getPreviewStacks(contextMap: ContextMap): List<FluidStack> = unsized
        .display()
        .resolve(contextMap, ForFluidStacks { it.copyWithAmount(amount) })
        .toList()
}
