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
    override val definition: HTMachineDefinition,
    override val itemIngredients: List<HTItemIngredient>,
    private val fluidInputs: List<HTFluidIngredient>,
    override val catalyst: HTItemIngredient?,
    private val itemOutputs: List<HTItemResult>,
    private val fluidOutputs: List<HTFluidResult>,
) : HTMachineRecipeBase {
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
                        .forGetter(HTMachineRecipe::fluidInputs),
                    HTItemIngredient.Companion.CODEC
                        .optionalFieldOf("catalyst")
                        .forOptionalGetter(HTMachineRecipe::catalyst),
                    HTItemResult.Companion.CODEC
                        .listOf()
                        .optionalFieldOf("item_outputs", listOf())
                        .forGetter(HTMachineRecipe::itemOutputs),
                    HTFluidResult.Companion.CODEC
                        .listOf()
                        .optionalFieldOf("fluid_outputs", listOf())
                        .forGetter(HTMachineRecipe::fluidOutputs),
                ).apply(instance, ::HTMachineRecipe)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineRecipe> = PacketCodec.tuple(
            HTMachineDefinition.Companion.PACKET_CODEC,
            HTMachineRecipe::definition,
            HTItemIngredient.Companion.PACKET_CODEC.toList(),
            HTMachineRecipe::itemIngredients,
            HTFluidIngredient.Companion.PACKET_CODEC.toList(),
            HTMachineRecipe::fluidInputs,
            PacketCodecs.optional(HTItemIngredient.Companion.PACKET_CODEC),
            { Optional.ofNullable(it.catalyst) },
            HTItemResult.Companion.PACKET_CODEC.toList(),
            HTMachineRecipe::itemOutputs,
            HTFluidResult.Companion.PACKET_CODEC.toList(),
            HTMachineRecipe::fluidOutputs,
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

    override fun getFluidIngredient(index: Int): HTFluidIngredient? = fluidInputs.getOrNull(index)

    override fun getItemResult(index: Int): HTItemResult? = itemOutputs.getOrNull(index)

    override fun getFluidResult(index: Int): HTFluidResult? = fluidOutputs.getOrNull(index)

    override fun matches(input: HTMachineInput, world: World): Boolean {
        if (input.key != this.key) return false
        if (input.tier < this.tier) return false
        if (!HTShapelessInputResolver.canMatch(itemIngredients, input.itemInputs)) return false
        fluidInputs.forEachIndexed { index: Int, fluid: HTFluidIngredient ->
            if (!fluid.test(input.getFluid(index))) {
                return false
            }
        }
        return catalyst?.test(input.catalyst) ?: input.catalyst.isEmpty
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MACHINE

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MACHINE
}
