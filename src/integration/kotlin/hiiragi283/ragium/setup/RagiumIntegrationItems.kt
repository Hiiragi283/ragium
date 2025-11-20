package hiiragi283.ragium.setup

import com.simibubi.create.content.equipment.sandPaper.SandPaperItem
import hiiragi283.ragium.RagiumIntegration
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.MekanismMaterialPrefixes
import hiiragi283.ragium.common.material.ModMaterialKeys
import hiiragi283.ragium.common.material.RagiumEssenceType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.variant.HTKitchenKnifeToolVariant
import hiiragi283.ragium.common.variant.HTKnifeToolVariant
import hiiragi283.ragium.common.variant.HTSandPaperToolVariant
import mekanism.common.registries.MekanismItems
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems as KaleidoItems
import vectorwing.farmersdelight.common.registry.ModItems as DelightItems

/**
 * @see RagiumItems
 */
object RagiumIntegrationItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        // eventBus.addListener(::modifyCreativeTabs)
        eventBus.addListener(::modifyComponents)
    }

    //    Materials    //

    @JvmStatic
    val MATERIALS: ImmutableTable<HTMaterialPrefix, HTMaterialKey, HTSimpleDeferredItem> = buildTable {
        // Enriched
        if (RagiumIntegration.isLoaded(RagiumConst.MEKANISM)) {
            for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
                val key: HTMaterialKey = essenceType.asMaterialKey()
                put(
                    MekanismMaterialPrefixes.ENRICHED.asMaterialPrefix(),
                    key,
                    REGISTER.registerSimpleItem("enriched_${key.name}"),
                )
            }
        }
    }

    @JvmStatic
    fun getMaterial(prefix: HTPrefixLike, material: HTMaterialLike): HTSimpleDeferredItem =
        MATERIALS[prefix.asMaterialPrefix(), material.asMaterialKey()]
            ?: error("Unknown $prefix item for ${material.asMaterialName()}")

    @JvmStatic
    fun getEnriched(material: HTMaterialLike): HTItemHolderLike = when (val key: HTMaterialKey = material.asMaterialKey()) {
        VanillaMaterialKeys.COAL -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_CARBON)
        VanillaMaterialKeys.REDSTONE -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_REDSTONE)
        VanillaMaterialKeys.DIAMOND -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_DIAMOND)
        VanillaMaterialKeys.GOLD -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_GOLD)
        else -> {
            when {
                ModMaterialKeys.Alloys.REFINED_OBSIDIAN.isOf(key) -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_OBSIDIAN)
                CommonMaterialKeys.Metals.TIN.isOf(key) -> HTItemHolderLike.fromHolder(MekanismItems.ENRICHED_TIN)
                else -> getMaterial(MekanismMaterialPrefixes.ENRICHED, key)
            }
        }
    }

    //    Tools    //

    @JvmStatic
    val TOOLS: ImmutableTable<HTToolVariant, HTMaterialKey, HTDeferredItem<*>> = buildTable {
        fun register(variant: HTToolVariant, material: HTEquipmentMaterial) {
            this[variant, material.asMaterialKey()] = variant.registerItem(REGISTER, material)
        }

        val materials: List<HTEquipmentMaterial> = listOf(
            RagiumEquipmentMaterials.RAGI_ALLOY,
            RagiumEquipmentMaterials.RAGI_CRYSTAL,
        )

        // Sand Paper
        if (RagiumIntegration.isLoaded(RagiumConst.CREATE)) {
            listOf(
                RagiumMaterialKeys.RAGI_CRYSTAL,
                RagiumMaterialKeys.IRIDESCENTIUM,
            ).forEach { key: HTMaterialKey ->
                this.put(
                    HTSandPaperToolVariant,
                    key,
                    REGISTER.registerItem("${key.name}_sand_paper", ::SandPaperItem),
                )
            }
        }
        // Knife
        if (RagiumIntegration.isLoaded(RagiumConst.FARMERS_DELIGHT)) {
            for (material: HTEquipmentMaterial in materials) {
                register(HTKnifeToolVariant, material)
            }
        }
        // Kitchen Knife
        if (RagiumIntegration.isLoaded(RagiumConst.KALEIDO_COOKERY)) {
            for (material: HTEquipmentMaterial in materials) {
                register(HTKitchenKnifeToolVariant, material)
            }
        }
    }

    @JvmStatic
    fun getTool(variant: HTToolVariant, material: HTMaterialLike): HTDeferredItem<*> = TOOLS[variant, material.asMaterialKey()]
        ?: error("Unknown ${variant.variantName()} item for ${material.asMaterialName()}")

    @JvmStatic
    fun getSandPaper(material: HTMaterialLike): HTDeferredItem<*> = getTool(HTSandPaperToolVariant, material)

    @JvmStatic
    fun getKnife(material: HTMaterialLike): HTItemHolderLike = when (val key: HTMaterialKey = material.asMaterialKey()) {
        VanillaMaterialKeys.IRON -> DelightItems.IRON_KNIFE.toHolderLike()
        VanillaMaterialKeys.GOLD -> DelightItems.GOLDEN_KNIFE.toHolderLike()
        VanillaMaterialKeys.DIAMOND -> DelightItems.DIAMOND_KNIFE.toHolderLike()
        VanillaMaterialKeys.NETHERITE -> DelightItems.NETHERITE_KNIFE.toHolderLike()
        else -> getTool(HTKnifeToolVariant, key)
    }

    @JvmStatic
    fun getKitchenKnife(material: HTMaterialLike): HTItemHolderLike = when (val key: HTMaterialKey = material.asMaterialKey()) {
        VanillaMaterialKeys.IRON -> HTItemHolderLike.fromHolder(KaleidoItems.IRON_KITCHEN_KNIFE)
        VanillaMaterialKeys.GOLD -> HTItemHolderLike.fromHolder(KaleidoItems.GOLD_KITCHEN_KNIFE)
        VanillaMaterialKeys.DIAMOND -> HTItemHolderLike.fromHolder(KaleidoItems.DIAMOND_KITCHEN_KNIFE)
        VanillaMaterialKeys.NETHERITE -> HTItemHolderLike.fromHolder(KaleidoItems.NETHERITE_KITCHEN_KNIFE)
        else -> getTool(HTKitchenKnifeToolVariant, key)
    }

    //    Foods    //

    //    Event    //

    /*private fun modifyCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        fun insertAfter(items: List<ItemLike>) {
            for (i: Int in items.indices) {
                val item: ItemLike = items[i]
                val nextItem: ItemLike = items.getOrNull(i + 1) ?: continue
                event.insertAfter(
                    ItemStack(item),
                    ItemStack(nextItem),
                    CreativeModeTab.TabVisibility.PARENT_TAB_ONLY,
                )
            }
        }

        fun insertAfter(vararg items: ItemLike) {
            insertAfter(items.toList())
        }

        if (!RagiumCreativeTabs.COMMON.`is`(event.tabKey)) return
        // Create
        if (RagiumIntegration.isLoaded(RagiumConst.CREATE)) {
            insertAfter(
                RagiumItems.getHammer(RagiumMaterialKeys.RAGI_CRYSTAL),
                getSandPaper(RagiumMaterialKeys.RAGI_CRYSTAL),
                getSandPaper(RagiumMaterialKeys.IRIDESCENTIUM),
            )
        }
        // Delight
        if (RagiumIntegration.isLoaded(RagiumConst.FARMERS_DELIGHT)) {
            for ((key: HTMaterialKey, knife: HTDeferredItem<*>) in TOOLS.row(HTKnifeToolVariant)) {
                insertAfter(RagiumItems.getHammer(key), knife)
            }

            insertAfter(
                RagiumItems.RAGI_CHERRY_PULP,
                RagiumDelightContents.RAGI_CHERRY_PIE,
                RagiumDelightContents.RAGI_CHERRY_PIE_SLICE,
            )

            insertAfter(
                RagiumItems.RAGI_CHERRY_TOAST,
                RagiumDelightContents.RAGI_CHERRY_TOAST_BLOCK,
            )
        }
        // Kaleido
        if (RagiumIntegration.isLoaded(RagiumConst.KALEIDO_COOKERY)) {
            for ((key: HTMaterialKey, knife: HTDeferredItem<*>) in TOOLS.row(HTKitchenKnifeToolVariant)) {
                insertAfter(RagiumItems.getHammer(key), knife)
            }
        }
        // Mekanism
        if (RagiumIntegration.isLoaded(RagiumConst.MEKANISM)) {
            for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
                val (basePrefix: HTPrefixLike, baseMaterial: HTMaterialKey) = essenceType.getBaseEntry()
                insertAfter(
                    RagiumItems.getMaterial(basePrefix, baseMaterial),
                    getEnriched(essenceType),
                )
            }
        }
    }*/

    @JvmStatic
    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        fun <T : Any> modify(item: ItemLike, type: DataComponentType<T>, value: T) {
            event.modify(item) { builder: DataComponentPatch.Builder -> builder.set(type, value) }
        }

        fun setEnch(item: ItemLike, ench: ResourceKey<Enchantment>, level: Int = 1) {
            modify(item, RagiumDataComponents.INTRINSIC_ENCHANTMENT, HTIntrinsicEnchantment(ench, level))
        }

        for (tool: ItemLike in TOOLS.columnValues(RagiumMaterialKeys.RAGI_CRYSTAL)) {
            setEnch(tool, Enchantments.MENDING)
        }
        // Create
        if (RagiumIntegration.isLoaded(RagiumConst.CREATE)) {
            event.modify(getSandPaper(RagiumMaterialKeys.RAGI_CRYSTAL)) { builder ->
                builder.set(DataComponents.MAX_DAMAGE, 8 * 8)
            }
            event.modify(getSandPaper(RagiumMaterialKeys.IRIDESCENTIUM)) { builder ->
                builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                builder.set(DataComponents.RARITY, Rarity.EPIC)
                builder.set(DataComponents.UNBREAKABLE, Unbreakable(true))
            }
        }

        // Mekanism
        if (RagiumIntegration.isLoaded(RagiumConst.MEKANISM)) {
            modify(MekanismItems.YELLOW_CAKE_URANIUM, DataComponents.FOOD, RagiumFoods.YELLOW_CAKE)
        }
    }
}
