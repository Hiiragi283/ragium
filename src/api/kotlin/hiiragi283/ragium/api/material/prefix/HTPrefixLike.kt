package hiiragi283.ragium.api.material.prefix

import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.RegistryKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

fun interface HTPrefixLike {
    fun asMaterialPrefix(): HTMaterialPrefix

    fun asPrefixName(): String = asMaterialPrefix().name

    fun <T : Any> createCommonTagKey(key: RegistryKey<T>): TagKey<T> = asMaterialPrefix().createCommonTagKey(key)

    fun <T : Any> createTagKey(key: RegistryKey<T>, material: HTMaterialLike): TagKey<T> = createTagKey(key, material.asMaterialName())

    fun <T : Any> createTagKey(key: RegistryKey<T>, name: String): TagKey<T> = asMaterialPrefix().createTagKey(key, name)

    fun blockTagKey(material: HTMaterialLike): TagKey<Block> = createTagKey(Registries.BLOCK, material)

    @Deprecated("Use `blockTagKey(HTMaterialLike) instead`")
    fun blockTagKey(name: String): TagKey<Block> = createTagKey(Registries.BLOCK, name)

    fun itemTagKey(material: HTMaterialLike): TagKey<Item> = createTagKey(Registries.ITEM, material)

    @Deprecated("Use `itemTagKey(HTMaterialLike) instead`")
    fun itemTagKey(name: String): TagKey<Item> = createTagKey(Registries.ITEM, name)

    fun toIngredient(material: HTMaterialLike): Ingredient = Ingredient.of(itemTagKey(material))
}
