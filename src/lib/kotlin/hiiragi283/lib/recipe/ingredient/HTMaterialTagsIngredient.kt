package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTComparators
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.collection.toNel
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.serialization.network.nelOf
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import java.util.stream.Stream

/**
 * @since 26.1.7
 * @author Hiiragi Tsubasa
 */
@ConsistentCopyVisibility
data class HTMaterialTagsIngredient private constructor(val tags: Nel<TagKey<Item>>) : ICustomIngredient {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMaterialTagsIngredient> = HTCodecs.tagKey(Registries.ITEM, false)
            .compactNelFieldOf("tag", "tags")
            .xmap(::HTMaterialTagsIngredient, HTMaterialTagsIngredient::tags)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMaterialTagsIngredient> = HTStreamCodecs.tagKey(Registries.ITEM)
            .nelOf()
            .map(::HTMaterialTagsIngredient, HTMaterialTagsIngredient::tags)

        @JvmField
        val TYPE: IngredientType<HTMaterialTagsIngredient> = IngredientType(CODEC, STREAM_CODEC)
    }

    constructor(prefixes: Nel<HTTagPrefix>, materials: Nel<HTMaterialLike>) : this(
        prefixes
            .flatMap { prefix: HTTagPrefix -> materials.map(prefix::itemTagKey) }
            .let(HTComparators::sortTagKeys)
            .toNel()
    )

    override fun test(stack: ItemStack): Boolean = tags.any(stack::`is`)

    override fun items(): Stream<Holder<Item>> = tags.stream()
        .flatMap { tagKey: TagKey<Item> -> BuiltInRegistries.ITEM.get(tagKey).stream().flatMap { it.stream() } }

    override fun isSimple(): Boolean = true

    override fun getType(): IngredientType<*> = TYPE
}
