package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.common.machine.HTMachineType
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.world.World

class HTMachineRecipe(
    val type: HTMachineType,
    override val inputs: List<WeightedIngredient>,
    override val outputs: List<HTRecipeResult>,
    val catalyst: Ingredient,
) : HTRecipeBase<HTMachineRecipe.Input> {
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

    //    Recipe    //

    override fun matches(input: Input, world: World): Boolean = input.matches(
        getInput(0),
        getInput(1),
        getInput(2),
        catalyst,
    )

    override fun createIcon(): ItemStack = type.block.asItem().defaultStack

    override fun getSerializer(): RecipeSerializer<*> = Serializer

    override fun getType(): RecipeType<*> = type

    object Serializer : RecipeSerializer<HTMachineRecipe> {
        override fun codec(): MapCodec<HTMachineRecipe> = CODEC

        override fun packetCodec(): PacketCodec<RegistryByteBuf, HTMachineRecipe> = PACKET_CODEC
    }

    //    Input    //

    class Input(
        private val first: ItemStack,
        private val second: ItemStack,
        private val third: ItemStack,
        private val catalyst: ItemStack,
    ) : RecipeInput {
        fun matches(
            first: WeightedIngredient?,
            second: WeightedIngredient?,
            third: WeightedIngredient?,
            catalyst: Ingredient?,
        ): Boolean = matchesInternal(
            first ?: WeightedIngredient.EMPTY,
            second ?: WeightedIngredient.EMPTY,
            third ?: WeightedIngredient.EMPTY,
            catalyst ?: Ingredient.EMPTY,
        )

        private fun matchesInternal(
            first: WeightedIngredient,
            second: WeightedIngredient,
            third: WeightedIngredient,
            catalyst: Ingredient,
        ): Boolean = when {
            !first.test(this.first) -> false
            !second.test(this.second) -> false
            !third.test(this.third) -> false
            !catalyst.test(this.catalyst) -> false
            else -> true
        }

        //    RecipeInput    //

        override fun getStackInSlot(slot: Int): ItemStack = when (slot) {
            0 -> first
            1 -> second
            2 -> third
            3 -> catalyst
            else -> throw IndexOutOfBoundsException()
        }

        override fun getSize(): Int = 4
    }
}
