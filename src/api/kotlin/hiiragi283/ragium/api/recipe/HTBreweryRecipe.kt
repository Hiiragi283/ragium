package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.extension.toOptional
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.alchemy.Potion
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*

class HTBreweryRecipe(
    group: String,
    val firstInput: HTItemIngredient,
    val secondInput: HTItemIngredient,
    val thirdInput: Optional<HTItemIngredient>,
    val potion: Holder<Potion>,
) : HTMachineRecipe(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTBreweryRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTItemIngredient.CODEC
                        .fieldOf("first_item_input")
                        .forGetter(HTBreweryRecipe::firstInput),
                    HTItemIngredient.CODEC
                        .fieldOf("second_item_input")
                        .forGetter(HTBreweryRecipe::secondInput),
                    HTItemIngredient.CODEC
                        .optionalFieldOf("third_item_input")
                        .forGetter(HTBreweryRecipe::thirdInput),
                    Potion.CODEC
                        .fieldOf("effect")
                        .forGetter(HTBreweryRecipe::potion),
                ).apply(instance, ::HTBreweryRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTBreweryRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTBreweryRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTBreweryRecipe::firstInput,
            HTItemIngredient.STREAM_CODEC,
            HTBreweryRecipe::secondInput,
            HTItemIngredient.STREAM_CODEC.toOptional(),
            HTBreweryRecipe::thirdInput,
            ByteBufCodecs.holderRegistry(Registries.POTION),
            HTBreweryRecipe::potion,
            ::HTBreweryRecipe,
        )

        @JvmField
        val WATER_INGREDIENT: SizedFluidIngredient = SizedFluidIngredient.of(Tags.Fluids.WATER, FluidType.BUCKET_VOLUME)
    }

    override fun matches(context: HTMachineRecipeContext): Boolean {
        val bool1: Boolean = this.firstInput.test(context.getItemStack(HTStorageIO.INPUT, 0))
        val bool2: Boolean = this.secondInput.test(context.getItemStack(HTStorageIO.INPUT, 1))
        val bool3: Boolean = this.thirdInput.map { it.test(context.getItemStack(HTStorageIO.INPUT, 2)) }.orElse(true)
        val bool4: Boolean = WATER_INGREDIENT.test(context.getFluidStack(HTStorageIO.INPUT, 0))
        return bool1 && bool2 && bool3 && bool4
    }

    override fun canProcess(context: HTMachineRecipeContext): Result<Unit> = runCatching {
        // Output
        if (!context.getSlot(HTStorageIO.OUTPUT, 0).canInsert(createPotionStack(potion, 3))) {
            throw HTMachineException.GrowItem()
        }
        // Input
        if (!context.getSlot(HTStorageIO.INPUT, 0).canShrink(firstInput.count)) {
            throw HTMachineException.ShrinkItem()
        }
        if (!context.getSlot(HTStorageIO.INPUT, 1).canShrink(secondInput.count)) {
            throw HTMachineException.ShrinkItem()
        }
        thirdInput.ifPresent { ingredient: HTItemIngredient ->
            if (!context.getSlot(HTStorageIO.INPUT, 2).canShrink(ingredient.count)) {
                throw HTMachineException.ShrinkItem()
            }
        }

        if (!context.getTank(HTStorageIO.INPUT, 0).canShrink(WATER_INGREDIENT.amount())) {
            throw HTMachineException.ShrinkFluid()
        }
    }

    override fun process(context: HTMachineRecipeContext) {
        // Output
        context.getSlot(HTStorageIO.OUTPUT, 0).insert(createPotionStack(potion, 3), false)
        // Input
        context.getSlot(HTStorageIO.INPUT, 0).extract(firstInput.count, false)
        context.getSlot(HTStorageIO.INPUT, 1).extract(secondInput.count, false)
        thirdInput.ifPresent { ingredient: HTItemIngredient ->
            context.getSlot(HTStorageIO.INPUT, 2).extract(ingredient.count, false)
        }

        context.getTank(HTStorageIO.INPUT, 0).shrinkStack(WATER_INGREDIENT.amount(), false)
    }

    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.BREWERY
}
