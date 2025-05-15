package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.HTDefinitionRecipe
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTBeehiveRecipe(private val ingredient: SizedIngredient, private val catalyst: Ingredient, private val output: HTItemOutput) :
    HTMachineRecipe(),
    HTDefinitionRecipe<HTMachineInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTBeehiveRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    SizedIngredient.FLAT_CODEC.fieldOf("input").forGetter(HTBeehiveRecipe::ingredient),
                    Ingredient.CODEC.optionalFieldOf("catalyst", Ingredient.EMPTY).forGetter(HTBeehiveRecipe::catalyst),
                    HTItemOutput.CODEC.fieldOf("output").forGetter(HTBeehiveRecipe::output),
                ).apply(instance, ::HTBeehiveRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTBeehiveRecipe> = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            HTBeehiveRecipe::ingredient,
            Ingredient.CONTENTS_STREAM_CODEC,
            HTBeehiveRecipe::catalyst,
            HTItemOutput.STREAM_CODEC,
            HTBeehiveRecipe::output,
            ::HTBeehiveRecipe,
        )
    }

    override fun matches(input: HTMachineInput): Boolean {
        val bool1: Boolean = ingredient.test(input.getItemStack(HTStorageIO.INPUT, 0))
        val catalystStack: ItemStack = input.getItemStack(HTStorageIO.INPUT, 0)
        val bool2: Boolean = when {
            catalyst.isEmpty -> catalystStack.isEmpty
            else -> catalyst.test(catalystStack)
        }
        return bool1 && bool2
    }

    override fun canProcess(input: HTMachineInput): Boolean {
        TODO("Not yet implemented")
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BEE_HIVE.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.BEE_HIVE.get()

    override fun getDefinition(): DataResult<HTRecipeDefinition> = DataResult.success(
        HTRecipeDefinition(
            listOf(ingredient),
            listOf(),
            catalyst,
            listOf(output),
            listOf(),
        ),
    )
}
