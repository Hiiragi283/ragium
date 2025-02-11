package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.base.HTItemResult
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeCodecs
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTGrinderRecipe(group: String, val input: SizedIngredient, itemResult: HTItemResult) : HTMachineRecipeBase(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTGrinderRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTRecipeCodecs.ITEM_INPUT.forGetter(HTGrinderRecipe::input),
                    HTRecipeCodecs.itemResult(),
                ).apply(instance, ::HTGrinderRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTGrinderRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTGrinderRecipe::getGroup,
            SizedIngredient.STREAM_CODEC,
            HTGrinderRecipe::input,
            HTItemResult.STREAM_CODEC,
            { it.itemResults[0] },
            ::HTGrinderRecipe,
        )
    }

    /*fun getSecondOutput(machine: HTMachineAccess): Optional<ItemStack> {
        val chanced: HTChancedItemStack = this.secondOutput.getOrNull() ?: return Optional.empty()
        val level: Level = machine.levelAccess ?: return Optional.empty()
        val fortune: Int = machine.getEnchantmentLevel(Enchantments.FORTUNE)
        val chance: Double = max(
            1.0,
            chanced.chance + when (fortune) {
                2 -> 0.66
                3 -> 1.0
                else -> 0.33
            },
        )
        return when {
            level.random.nextFloat() > (1 - chance) -> Optional.of(chanced.toStack())
            else -> Optional.empty()
        }
    }*/

    override val itemResults: List<HTItemResult> = listOf(itemResult)

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean = this.input.test(input.getItem(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.GRINDER.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.GRINDER.get()
}
