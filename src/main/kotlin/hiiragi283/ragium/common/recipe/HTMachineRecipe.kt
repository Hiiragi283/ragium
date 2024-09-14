package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.World

class HTMachineRecipe(
    val type: HTMachineType,
    val inputs: List<WeightedIngredient>,
    val outputs: List<HTRecipeResult>,
) : Recipe<HTRecipeInput> {

    companion object {
        @JvmField
        val CODEC: MapCodec<HTMachineRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                HTMachineType.CODEC.fieldOf("machine_type").forGetter(HTMachineRecipe::type),
                WeightedIngredient.CODEC.listOf().fieldOf("inputs").forGetter(HTMachineRecipe::inputs),
                HTRecipeResult.CODEC.listOf().fieldOf("outputs").forGetter(HTMachineRecipe::outputs)
            ).apply(instance, ::HTMachineRecipe)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineRecipe> = PacketCodec.tuple(
            HTMachineType.PACKET_CODEC,
            HTMachineRecipe::type,
            WeightedIngredient.LIST_PACKET_CODEC,
            HTMachineRecipe::inputs,
            HTRecipeResult.LIST_PACKET_CODEC,
            HTMachineRecipe::outputs,
            ::HTMachineRecipe
        )
    }

    override fun matches(input: HTRecipeInput, world: World): Boolean = input.matches(inputs)

    override fun craft(input: HTRecipeInput, lookup: RegistryWrapper.WrapperLookup): ItemStack = getResult(lookup)

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