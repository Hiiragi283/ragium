package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.serialization.network.nelOf
import hiiragi283.lib.util.Ior
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.neoforged.neoforge.fluids.FluidInstance

@JvmRecord
data class HTListItemOrFluidIngredient(val contents: Ior<Nel<HTItemIngredient>, Nel<HTFluidIngredient>>) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTListItemOrFluidIngredient> = HTCodecs.ior(
            HTItemIngredient.CODEC.compactNelFieldOf(HTConstants.ITEM, HTConstants.ITEMS, 2),
            HTFluidIngredient.CODEC.compactNelFieldOf(HTConstants.FLUID, HTConstants.FLUIDS, 2),
        ).xmap(::HTListItemOrFluidIngredient, HTListItemOrFluidIngredient::contents)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTListItemOrFluidIngredient> = HTStreamCodecs.ior(
            HTItemIngredient.STREAM_CODEC.nelOf(),
            HTFluidIngredient.STREAM_CODEC.nelOf(),
        ).map(::HTListItemOrFluidIngredient, HTListItemOrFluidIngredient::contents)
    }

    fun test(items: List<ItemInstance>, fluids: List<FluidInstance>): Boolean = contents.fold(
        { HTIngredientHelper.match(it, items) },
        { HTIngredientHelper.match(it, fluids) },
        { items1: Nel<HTItemIngredient>, fluids1: Nel<HTFluidIngredient> ->
            HTIngredientHelper.match(items1, items) && HTIngredientHelper.match(fluids1, fluids)
        },
    )

    fun getMatchingItems(items: List<ItemInstance>): List<ItemInstance> =
        contents.getLeft()?.let { HTIngredientHelper.getMatchingStacks(it, items) } ?: listOf()

    fun getMatchingFluids(fluids: List<FluidInstance>): List<FluidInstance> =
        contents.getRight()?.let { HTIngredientHelper.getMatchingStacks(it, fluids) } ?: listOf()

    fun itemDisplays(): List<SlotDisplay> = contents.getLeft()?.map(HTItemIngredient::display) ?: listOf()

    fun fluidDisplays(): List<SlotDisplay> = contents.getRight()?.map(HTFluidIngredient::display) ?: listOf()
}
