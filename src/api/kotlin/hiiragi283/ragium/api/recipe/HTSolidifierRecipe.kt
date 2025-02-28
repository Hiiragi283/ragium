package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toOptional
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTSolidifierRecipe(
    group: String,
    val input: SizedFluidIngredient,
    val catalyst: Optional<Ingredient>,
    val itemOutput: HTItemOutput,
) : HTMachineRecipe(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTSolidifierRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTRecipeCodecs.FLUID_INPUT.fieldOf("fluid_input").forGetter(HTSolidifierRecipe::input),
                    HTRecipeCodecs.CATALYST.forGetter(HTSolidifierRecipe::catalyst),
                    HTRecipeCodecs.ITEM_OUTPUT.forGetter(HTSolidifierRecipe::itemOutput),
                ).apply(instance, ::HTSolidifierRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSolidifierRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTSolidifierRecipe::getGroup,
            SizedFluidIngredient.STREAM_CODEC,
            HTSolidifierRecipe::input,
            Ingredient.CONTENTS_STREAM_CODEC.toOptional(),
            HTSolidifierRecipe::catalyst,
            HTItemOutput.STREAM_CODEC,
            HTSolidifierRecipe::itemOutput,
            ::HTSolidifierRecipe,
        )
    }

    override fun matches(context: HTMachineRecipeContext): Boolean {
        if (!this.input.test(context.getFluidStack(HTStorageIO.INPUT, 0))) return false
        val catalystItem: ItemStack = context.getItemStack(HTStorageIO.INPUT, 0)
        return catalyst.map { it.test(catalystItem) }.orElse(catalystItem.isEmpty)
    }

    override fun canProcess(context: HTMachineRecipeContext): Result<Unit> = runCatching {
        // Output
        if (!context.getSlot(HTStorageIO.OUTPUT, 0).canInsert(itemOutput.get())) {
            throw HTMachineException.GrowItem()
        }
        // Input
        if (!context.getTank(HTStorageIO.INPUT, 0).canShrink(input.amount())) {
            throw HTMachineException.ShrinkFluid()
        }
    }

    override fun process(context: HTMachineRecipeContext) {
        // Output
        context.getSlot(HTStorageIO.OUTPUT, 0).insertItem(itemOutput.get(), false)
        // Input
        context.getTank(HTStorageIO.INPUT, 0).shrinkStack(input.amount(), false)
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.SOLIDIFIER
}
