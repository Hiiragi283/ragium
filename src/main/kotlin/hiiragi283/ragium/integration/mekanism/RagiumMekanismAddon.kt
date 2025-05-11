package hiiragi283.ragium.integration.mekanism

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumFoods
import hiiragi283.ragium.setup.RagiumItems
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalBuilder
import mekanism.common.registration.impl.ChemicalDeferredRegister
import mekanism.common.registration.impl.DeferredChemical
import mekanism.common.registration.impl.SlurryRegistryObject
import mekanism.common.registries.MekanismItems
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
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

    @JvmField
    val ITEM_DIRTY_RAGINITE_DUST: DeferredItem<Item> = ITEM_REGISTER.registerSimpleItem("dirty_raginite_dust")

    @JvmField
    val ITEM_RAGINITE_CLUMP: DeferredItem<Item> = ITEM_REGISTER.registerSimpleItem("raginite_clump")

    @JvmField
    val ITEM_RAGINITE_SHARD: DeferredItem<Item> = ITEM_REGISTER.registerSimpleItem("raginite_shard")

    @JvmField
    val ITEM_RAGINITE_CRYSTAL: DeferredItem<Item> = ITEM_REGISTER.registerSimpleItem("raginite_crystal")

    @JvmField
    val ITEM_ENRICHED_AZURE: DeferredItem<Item> = ITEM_REGISTER.registerSimpleItem("enriched_azure")

    //    RagiumAddon    //

    override val priority: Int = 0

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        eventBus.addListener(::buildCreativeTabs)
        eventBus.addListener(::modifyComponent)

        CHEMICAL_REGISTER.register(eventBus)
        ITEM_REGISTER.register(eventBus)
    }

    private fun modifyComponent(event: ModifyDefaultComponentsEvent) {
        event.modify(MekanismItems.YELLOW_CAKE_URANIUM) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.FOOD, RagiumFoods.YELLOW_CAKE)
        }
    }

    private lateinit var lastStack: ItemStack

    private fun buildCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        fun acceptRaginite(item: ItemLike) {
            val stack: ItemStack = item.toStack()
            event.insertAfter(
                lastStack,
                stack,
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
            )
            lastStack = stack
        }

        if (RagiumCreativeTabs.COMMON.`is`(event.tabKey)) {
            lastStack = RagiumItems.RAW_RAGINITE.toStack()
            // Raginite
            acceptRaginite(ITEM_ENRICHED_RAGINITE)
            acceptRaginite(ITEM_DIRTY_RAGINITE_DUST)
            acceptRaginite(ITEM_RAGINITE_CLUMP)
            acceptRaginite(ITEM_RAGINITE_SHARD)
            acceptRaginite(ITEM_RAGINITE_CRYSTAL)
            // Azure
            event.insertAfter(
                RagiumItems.AZURE_SHARD.toStack(),
                ITEM_ENRICHED_AZURE.toStack(),
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
            )
        }
    }
}
