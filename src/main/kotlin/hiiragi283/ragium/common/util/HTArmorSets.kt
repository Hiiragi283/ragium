package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.registry.HTItemSet
import net.minecraft.core.Holder
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredItem

class HTArmorSets(material: Holder<ArmorMaterial>, name: String, multiplier: Int) : HTItemSet {
    private val itemRegister = HTItemRegister(RagiumAPI.MOD_ID)

    private val armorMap: Map<ArmorItem.Type, DeferredItem<ArmorItem>> = ArmorItem.Type.entries
        .filter(ArmorItem.Type::hasTrims)
        .associateWith { armorType: ArmorItem.Type ->
            itemRegister.registerItem(
                "${name}_${armorType.serializedName}",
                { properties: Item.Properties -> ArmorItem(material, armorType, properties) },
                Item.Properties().durability(armorType.getDurability(multiplier)),
            )
        }

    val helmetItem: DeferredItem<ArmorItem> = armorMap[ArmorItem.Type.HELMET]!!
    val chestplateItem: DeferredItem<ArmorItem> = armorMap[ArmorItem.Type.CHESTPLATE]!!
    val leggingsItem: DeferredItem<ArmorItem> = armorMap[ArmorItem.Type.LEGGINGS]!!
    val bootsItem: DeferredItem<ArmorItem> = armorMap[ArmorItem.Type.BOOTS]!!

    //    HTItemSet    //

    override val itemHolders: List<DeferredItem<*>>
        get() = armorMap.values.toList()

    override fun init(eventBus: IEventBus) {
        itemRegister.register(eventBus)
    }

    override fun addItemModels(provider: ItemModelProvider) {
        getItems().forEach(provider::basicItem)
    }

    override fun addTranslationEn(name: String, provider: LanguageProvider) {
        provider.addItem(helmetItem, "$name Helmet")
        provider.addItem(chestplateItem, "$name Chestplate")
        provider.addItem(leggingsItem, "$name Leggings")
        provider.addItem(bootsItem, "$name Boots")
    }

    override fun addTranslationJp(name: String, provider: LanguageProvider) {
        provider.addItem(helmetItem, "${name}のヘルメット")
        provider.addItem(chestplateItem, "${name}のチェストプレート")
        provider.addItem(leggingsItem, "${name}のレギンス")
        provider.addItem(bootsItem, "${name}のブーツ")
    }
}
