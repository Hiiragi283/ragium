package hiiragi283.ragium.integration.mekanism

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.material.HTMaterialItemLike
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.common.init.RagiumCreativeTabs
import hiiragi283.ragium.common.init.RagiumFoods
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.item.HTMaterialItem
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalBuilder
import mekanism.common.registration.impl.ChemicalDeferredRegister
import mekanism.common.registration.impl.DeferredChemical
import mekanism.common.registration.impl.SlurryRegistryObject
import mekanism.common.registries.MekanismItems
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.registries.DeferredItem

@HTAddon("mekanism")
object RagiumMekanismAddon : RagiumAddon {
    //    Chemical    //

    @JvmField
    val CHEMICAL_REGISTER = ChemicalDeferredRegister(RagiumAPI.MOD_ID)

    @JvmField
    val CHEMICAL_RAGINITE: DeferredChemical<Chemical> = CHEMICAL_REGISTER.registerInfuse("raginite", 0xFF0033)

    @JvmField
    val CHEMICAL_AZURE: DeferredChemical<Chemical> = CHEMICAL_REGISTER.registerInfuse("azure", 0x9999cc)

    @JvmField
    val CHEMICAL_RAGINITE_SLURRY: SlurryRegistryObject<Chemical, Chemical> =
        CHEMICAL_REGISTER.registerSlurry("raginite") { builder: ChemicalBuilder ->
            builder.tint(0xFF0033)
        }

    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTItemRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ITEM_ENRICHED_RAGINITE: DeferredItem<Item> = ITEM_REGISTER.registerSimpleItem("enriched_raginite")

    enum class OreResources(override val prefix: HTTagPrefix) : HTMaterialItemLike {
        DIRTY_DUST(HTTagPrefixes.DIRTY_DUST),
        CLUMP(HTTagPrefixes.CLUMP),
        SHARD(HTTagPrefixes.SHARD),
        CRYSTAL(HTTagPrefixes.CRYSTAL),
        ;

        override val key: HTMaterialKey = RagiumMaterials.RAGINITE
        private val holder: DeferredItem<HTMaterialItem> = ITEM_REGISTER.registerItem(prefix.createPath(key)) { prop ->
            HTMaterialItem(prefix, key, prop)
        }
        override val id: ResourceLocation = holder.id

        override fun asItem(): Item = holder.asItem()
    }

    //    RagiumAddon    //

    override val priority: Int = 0

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        eventBus.addListener(::buildCreativeTabs)
        eventBus.addListener(::modifyComponent)

        CHEMICAL_REGISTER.register(eventBus)
        OreResources.entries
        ITEM_REGISTER.register(eventBus)
    }

    private fun modifyComponent(event: ModifyDefaultComponentsEvent) {
        event.modify(MekanismItems.YELLOW_CAKE_URANIUM) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, RagiumFoods.YELLOW_CAKE)
        }
    }

    private fun buildCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        fun acceptRaginite(item: ItemLike) {
            event.insertAfter(
                RagiumItems.RawResources.RAGINITE.toStack(),
                item.toStack(),
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            )
        }

        if (RagiumCreativeTabs.COMMON.`is`(event.tabKey)) {
            acceptRaginite(ITEM_ENRICHED_RAGINITE)
            OreResources.entries.forEach(::acceptRaginite)
        }
    }
}
