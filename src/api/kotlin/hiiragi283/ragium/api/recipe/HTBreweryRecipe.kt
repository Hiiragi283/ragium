package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.util.HTMachineException
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTBreweryRecipe(
    group: String,
    val firstInput: HTItemIngredient,
    val secondInput: Optional<HTItemIngredient>,
    val potion: Holder<Potion>,
) : HTMachineRecipe(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTBreweryRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTItemIngredient.CODEC.fieldOf("first_item_input").forGetter(HTBreweryRecipe::firstInput),
                    HTItemIngredient.CODEC.optionalFieldOf("second_item_input").forGetter(HTBreweryRecipe::secondInput),
                    Potion.CODEC.fieldOf("potion_output").forGetter(HTBreweryRecipe::potion),
                ).apply(instance, ::HTBreweryRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTBreweryRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTBreweryRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTBreweryRecipe::firstInput,
            ByteBufCodecs.optional(HTItemIngredient.STREAM_CODEC),
            HTBreweryRecipe::secondInput,
            ByteBufCodecs.holderRegistry(Registries.POTION),
            HTBreweryRecipe::potion,
            ::HTBreweryRecipe,
        )

        @JvmField
        val WART_INGREDIENT: Ingredient = Ingredient.of(Tags.Items.CROPS_NETHER_WART)

        @JvmField
        val WATER_INGREDIENT: SizedFluidIngredient = SizedFluidIngredient.of(Tags.Fluids.WATER, 1000)
    }

    val potionStack: ItemStack = createPotionStack(potion, 3)

    override fun matches(context: HTMachineRecipeContext): Boolean {
        val bool1: Boolean = WART_INGREDIENT.test(context.getItemStack(HTStorageIO.INPUT, 0))
        val bool2: Boolean = firstInput.test(context.getItemStack(HTStorageIO.INPUT, 1))
        val thirdStack: ItemStack = context.getItemStack(HTStorageIO.INPUT, 2)
        val bool3: Boolean = secondInput.map { it.test(thirdStack) }.orElse(thirdStack.isEmpty)
        val bool4: Boolean = WATER_INGREDIENT.test(context.getFluidStack(HTStorageIO.INPUT, 0))
        return bool1 && bool2 && bool3 && bool4
    }

    override fun canProcess(context: HTMachineRecipeContext): Result<Unit> = runCatching {
        // Output
        if (!context.getSlot(HTStorageIO.OUTPUT, 0).canInsert(potionStack)) {
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

        if (!context.getTank(HTStorageIO.INPUT, 0).canExtract(WATER_INGREDIENT.amount())) {
            throw HTMachineException.ShrinkFluid()
        }
    }

    override fun process(context: HTMachineRecipeContext) {
        // Output
        context.getSlot(HTStorageIO.OUTPUT, 0).insert(potionStack, false)
        // Input
        context.getSlot(HTStorageIO.INPUT, 0).extract(1, false)
        context.getSlot(HTStorageIO.INPUT, 1).extract(firstInput.count, false)
        secondInput.ifPresent { ingredient: HTItemIngredient ->
            context.getSlot(HTStorageIO.INPUT, 2).extract(ingredient.count, false)
        }

        context.getTank(HTStorageIO.INPUT, 0).extract(WATER_INGREDIENT.amount(), false)
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.BREWERY
}
