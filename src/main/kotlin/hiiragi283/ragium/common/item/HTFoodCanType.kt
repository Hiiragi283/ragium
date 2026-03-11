package hiiragi283.ragium.common.item

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangType
import hiiragi283.core.api.data.lang.HTLangTypes
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.util.Either
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.Tags

enum class HTFoodCanType(private val enPattern: String, private val jaPattern: String, val foodTag: TagKey<Item>) :
    StringRepresentable,
    HTLangName,
    HTItemHolderLike.Simple<Item> {
    FISH("Fish", "魚", Tags.Items.FOODS_COOKED_FISH),
    FRUIT("Fruit", "果物", Tags.Items.FOODS_FRUIT),
    MEAT("Meat", "肉", Tags.Items.FOODS_COOKED_MEAT),
    SOUP("Soup", "スープ", Tags.Items.FOODS_SOUP),
    VEGETABLE("Vegetable", "野菜", Tags.Items.FOODS_VEGETABLE),
    ;

    private val delegate: HTSimpleItemHolderLike by lazy { RagiumItems.FOOD_CANS[this]!! }

    override fun unwrap(): Either<ResourceKey<Item>, Holder<Item>> = delegate.unwrap()

    override fun get(): Item = delegate.get()

    override fun getTranslatedName(type: HTLangType): String = when (type) {
        HTLangTypes.JA_JP -> jaPattern
        else -> enPattern
    }

    override fun getSerializedName(): String = name.lowercase()
}
