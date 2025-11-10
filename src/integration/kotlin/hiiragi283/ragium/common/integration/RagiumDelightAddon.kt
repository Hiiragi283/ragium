package hiiragi283.ragium.common.integration

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTBasicDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockRegister
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.variant.HTKnifeToolVariant
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumEquipmentMaterials
import hiiragi283.ragium.setup.RagiumFoods
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import vectorwing.farmersdelight.common.block.FeastBlock
import vectorwing.farmersdelight.common.block.PieBlock
import vectorwing.farmersdelight.common.registry.ModItems
import kotlin.collections.iterator

object RagiumDelightAddon : RagiumAddon {
    //    Block    //

    @JvmField
    val BLOCK_REGISTER = HTDeferredBlockRegister(RagiumAPI.MOD_ID)

    @JvmField
    val RAGI_CHERRY_PIE: HTBasicDeferredBlock<PieBlock> = BLOCK_REGISTER.registerSimple(
        "ragi_cherry_pie",
        BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE),
        { prop: BlockBehaviour.Properties -> PieBlock(prop, RAGI_CHERRY_PIE_SLICE) },
    )

    @JvmField
    val RAGI_CHERRY_TOAST_BLOCK: HTBasicDeferredBlock<FeastBlock> = BLOCK_REGISTER.registerSimple(
        "ragi_cherry_toast_block",
        BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE),
        { prop: BlockBehaviour.Properties -> FeastBlock(prop, RagiumItems.RAGI_CHERRY_TOAST, true) },
    )

    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    // Knives
    @JvmField
    val KNIFE_MAP: Map<HTMaterialKey, HTDeferredItem<*>> = listOf(
        RagiumEquipmentMaterials.RAGI_ALLOY,
        RagiumEquipmentMaterials.RAGI_CRYSTAL,
    ).associateBy(HTEquipmentMaterial::asMaterialKey)
        .mapValues { (key: HTMaterialKey, material: HTEquipmentMaterial) ->
            HTKnifeToolVariant.registerItem(ITEM_REGISTER, material)
        }

    @JvmStatic
    fun getKnife(material: HTMaterialLike): HTItemHolderLike = when (val key: HTMaterialKey = material.asMaterialKey()) {
        VanillaMaterialKeys.IRON -> ModItems.IRON_KNIFE.toHolderLike()
        VanillaMaterialKeys.GOLD -> ModItems.GOLDEN_KNIFE.toHolderLike()
        VanillaMaterialKeys.DIAMOND -> ModItems.DIAMOND_KNIFE.toHolderLike()
        VanillaMaterialKeys.NETHERITE -> ModItems.NETHERITE_KNIFE.toHolderLike()
        else -> KNIFE_MAP[key] ?: error("Unknown knife item for ${key.name}")
    }

    // Food
    @JvmField
    val RAGI_CHERRY_PIE_SLICE: HTSimpleDeferredItem =
        ITEM_REGISTER.registerSimpleItem("ragi_cherry_pie_slice") { it.food(RagiumFoods.RAGI_CHERRY_PIE_SLICE) }

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        BLOCK_REGISTER.register(eventBus)
        ITEM_REGISTER.register(eventBus)
    }

    override fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        event.modify(getKnife(RagiumMaterialKeys.RAGI_CRYSTAL)) { builder: DataComponentPatch.Builder ->
            builder.set(
                RagiumDataComponents.INTRINSIC_ENCHANTMENT,
                HTIntrinsicEnchantment(Enchantments.MENDING, 1),
            )
        }
    }

    override fun buildCreativeTabs(helper: RagiumAddon.CreativeTabHelper) {
        helper.ifMatchTab(RagiumCreativeTabs.ITEMS) { event: BuildCreativeModeTabContentsEvent ->
            for ((key: HTMaterialKey, knife: HTDeferredItem<*>) in KNIFE_MAP) {
                helper.insertAfter(event, RagiumItems.getHammer(key), knife)
            }

            helper.insertAfter(
                event,
                RagiumItems.RAGI_CHERRY_PULP,
                RAGI_CHERRY_PIE,
                RAGI_CHERRY_PIE_SLICE,
            )

            helper.insertAfter(
                event,
                RagiumItems.RAGI_CHERRY_TOAST,
                RAGI_CHERRY_TOAST_BLOCK,
            )
        }
    }
}
