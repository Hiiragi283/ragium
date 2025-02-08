package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.canInsert
import hiiragi283.ragium.api.extension.insertOrDrop
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.recipe.base.HTChancedItemStack
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeCodecs
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.items.IItemHandler
import java.util.*
import kotlin.jvm.optionals.getOrNull

class HTGrinderRecipe(
    group: String,
    val input: SizedIngredient,
    private val output: ItemStack,
    val secondOutput: Optional<HTChancedItemStack>,
) : HTMachineRecipeBase(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTGrinderRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTRecipeCodecs.ITEM_INPUT.forGetter(HTGrinderRecipe::input),
                    HTRecipeCodecs.ITEM_OUTPUT.forGetter(HTGrinderRecipe::output),
                    HTChancedItemStack.CODEC.optionalFieldOf("second_output").forGetter(HTGrinderRecipe::secondOutput),
                ).apply(instance, ::HTGrinderRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTGrinderRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTGrinderRecipe::getGroup,
            SizedIngredient.STREAM_CODEC,
            HTGrinderRecipe::input,
            ItemStack.STREAM_CODEC,
            HTGrinderRecipe::output,
            ByteBufCodecs.optional(HTChancedItemStack.STREAM_CODEC),
            HTGrinderRecipe::secondOutput,
            ::HTGrinderRecipe,
        )
    }

    fun canInsert(itemHandler: IItemHandler) {
        // First Output
        if (!itemHandler.canInsert(output.copy())) throw HTMachineException.MergeResult(false)
        // Second Output
        secondOutput.ifPresent { second: HTChancedItemStack ->
            if (!itemHandler.canInsert(second.toStack())) throw HTMachineException.MergeResult(false)
        }
    }

    fun insertOutputs(machine: HTMachineAccess, itemHandler: IItemHandler) {
        val level: Level = machine.levelAccess ?: return
        val pos: BlockPos = machine.pos
        // First Output
        itemHandler.insertOrDrop(level, pos, output.copy())
        // Second Output
        getSecondOutput(machine).ifPresent { second: ItemStack ->
            itemHandler.insertOrDrop(level, pos, second)
        }
    }

    fun getSecondOutput(machine: HTMachineAccess): Optional<ItemStack> {
        val chanced: HTChancedItemStack = this.secondOutput.getOrNull() ?: return Optional.empty()
        val level: Level = machine.levelAccess ?: return Optional.empty()
        val fortune: Int = machine.getEnchantmentLevel(Enchantments.FORTUNE)
        repeat(fortune + 1) {
            if (level.random.nextFloat() > chanced.chance) {
                return Optional.of(chanced.toStack())
            }
        }
        return Optional.empty()
    }

    override fun getItemOutput(): ItemStack = output.copy()

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean = this.input.test(input.getItem(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.GRINDER.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.GRINDER.get()
}
