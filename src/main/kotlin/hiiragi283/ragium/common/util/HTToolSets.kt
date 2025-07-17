package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.item.HTForgeHammerItem
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.registry.HTItemSet
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredItem

class HTToolSets(material: Tier, name: String) : HTItemSet {
    private val itemRegister = HTItemRegister(RagiumAPI.MOD_ID)

    val shovelItem: DeferredItem<ShovelItem> = itemRegister.registerItem(
        "${name}_shovel",
        { ShovelItem(material, it) },
        Item.Properties().attributes(DiggerItem.createAttributes(material, 1.5f, -3f)),
    )

    val pickaxeItem: DeferredItem<PickaxeItem> = itemRegister.registerItem(
        "${name}_pickaxe",
        { PickaxeItem(material, it) },
        Item.Properties().attributes(DiggerItem.createAttributes(material, 1f, -2.8f)),
    )

    val axeItem: DeferredItem<AxeItem> = itemRegister.registerItem(
        "${name}_axe",
        { AxeItem(material, it) },
        Item.Properties().attributes(DiggerItem.createAttributes(material, 6f, -3.1f)),
    )

    val hoeItem: DeferredItem<HoeItem> = itemRegister.registerItem(
        "${name}_hoe",
        { HoeItem(material, it) },
        Item.Properties().attributes(DiggerItem.createAttributes(material, -2f, -1f)),
    )

    val swordItem: DeferredItem<SwordItem> = itemRegister.registerItem(
        "${name}_sword",
        { SwordItem(material, it) },
        Item.Properties().attributes(DiggerItem.createAttributes(material, 3f, -2.4f)),
    )

    val hammerItem: DeferredItem<HTForgeHammerItem> = itemRegister.registerItem("${name}_hammer") { HTForgeHammerItem(material, it) }

    //    HTItemSet    //

    override val itemHolders: List<DeferredItem<*>> = itemRegister.entries

    override fun init(eventBus: IEventBus) {
        itemRegister.register(eventBus)
    }

    override fun addItemModels(provider: ItemModelProvider) {
        getItems().forEach(provider::handheldItem)
    }

    override fun addTranslationEn(name: String, provider: LanguageProvider) {
        provider.addItem(axeItem, "$name Axe")
        provider.addItem(hoeItem, "$name Hoe")
        provider.addItem(pickaxeItem, "$name Pickaxe")
        provider.addItem(shovelItem, "$name Shovel")
        provider.addItem(swordItem, "$name Sword")
        provider.addItem(hammerItem, "$name Forge Hammer")
    }

    override fun addTranslationJp(name: String, provider: LanguageProvider) {
        provider.addItem(axeItem, "${name}の斧")
        provider.addItem(hoeItem, "${name}のクワ")
        provider.addItem(pickaxeItem, "${name}のツルハシ")
        provider.addItem(shovelItem, "${name}のシャベル")
        provider.addItem(swordItem, "${name}の剣")
        provider.addItem(hammerItem, "${name}の鍛造ハンマー")
    }
}
