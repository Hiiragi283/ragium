package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.common.machine.HTMachineType
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.World

class HTMachineRecipe(
    val type: HTMachineType,
    val inputs: List<WeightedIngredient>,
    val outputs: List<HTRecipeResult>,
    val catalyst: Ingredient,
) : Recipe<HTMachineRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMachineRecipe> =
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        HTMachineType.CODEC.fieldOf("machine_type").forGetter(HTMachineRecipe::type),
                        WeightedIngredient.CODEC
                            .listOf()
                            .fieldOf("inputs")
                            .forGetter(HTMachineRecipe::inputs),
                        HTRecipeResult.CODEC
                            .listOf()
                            .fieldOf("outputs")
                            .forGetter(HTMachineRecipe::outputs),
                        Ingredient.ALLOW_EMPTY_CODEC
                            .optionalFieldOf("catalyst", Ingredient.EMPTY)
                            .forGetter(HTMachineRecipe::catalyst),
                    ).apply(instance, ::HTMachineRecipe)
            }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineRecipe> =
            PacketCodec.tuple(
                HTMachineType.PACKET_CODEC,
                HTMachineRecipe::type,
                WeightedIngredient.LIST_PACKET_CODEC,
                HTMachineRecipe::inputs,
                HTRecipeResult.LIST_PACKET_CODEC,
                HTMachineRecipe::outputs,
                Ingredient.PACKET_CODEC,
                HTMachineRecipe::catalyst,
                ::HTMachineRecipe,
            )
    }

    override fun matches(input: HTMachineRecipeInput, world: World): Boolean = input.matches(
        inputs.getOrNull(0),
        inputs.getOrNull(1),
        inputs.getOrNull(2),
        catalyst,
    )

    override fun craft(input: HTMachineRecipeInput, lookup: RegistryWrapper.WrapperLookup): ItemStack = getResult(lookup)

    override fun fits(width: Int, height: Int): Boolean = true

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack = outputs[0].toStack()

    override fun isIgnoredInRecipeBook(): Boolean = true

    override fun createIcon(): ItemStack = ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = Serializer

    override fun getType(): RecipeType<*> = type

    object Serializer : RecipeSerializer<HTMachineRecipe> {
        override fun codec(): MapCodec<HTMachineRecipe> = CODEC

        override fun packetCodec(): PacketCodec<RegistryByteBuf, HTMachineRecipe> = PACKET_CODEC
    }
}
