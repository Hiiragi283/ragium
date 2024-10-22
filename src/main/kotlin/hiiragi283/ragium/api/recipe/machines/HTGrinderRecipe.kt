package hiiragi283.ragium.api.recipe.machines

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.SingleStackRecipeInput
import net.minecraft.world.World

class HTGrinderRecipe(
    tier: HTMachineTier,
    val input: HTItemIngredient,
    override val itemOutputs: List<HTItemResult>,
    override val catalyst: HTItemIngredient
) :
    HTMachineRecipeBase<SingleStackRecipeInput>(RagiumMachineTypes.Processor.GRINDER, tier) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTGrinderRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMachineTier.CODEC
                        .optionalFieldOf("tier", HTMachineTier.PRIMITIVE)
                        .forGetter(HTGrinderRecipe::tier),
                    HTIngredientNew.ITEM_CODEC
                        .fieldOf("input")
                        .forGetter(HTGrinderRecipe::input),
                    HTRecipeResultNew.ITEM_CODEC
                        .listOf()
                        .optionalFieldOf("outputs", listOf())
                        .forGetter(HTGrinderRecipe::itemOutputs),
                    HTIngredientNew.ITEM_CODEC
                        .optionalFieldOf("catalyst", HTItemIngredient.EMPTY_ITEM)
                        .forGetter(HTGrinderRecipe::catalyst),
                ).apply(instance, ::HTGrinderRecipe)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTGrinderRecipe> = PacketCodec.tuple(
            HTMachineTier.PACKET_CODEC,
            HTGrinderRecipe::tier,
            HTIngredientNew.ITEM_PACKET_CODEC,
            HTGrinderRecipe::input,
            HTRecipeResultNew.ITEM_PACKET_CODEC.toList(),
            HTGrinderRecipe::itemOutputs,
            HTIngredientNew.ITEM_PACKET_CODEC,
            HTGrinderRecipe::catalyst,
            ::HTGrinderRecipe,
        )
    }

    override val itemInputs: List<HTItemIngredient> = listOf(input)
    override val fluidInputs: List<HTFluidIngredient> = listOf()
    override val fluidOutputs: List<HTFluidResult> = listOf()

    override fun matches(input: SingleStackRecipeInput, world: World): Boolean = input.item.let {
        this.input.test(ItemVariant.of(it), it.count.toLong())
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.GRINDER

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.GRINDER
}
