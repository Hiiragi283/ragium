package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.forOptionalGetter
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

class HTCompressorMachineRecipe(definition: HTMachineDefinition, data: HTMachineRecipeData) : HTMachineRecipe(definition, data) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCompressorMachineRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTMachineTier.FIELD_CODEC.forGetter(HTCompressorMachineRecipe::tier),
                        HTItemIngredient.CODEC.fieldOf("input").forGetter { it.data.itemIngredients[0] },
                        HTItemIngredient.CODEC.optionalFieldOf("catalyst").forOptionalGetter { it.data.catalyst },
                        HTItemResult.CODEC.fieldOf("output").forGetter { it.data.itemResults[0] },
                    ).apply(instance, ::HTCompressorMachineRecipe)
            }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTCompressorMachineRecipe> =
            createPacketCodec(::HTCompressorMachineRecipe)
    }

    private constructor(
        tier: HTMachineTier,
        ingredient: HTItemIngredient,
        catalyst: Optional<HTItemIngredient>,
        output: HTItemResult,
    ) : this(
        HTMachineDefinition(RagiumMachineKeys.COMPRESSOR, tier),
        HTMachineRecipeData(
            listOf(ingredient),
            listOf(),
            catalyst,
            listOf(output),
            listOf(),
        ),
    )

    override fun matches(input: HTMachineInput, world: World): Boolean {
        if (!checkDefinition(input)) return false
        if (!data.itemIngredients[0].test(input.getStackInSlot(0))) return false
        return data.catalyst?.test(input.catalyst) ?: input.catalyst.isEmpty
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.COMPRESSOR

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.COMPRESSOR
}
