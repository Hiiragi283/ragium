package hiiragi283.ragium.integration

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.material.HTMaterialItemLike
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.common.init.RagiumCreativeTabs
import hiiragi283.ragium.common.item.HTMaterialItem
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalBuilder
import mekanism.common.registration.impl.ChemicalDeferredRegister
import mekanism.common.registration.impl.DeferredChemical
import mekanism.common.registration.impl.SlurryRegistryObject
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
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

        CHEMICAL_REGISTER.register(eventBus)
        OreResources.entries
        ITEM_REGISTER.register(eventBus)
    }

    private fun buildCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        if (RagiumCreativeTabs.COMMON.`is`(event.tabKey)) {
            event.accept(ITEM_ENRICHED_RAGINITE)
            OreResources.entries.forEach(event::accept)
        }
    }
}
