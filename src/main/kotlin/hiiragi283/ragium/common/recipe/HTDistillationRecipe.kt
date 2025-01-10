package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
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

class HTDistillationRecipe(definition: HTMachineDefinition, data: HTMachineRecipeData) : HTMachineRecipe(definition, data) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTDistillationRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTMachineTier.FIELD_CODEC.forGetter(HTDistillationRecipe::tier),
                        HTFluidIngredient.CODEC.fieldOf("input").forGetter { it.data.fluidIngredients[0] },
                        HTItemIngredient.CODEC.fieldOf("catalyst").forGetter { it.data.catalyst },
                        HTFluidResult.CODEC
                            .listOf()
                            .fieldOf("outputs")
                            .forGetter { it.data.fluidResults },
                    ).apply(instance, ::HTDistillationRecipe)
            }.validate { recipe: HTDistillationRecipe ->
                DataResult.success(recipe).validate(
                    { recipe.data.fluidResults.isNotEmpty() },
                    { "Empty outputs is not allowed!" },
                )
            }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTDistillationRecipe> = createPacketCodec(::HTDistillationRecipe)
    }

    private constructor(
        tier: HTMachineTier,
        ingredient: HTFluidIngredient,
        catalyst: HTItemIngredient,
        outputs: List<HTFluidResult>,
    ) : this(
        HTMachineDefinition(RagiumMachineKeys.DISTILLATION_TOWER, tier),
        HTMachineRecipeData(
            listOf(),
            listOf(ingredient),
            catalyst,
            listOf(),
            outputs,
        ),
    )

    override fun matches(input: HTMachineInput, world: World): Boolean {
        if (!data.isValidOutput(true)) return false
        if (input.key != this.key) return false
        if (input.tier < this.tier) return false
        data.fluidIngredients.forEachIndexed { index: Int, fluid: HTFluidIngredient ->
            if (!fluid.test(input.getFluidInSlot(index))) {
                return false
            }
        }
        return data.catalyst?.test(input.catalyst) == true
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.DISTILLATION

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.DISTILLATION
}
