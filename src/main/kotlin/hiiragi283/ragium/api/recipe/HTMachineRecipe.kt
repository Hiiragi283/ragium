package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.World
import java.util.*
import kotlin.jvm.optionals.getOrNull

class HTMachineRecipe(
    val definition: HTMachineDefinition,
    val itemInputs: List<HTIngredient.Item>,
    val fluidInputs: List<HTIngredient.Fluid>,
    val catalyst: HTIngredient.Item?,
    val itemOutputs: List<HTItemResult>,
    val fluidOutputs: List<HTFluidResult>,
) : Recipe<HTMachineInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMachineRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMachineDefinition.CODEC
                        .fieldOf("definition")
                        .forGetter(HTMachineRecipe::definition),
                    HTIngredient.ITEM_CODEC
                        .listOf()
                        .optionalFieldOf("item_inputs", listOf())
                        .forGetter(HTMachineRecipe::itemInputs),
                    HTIngredient.FLUID_CODEC
                        .listOf()
                        .optionalFieldOf("fluid_inputs", listOf())
                        .forGetter(HTMachineRecipe::fluidInputs),
                    HTIngredient.ITEM_CODEC
                        .optionalFieldOf("catalyst")
                        .forGetter { Optional.ofNullable(it.catalyst) },
                    HTItemResult.CODEC
                        .listOf()
                        .optionalFieldOf("item_outputs", listOf())
                        .forGetter(HTMachineRecipe::itemOutputs),
                    HTFluidResult.CODEC
                        .listOf()
                        .optionalFieldOf("fluid_outputs", listOf())
                        .forGetter(HTMachineRecipe::fluidOutputs),
                ).apply(instance, ::HTMachineRecipe)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineRecipe> = PacketCodec.tuple(
            HTMachineDefinition.PACKET_CODEC,
            HTMachineRecipe::definition,
            HTIngredient.ITEM_PACKET_CODEC.toList(),
            HTMachineRecipe::itemInputs,
            HTIngredient.FLUID_PACKET_CODEC.toList(),
            HTMachineRecipe::fluidInputs,
            PacketCodecs.optional(HTIngredient.ITEM_PACKET_CODEC),
            { Optional.ofNullable(it.catalyst) },
            HTItemResult.PACKET_CODEC.toList(),
            HTMachineRecipe::itemOutputs,
            HTFluidResult.PACKET_CODEC.toList(),
            HTMachineRecipe::fluidOutputs,
            ::HTMachineRecipe,
        )
    }

    constructor(
        definition: HTMachineDefinition,
        itemInputs: List<HTIngredient.Item>,
        fluidInputs: List<HTIngredient.Fluid>,
        catalyst: Optional<HTIngredient.Item>,
        itemOutputs: List<HTItemResult>,
        fluidOutputs: List<HTFluidResult>,
    ) : this(
        definition,
        itemInputs,
        fluidInputs,
        catalyst.getOrNull(),
        itemOutputs,
        fluidOutputs,
    )

    val key: HTMachineKey
        get() = definition.key
    val tier: HTMachineTier
        get() = definition.tier
    val firstOutput: ItemStack
        get() = itemOutputs.getOrNull(0)?.stack ?: ItemStack.EMPTY

    //    Recipe    //

    override fun matches(input: HTMachineInput, world: World): Boolean {
        if (input.key != this.key) return false
        if (input.tier < this.tier) return false
        itemInputs.forEachIndexed { index: Int, item: HTIngredient.Item ->
            if (!item.test(input.getItem(index))) {
                return false
            }
        }
        fluidInputs.forEachIndexed { index: Int, fluid: HTIngredient.Fluid ->
            if (!fluid.test(input.getFluid(index))) {
                return false
            }
        }
        return catalyst?.test(input.catalyst) ?: input.catalyst.isEmpty
    }

    override fun craft(input: HTMachineInput, lookup: RegistryWrapper.WrapperLookup): ItemStack = getResult(lookup)

    override fun fits(width: Int, height: Int): Boolean = true

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack = firstOutput

    override fun isIgnoredInRecipeBook(): Boolean = true

    override fun showNotification(): Boolean = false

    override fun createIcon(): ItemStack = definition.iconStack

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MACHINE

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MACHINE

    override fun isEmpty(): Boolean = true
}
