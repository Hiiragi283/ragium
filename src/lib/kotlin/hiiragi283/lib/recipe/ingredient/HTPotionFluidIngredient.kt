package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.fluid.FluidStack
import hiiragi283.lib.item.alchemy.HTPotionFluidAccess
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.util.fold
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType
import net.neoforged.neoforge.fluids.crafting.display.FluidStackSlotDisplay
import java.util.stream.Stream

/**
 * 液体ポーションを扱う[FluidIngredient]の実装クラスです。
 * @param potions 対象となるポーションの一覧
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data class HTPotionFluidIngredient(val potions: HolderSet<Potion>) : FluidIngredient() {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTPotionFluidIngredient> = HTCodecs.holderSet(Registries.POTION)
            .fieldOf(HTConstants.POTIONS)
            .xmap(::HTPotionFluidIngredient, HTPotionFluidIngredient::potions)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTPotionFluidIngredient> =
            HTStreamCodecs.holderSet(Registries.POTION).map(::HTPotionFluidIngredient, HTPotionFluidIngredient::potions)

        @JvmField
        val TYPE: FluidIngredientType<HTPotionFluidIngredient> = FluidIngredientType(CODEC, STREAM_CODEC)
    }

    constructor(potion: Holder<Potion>) : this(HolderSet.direct(potion))

    override fun test(fluidStack: FluidStack): Boolean =
        HTPotionHelper.getContents(fluidStack).potion().fold({ false }, potions::contains)

    @Suppress("DEPRECATION")
    override fun generateFluids(): Stream<Holder<Fluid>> =
        Stream.of(HTPotionFluidAccess.INSTANCE.fluidContent.sourceHolder)

    override fun display(): SlotDisplay = potions
        .stream()
        .filter { it.value().isEnabled(HTPhysicalSideHelper.getFeatureFlags()) }
        .map { potion: Holder<Potion> ->
            when (potion) {
                Potions.WATER -> FluidStack(Fluids.WATER)
                else -> HTPotionHelper.createFluid(potion)
            }
        }.map(::FluidStackSlotDisplay)
        .toList()
        .let(SlotDisplay::Composite)

    override fun isSimple(): Boolean = false

    override fun getType(): FluidIngredientType<*> = TYPE
}
