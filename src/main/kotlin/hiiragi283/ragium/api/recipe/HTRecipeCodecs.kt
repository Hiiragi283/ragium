package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

object HTRecipeCodecs {
    @JvmField
    val GROUP: MapCodec<String> = Codec.STRING.optionalFieldOf("group", "")

    @JvmField
    val ITEM_INPUT: MapCodec<SizedIngredient> = SizedIngredient.FLAT_CODEC.fieldOf("item_input")

    @JvmField
    val FLUID_INPUT: MapCodec<SizedFluidIngredient> = SizedFluidIngredient.FLAT_CODEC.fieldOf("fluid_input")

    @JvmField
    val ITEM_OUTPUT: MapCodec<ItemStack> = ItemStack.CODEC.fieldOf("item_output")

    @JvmField
    val FLUID_OUTPUT: MapCodec<FluidStack> = FluidStack.CODEC.fieldOf("fluid_output")
}
