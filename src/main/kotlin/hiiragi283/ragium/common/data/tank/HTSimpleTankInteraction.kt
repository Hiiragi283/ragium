package hiiragi283.ragium.common.data.tank

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.HTSimpleFluidHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.data.tank.HTTankInteraction
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class HTSimpleTankInteraction(
    val emptyContainer: HTSimpleItemHolderLike,
    val filledContainer: HTSimpleItemHolderLike,
    val fluid: HTSimpleFluidHolderLike,
    override val amount: Int,
    val fluidTag: Optional<TagKey<Fluid>>,
) : HTTankInteraction.Serializable {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTSimpleTankInteraction> = MapBiCodec
            .composite(
                HTItemHolderLike.CODEC.fieldOf("empty_container").forGetter(HTSimpleTankInteraction::emptyContainer),
                HTItemHolderLike.CODEC.fieldOf("filled_container").forGetter(HTSimpleTankInteraction::filledContainer),
                HTFluidHolderLike.CODEC.fieldOf(HTConst.FLUID).forGetter(HTSimpleTankInteraction::fluid),
                BiCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.AMOUNT).forGetter(HTSimpleTankInteraction::amount),
                VanillaBiCodecs.tagKey(Registries.FLUID, true).optionalFieldOf("fluid_tag").forGetter(HTSimpleTankInteraction::fluidTag),
                ::HTSimpleTankInteraction,
            ).codec
    }

    private val ingredient: HTFluidIngredient = listOfNotNull(
        FluidIngredient.of(fluid.get()),
        fluidTag.map(FluidIngredient::tag).getOrNull(),
    ).let { CompoundFluidIngredient.of(it) }
        .let { HTFluidIngredient(it, amount) }

    override fun type(): MapCodec<HTSimpleTankInteraction> = CODEC

    override fun canEmptyContainer(container: ItemStack): Boolean = filledContainer.isOf(container)

    override fun emptyContainer(container: ItemStack): Pair<ItemStack, FluidStack> = emptyContainer.toStack() to fluid.toStack(amount)

    override fun canFillContainer(container: ItemStack, fluidStack: FluidStack): Boolean =
        emptyContainer.isOf(container) && ingredient.test(fluidStack)

    override fun fillContainer(container: ItemStack, fluidStack: FluidStack): ItemStack = filledContainer.toStack()
}
