package hiiragi283.ragium.api.recipe.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.HTMachineTypeRegistry
import hiiragi283.ragium.api.recipe.HTRecipeBase
import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.api.recipe.HTRequireScanRecipe
import hiiragi283.ragium.api.recipe.WeightedIngredient
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.world.World

class HTMachineRecipe(
    val type: HTMachineType.Processor,
    val minTier: HTMachineTier,
    override val requireScan: Boolean,
    override val inputs: List<WeightedIngredient>,
    override val outputs: List<HTRecipeResult>,
    val catalyst: Ingredient,
) : HTRecipeBase<HTMachineRecipe.Input>,
    HTRequireScanRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMachineRecipe> =
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        HTMachineTypeRegistry.PROCESSOR_CODEC
                            .fieldOf("machine_type")
                            .forGetter(HTMachineRecipe::type),
                        HTMachineTier.CODEC
                            .optionalFieldOf("min_tier", HTMachineTier.PRIMITIVE)
                            .forGetter(HTMachineRecipe::minTier),
                        Codec.BOOL
                            .optionalFieldOf("require_scan", false)
                            .forGetter(HTMachineRecipe::requireScan),
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
                HTMachineTypeRegistry.PROCESSOR_PACKET_CODEC,
                HTMachineRecipe::type,
                HTMachineTier.PACKET_CODEC,
                HTMachineRecipe::minTier,
                PacketCodecs.BOOL,
                HTMachineRecipe::requireScan,
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
        type,
        minTier,
        getInput(0),
        getInput(1),
        getInput(2),
        catalyst,
    )

    override fun createIcon(): ItemStack = type.getBlock(minTier)?.asItem()?.defaultStack ?: ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = Serializer

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MACHINE

    //    Serializer    //
    
    data object Serializer : RecipeSerializer<HTMachineRecipe> {
        override fun codec(): MapCodec<HTMachineRecipe> = CODEC

        override fun packetCodec(): PacketCodec<RegistryByteBuf, HTMachineRecipe> = PACKET_CODEC
    }

    //    Input    //

    class Input private constructor(
        private val currentType: HTMachineType.Processor,
        private val currentTier: HTMachineTier,
        private val first: ItemStack,
        private val second: ItemStack,
        private val third: ItemStack,
        private val catalyst: ItemStack,
    ) : RecipeInput {
        companion object {
            @JvmStatic
            fun create(
                currentType: HTMachineConvertible,
                currentTier: HTMachineTier,
                first: ItemStack,
                second: ItemStack,
                third: ItemStack,
                catalyst: ItemStack,
            ): Input = currentType
                .asProcessor()
                ?.let { Input(it, currentTier, first, second, third, catalyst) }
                ?: throw IllegalStateException("Machine Type;  ${currentType.asMachine().id} must be Processor!")
        }

        fun matches(
            type: HTMachineConvertible,
            currentTier: HTMachineTier,
            first: WeightedIngredient?,
            second: WeightedIngredient?,
            third: WeightedIngredient?,
            catalyst: Ingredient?,
        ): Boolean = matchesInternal(
            type,
            currentTier,
            first ?: WeightedIngredient.EMPTY,
            second ?: WeightedIngredient.EMPTY,
            third ?: WeightedIngredient.EMPTY,
            catalyst ?: Ingredient.EMPTY,
        )

        private fun matchesInternal(
            type: HTMachineConvertible,
            tier: HTMachineTier,
            first: WeightedIngredient,
            second: WeightedIngredient,
            third: WeightedIngredient,
            catalyst: Ingredient,
        ): Boolean = when {
            type.asMachine() != this.currentType -> false
            this.currentTier < tier -> false
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
