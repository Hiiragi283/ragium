package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.forOptionalGetter
import hiiragi283.ragium.api.extension.validate
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.world.World
import java.util.*

class HTAssemblerRecipe(definition: HTMachineDefinition, data: HTMachineRecipeData) : HTMachineRecipe(definition, data) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTAssemblerRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTMachineTier.FIELD_CODEC.forGetter(HTAssemblerRecipe::tier),
                        HTItemIngredient.CODEC
                            .listOf()
                            .fieldOf("inputs")
                            .forGetter { it.data.itemIngredients },
                        HTItemIngredient.CODEC.optionalFieldOf("catalyst").forOptionalGetter { it.data.catalyst },
                        HTItemResult.CODEC.fieldOf("output").forGetter { it.data.itemResults[0] },
                    ).apply(instance, ::HTAssemblerRecipe)
            }.validate { recipe: HTAssemblerRecipe ->
                DataResult.success(recipe).validate(
                    { recipe.data.itemIngredients.isNotEmpty() },
                    { "Empty inputs is not allowed!" },
                )
            }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTAssemblerRecipe> = createPacketCodec(::HTAssemblerRecipe)
    }

    private constructor(
        tier: HTMachineTier,
        ingredients: List<HTItemIngredient>,
        catalyst: Optional<HTItemIngredient>,
        output: HTItemResult,
    ) : this(
        HTMachineDefinition(RagiumMachineKeys.ASSEMBLER, tier),
        HTMachineRecipeData(
            ingredients,
            listOf(),
            catalyst,
            listOf(output),
            listOf(),
        ),
    )

    override fun matches(input: HTMachineInput, world: World): Boolean {
        if (!checkDefinition(input)) return false
        if (!HTShapelessInputResolver.canMatch(data.itemIngredients, input.itemInputs)) return false
        return data.catalyst?.test(input.catalyst) ?: input.catalyst.isEmpty
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ASSEMBLER

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ASSEMBLER
}
