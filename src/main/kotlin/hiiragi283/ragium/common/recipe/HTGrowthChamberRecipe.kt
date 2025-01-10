package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
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

class HTGrowthChamberRecipe(definition: HTMachineDefinition, data: HTMachineRecipeData) : HTMachineRecipe(definition, data) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTGrowthChamberRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTMachineTier.FIELD_CODEC.forGetter(HTGrowthChamberRecipe::tier),
                        RegistryFixedCodec
                            .of(RegistryKeys.ITEM)
                            .fieldOf("seed")
                            .forGetter { it.data.itemResults[1].firstEntry },
                        HTFluidIngredient.CODEC.fieldOf("fluid").forGetter { it.data.fluidIngredients[0] },
                        HTItemIngredient.CODEC.fieldOf("soil").forGetter { it.data.catalyst },
                        HTItemResult.CODEC.fieldOf("crops").forGetter { it.data.itemResults[0] },
                    ).apply(instance, ::HTGrowthChamberRecipe)
            }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTGrowthChamberRecipe> =
            createPacketCodec(::HTGrowthChamberRecipe)
    }

    private constructor(
        tier: HTMachineTier,
        seed: RegistryEntry<Item>,
        fluid: HTFluidIngredient,
        soil: HTItemIngredient,
        crop: HTItemResult,
    ) : this(
        HTMachineDefinition(RagiumMachineKeys.GROWTH_CHAMBER, tier),
        HTMachineRecipeData(
            listOf(HTItemIngredient.of(seed)),
            listOf(fluid),
            soil,
            listOf(crop, HTItemResult.ofItem(seed)),
            listOf(),
        ),
    )

    override fun matches(input: HTMachineInput, world: World): Boolean {
        if (!checkDefinition(input)) return false
        if (!data.itemIngredients[0].test(input.getStackInSlot(0))) return false
        if (!data.fluidIngredients[0].test(input.getFluidInSlot(0))) return false
        return data.catalyst?.test(input.catalyst) == true
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.GROWTH_CHAMBER

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.GROWTH_CHAMBER
}
