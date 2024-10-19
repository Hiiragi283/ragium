package hiiragi283.ragium.api.recipe.machine

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.recipe.HTIngredient
import hiiragi283.ragium.api.recipe.HTRecipeBase
import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.api.recipe.HTRequireScanRecipe
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.component.ComponentChanges
import net.minecraft.component.ComponentHolder
import net.minecraft.component.ComponentMap
import net.minecraft.component.ComponentMapImpl
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
    val minTier: HTMachineTier,
    override val inputs: List<HTIngredient>,
    override val outputs: List<HTRecipeResult>,
    val catalyst: Ingredient,
    private val customData: ComponentChanges,
) : HTRecipeBase<HTMachineRecipe.Input>,
    HTRequireScanRecipe,
    ComponentHolder {
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
                        HTIngredient.CODEC
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
                        ComponentChanges.CODEC
                            .optionalFieldOf("custom_data", ComponentChanges.EMPTY)
                            .forGetter(HTMachineRecipe::customData),
                    ).apply(instance, ::HTMachineRecipe)
            }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineRecipe> =
            PacketCodec.tuple(
                HTMachineTypeRegistry.PACKET_CODEC,
                HTMachineRecipe::type,
                HTMachineTier.PACKET_CODEC,
                HTMachineRecipe::minTier,
                HTIngredient.LIST_PACKET_CODEC,
                HTMachineRecipe::inputs,
                HTRecipeResult.LIST_PACKET_CODEC,
                HTMachineRecipe::outputs,
                Ingredient.PACKET_CODEC,
                HTMachineRecipe::catalyst,
                ComponentChanges.PACKET_CODEC,
                HTMachineRecipe::customData,
                ::HTMachineRecipe,
            )
    }

    @JvmField
    val componentMap: ComponentMap = ComponentMapImpl.create(ComponentMap.EMPTY, customData)

    override val requireScan: Boolean = getOrDefault(HTRecipeComponentTypes.REQUIRE_SCAN, false)

    //    Recipe    //

    override fun matches(input: Input, world: World): Boolean = input.matches(
        type,
        minTier,
        getInput(0),
        getInput(1),
        getInput(2),
        catalyst,
        components,
    )

    override fun createIcon(): ItemStack = type.createItemStack(minTier)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MACHINE

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MACHINE

    //    ComponentHolder    //

    override fun getComponents(): ComponentMap = componentMap

    //    Input    //

    class Input private constructor(
        val currentType: HTMachineType,
        val currentTier: HTMachineTier,
        private val first: ItemStack,
        private val second: ItemStack,
        private val third: ItemStack,
        private val catalyst: ItemStack,
        private val customData: ComponentMap,
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
                customData: ComponentMap,
            ): Input = currentType
                .asProcessor()
                ?.let { Input(it, currentTier, first, second, third, catalyst, customData) }
                ?: throw IllegalStateException("Machine Type;  ${currentType.asMachine().id} must be Processor!")
        }

        fun matches(
            type: HTMachineConvertible,
            currentTier: HTMachineTier,
            first: HTIngredient?,
            second: HTIngredient?,
            third: HTIngredient?,
            catalyst: Ingredient?,
            customData: ComponentMap,
        ): Boolean = matchesInternal(
            type,
            currentTier,
            first ?: HTIngredient.EMPTY,
            second ?: HTIngredient.EMPTY,
            third ?: HTIngredient.EMPTY,
            catalyst ?: Ingredient.EMPTY,
            customData,
        )

        private fun matchesInternal(
            type: HTMachineConvertible,
            tier: HTMachineTier,
            first: HTIngredient,
            second: HTIngredient,
            third: HTIngredient,
            catalyst: Ingredient,
            customData: ComponentMap,
        ): Boolean = when {
            type.asMachine() != this.currentType -> false
            this.currentTier < tier -> false
            !first.test(this.first) -> false
            !second.test(this.second) -> false
            !third.test(this.third) -> false
            !catalyst.test(this.catalyst) -> false
            !this.currentType.getOrDefault(HTMachinePropertyKeys.ADDITIONAL_RECIPE_MATCHER)
                (this.customData, customData) -> false
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

        override fun isEmpty(): Boolean = super.isEmpty() && customData.isEmpty
    }
}
