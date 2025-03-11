package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toOptional
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.util.HTMachineException
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import java.util.*

class HTEnchanterRecipe(
    group: String,
    val firstInput: HTItemIngredient,
    val secondInput: Optional<HTItemIngredient>,
    val enchantment: Holder<Enchantment>,
) : HTMachineRecipe(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTEnchanterRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTItemIngredient.CODEC.fieldOf("first_item_input").forGetter(HTEnchanterRecipe::firstInput),
                    HTItemIngredient.CODEC
                        .optionalFieldOf("second_item_input")
                        .forGetter(HTEnchanterRecipe::secondInput),
                    RegistryFixedCodec
                        .create(Registries.ENCHANTMENT)
                        .fieldOf("enchantment_output")
                        .forGetter(HTEnchanterRecipe::enchantment),
                ).apply(instance, ::HTEnchanterRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTEnchanterRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTEnchanterRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTEnchanterRecipe::firstInput,
            HTItemIngredient.STREAM_CODEC.toOptional(),
            HTEnchanterRecipe::secondInput,
            ByteBufCodecs.holderRegistry(Registries.ENCHANTMENT),
            HTEnchanterRecipe::enchantment,
            ::HTEnchanterRecipe,
        )
    }

    override fun matches(context: HTMachineRecipeContext): Boolean {
        if (context.getItemStack(HTStorageIO.INPUT, 0).getEnchantmentLevel(enchantment) > 0) {
            return false
        }
        val second: ItemStack = context.getItemStack(HTStorageIO.INPUT, 2)
        return firstInput.test(context.getItemStack(HTStorageIO.INPUT, 1)) &&
            secondInput
                .map { it.test(second) }
                .orElse(second.isEmpty)
    }

    override fun canProcess(context: HTMachineRecipeContext): Result<Unit> = runCatching {
        // Check enchantment application
        val stack1: ItemStack = context.getItemStack(HTStorageIO.INPUT, 0)
        val bool1: Boolean = stack1.supportsEnchantment(enchantment)
        val bool2: Boolean = EnchantmentHelper.isEnchantmentCompatible(
            EnchantmentHelper.getEnchantmentsForCrafting(stack1).keySet(),
            enchantment,
        )
        if (!bool1 || !bool2) throw HTMachineException.Custom("Cannot apply enchantment for the target input!")
        // Output
        val outputCopy: ItemStack = stack1.copy()
        outputCopy.enchant(enchantment, enchantment.value().maxLevel)
        if (!context.getSlot(HTStorageIO.OUTPUT, 0).canInsert(outputCopy)) {
            throw HTMachineException.GrowItem()
        }
        // Input
        if (!context.getSlot(HTStorageIO.INPUT, 0).canExtract(1)) {
            throw HTMachineException.ShrinkItem()
        }
        if (!context.getSlot(HTStorageIO.INPUT, 1).canExtract(firstInput.count)) {
            throw HTMachineException.ShrinkItem()
        }
        secondInput.ifPresent { ingredient: HTItemIngredient ->
            if (!context.getSlot(HTStorageIO.INPUT, 2).canExtract(ingredient.count)) {
                throw HTMachineException.ShrinkItem()
            }
        }
    }

    override fun process(context: HTMachineRecipeContext) {
        // Apply enchantments
        val stackIn: ItemStack = context.getItemStack(HTStorageIO.OUTPUT, 0)
        stackIn.enchant(enchantment, enchantment.value().maxLevel)
        context.getSlot(HTStorageIO.OUTPUT, 0).insert(stackIn, false)
        // Input
        context.getSlot(HTStorageIO.INPUT, 0).extract(1, false)
        context.getSlot(HTStorageIO.INPUT, 1).extract(firstInput.count, false)
        secondInput.ifPresent { ingredient: HTItemIngredient ->
            context.getSlot(HTStorageIO.INPUT, 2).extract(ingredient.count, false)
        }
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.ENCHANTER
}
