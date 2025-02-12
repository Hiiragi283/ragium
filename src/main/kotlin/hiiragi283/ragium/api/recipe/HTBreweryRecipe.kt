package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.base.HTItemResult
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeCodecs
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.*

class HTBreweryRecipe(
    group: String,
    val firstInput: SizedIngredient,
    val secondInput: SizedIngredient,
    val thirdInput: Optional<SizedIngredient>,
    private val potion: Holder<Potion>,
) : HTMachineRecipeBase(group) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTBreweryRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    SizedIngredient.FLAT_CODEC
                        .fieldOf("first_item_input")
                        .forGetter(HTBreweryRecipe::firstInput),
                    SizedIngredient.FLAT_CODEC
                        .fieldOf("second_item_input")
                        .forGetter(HTBreweryRecipe::secondInput),
                    SizedIngredient.FLAT_CODEC
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
            SizedIngredient.STREAM_CODEC,
            HTBreweryRecipe::firstInput,
            SizedIngredient.STREAM_CODEC,
            HTBreweryRecipe::secondInput,
            ByteBufCodecs.optional(SizedIngredient.STREAM_CODEC),
            HTBreweryRecipe::thirdInput,
            ByteBufCodecs.holderRegistry(Registries.POTION),
            HTBreweryRecipe::potion,
            ::HTBreweryRecipe,
        )
    }

    override val itemResults: List<HTItemResult> = listOf(PotionResult(potion))

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean {
        val bool1: Boolean = this.firstInput.test(input.getItem(0))
        val bool2: Boolean = this.secondInput.test(input.getItem(1))
        val bool3: Boolean = this.thirdInput.map { it.test(input.getItem(2)) }.orElse(true)
        return bool1 && bool2 && bool3
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BREWERY.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.BREWERY.get()

    class PotionResult(val potion: Holder<Potion>) : HTItemResult {
        companion object {
            @JvmField
            val CODEC: MapCodec<PotionResult> =
                Potion.CODEC.fieldOf("effect").xmap(::PotionResult, PotionResult::potion)
        }

        override fun getCodec(): MapCodec<out HTItemResult> = CODEC

        override fun getResultId(): ResourceLocation = potion.idOrThrow

        override fun getItem(enchantments: ItemEnchantments): ItemStack {
            val stack = ItemStack(Items.POTION)
            stack.set(DataComponents.POTION_CONTENTS, PotionContents(potion))
            return stack
        }
    }
}
