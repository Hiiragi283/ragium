package hiiragi283.ragium.common.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.forOptionalGetter
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.item.Item
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryFixedCodec
import net.minecraft.world.World
import java.util.Optional

class HTRockGeneratorRecipe(definition: HTMachineDefinition, data: HTMachineRecipeData) : HTMachineRecipeBase(definition, data) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRockGeneratorRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTMachineTier.FIELD_CODEC.forGetter(HTRockGeneratorRecipe::tier),
                        RegistryFixedCodec
                            .of(RegistryKeys.ITEM)
                            .fieldOf("output")
                            .forGetter { it.data.itemResults[0].firstEntry },
                        Codec
                            .intRange(0, Int.MAX_VALUE)
                            .optionalFieldOf("count", 8)
                            .forGetter { it.data.itemResults[0].count },
                        HTFluidIngredient.CODEC
                            .optionalFieldOf("fluid_input")
                            .forOptionalGetter { it.getFluidIngredient(0) },
                    ).apply(instance, ::HTRockGeneratorRecipe)
            }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTRockGeneratorRecipe> =
            createPacketCodec(::HTRockGeneratorRecipe)
    }

    private constructor(
        tier: HTMachineTier,
        output: RegistryEntry<Item>,
        count: Int,
        fluid: Optional<HTFluidIngredient>,
    ) : this(
        HTMachineDefinition(RagiumMachineKeys.ROCK_GENERATOR, tier),
        HTMachineRecipeData(
            listOf(),
            fluid.map(::listOf).orElse(listOf()),
            HTItemIngredient.of(output),
            listOf(HTItemResult.ofItem(output, count)),
            listOf(),
        ),
    )

    override fun matches(input: HTMachineInput, world: World): Boolean {
        if (!data.isValidOutput(true)) return false
        if (input.key != this.key) return false
        if (input.tier < this.tier) return false
        return data.catalyst?.test(input.catalyst) == true
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ROCK_GENERATOR

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ROCK_GENERATOR
}
