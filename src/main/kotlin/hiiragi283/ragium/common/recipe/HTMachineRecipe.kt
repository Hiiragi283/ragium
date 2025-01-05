package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.forOptionalGetter
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.world.World
import java.util.*
import kotlin.jvm.optionals.getOrNull

class HTMachineRecipe(
    definition: HTMachineDefinition,
    override val itemIngredients: List<HTItemIngredient>,
    override val fluidIngredients: List<HTFluidIngredient>,
    override val catalyst: HTItemIngredient?,
    override val itemResults: List<HTItemResult>,
    override val fluidResults: List<HTFluidResult>,
) : HTMachineRecipeBase(definition) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMachineRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMachineDefinition.Companion.CODEC
                        .fieldOf("definition")
                        .forGetter(HTMachineRecipe::definition),
                    HTItemIngredient.Companion.CODEC
                        .listOf()
                        .optionalFieldOf("item_inputs", listOf())
                        .forGetter(HTMachineRecipe::itemIngredients),
                    HTFluidIngredient.Companion.CODEC
                        .listOf()
                        .optionalFieldOf("fluid_inputs", listOf())
                        .forGetter(HTMachineRecipe::fluidIngredients),
                    HTItemIngredient.Companion.CODEC
                        .optionalFieldOf("catalyst")
                        .forOptionalGetter(HTMachineRecipe::catalyst),
                    HTItemResult.Companion.CODEC
                        .listOf()
                        .optionalFieldOf("item_outputs", listOf())
                        .forGetter(HTMachineRecipe::itemResults),
                    HTFluidResult.Companion.CODEC
                        .listOf()
                        .optionalFieldOf("fluid_outputs", listOf())
                        .forGetter(HTMachineRecipe::fluidResults),
                ).apply(instance, ::HTMachineRecipe)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineRecipe> = PacketCodec.tuple(
            HTMachineDefinition.PACKET_CODEC,
            HTMachineRecipe::definition,
            HTItemIngredient.PACKET_CODEC.toList(),
            HTMachineRecipe::itemIngredients,
            HTFluidIngredient.PACKET_CODEC.toList(),
            HTMachineRecipe::fluidIngredients,
            PacketCodecs.optional(HTItemIngredient.PACKET_CODEC),
            { Optional.ofNullable(it.catalyst) },
            HTItemResult.PACKET_CODEC.toList(),
            HTMachineRecipe::itemResults,
            HTFluidResult.PACKET_CODEC.toList(),
            HTMachineRecipe::fluidResults,
            ::HTMachineRecipe,
        )
    }

    constructor(
        definition: HTMachineDefinition,
        itemInputs: List<HTItemIngredient>,
        fluidInputs: List<HTFluidIngredient>,
        catalyst: Optional<HTItemIngredient>,
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

    //    HTMachineRecipeBase    //

    override fun matches(input: HTMachineInput, world: World): Boolean {
        if (input.key != this.key) return false
        if (input.tier < this.tier) return false
        if (!HTShapelessInputResolver.canMatch(itemIngredients, input.itemInputs)) return false
        fluidIngredients.forEachIndexed { index: Int, fluid: HTFluidIngredient ->
            if (!fluid.test(input.getFluidInSlot(index))) {
                return false
            }
        }
        return catalyst?.test(input.catalyst) ?: input.catalyst.isEmpty
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MACHINE

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MACHINE
}
