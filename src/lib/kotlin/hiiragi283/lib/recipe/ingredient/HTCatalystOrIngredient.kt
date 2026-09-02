package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.util.Either
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

/**
 * [触媒][Ingredient]または[材料][HTItemIngredient]を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTCatalystOrIngredient(private val content: Either<Ingredient, HTItemIngredient>) :
    HTIngredient<ItemInstance>,
    HTStackPreview<ItemStack> {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<HTCatalystOrIngredient> = HTCodecs.mapEither(
            Ingredient.CODEC.fieldOf(HTConstants.CATALYST),
            HTItemIngredient.CODEC.fieldOf(HTConstants.ITEM_INGREDIENT),
        ).xmap(::HTCatalystOrIngredient, HTCatalystOrIngredient::content)

        @JvmField
        val CODEC: Codec<HTCatalystOrIngredient> = MAP_CODEC.codec()

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTCatalystOrIngredient> = HTStreamCodecs.either(
            Ingredient.CONTENTS_STREAM_CODEC,
            HTItemIngredient.STREAM_CODEC,
        ).map(::HTCatalystOrIngredient, HTCatalystOrIngredient::content)
    }

    constructor(catalyst: Ingredient) : this(Either.Left(catalyst))

    constructor(ingredient: HTItemIngredient) : this(Either.Right(ingredient))

    val isCatalyst: Boolean get() = content.isLeft()

    //    HTIngredient    //

    override fun test(instance: ItemInstance): Boolean = content.fold({ testOnlyType(instance) }, { it.test(instance) })

    override fun testOnlyType(instance: ItemInstance): Boolean = content.fold({ HTIngredientHelper.unwrap(instance).let(::testOnlyType) }, { it.testOnlyType(instance) })

    override fun getRequiredAmount(instance: ItemInstance): Int = content.fold({ 0 }, { it.getRequiredAmount(instance) })

    //    HTStackPreview    //

    override fun getPreviewStacks(contextMap: ContextMap): List<ItemStack> = content.fold({ it.display().resolveForStacks(contextMap) }, { it.getPreviewStacks(contextMap) })
}
