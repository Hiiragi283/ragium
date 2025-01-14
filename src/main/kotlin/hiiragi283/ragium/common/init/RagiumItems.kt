package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.world.item.Item
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumItems {
    //    Materials    //

    enum class Dusts(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 1
        CRUDE_RAGINITE(RagiumMaterialKeys.CRUDE_RAGINITE),
        ALKALI(RagiumMaterialKeys.ALKALI),
        ASH(RagiumMaterialKeys.ASH),
        COPPER(RagiumMaterialKeys.COPPER),
        IRON(RagiumMaterialKeys.IRON),
        LAPIS(RagiumMaterialKeys.LAPIS),
        LEAD(RagiumMaterialKeys.LEAD),
        NITER(RagiumMaterialKeys.NITER),
        QUARTZ(RagiumMaterialKeys.QUARTZ),
        SALT(RagiumMaterialKeys.SALT),
        SULFUR(RagiumMaterialKeys.SULFUR),

        // tier 2
        RAGINITE(RagiumMaterialKeys.RAGINITE),
        GOLD(RagiumMaterialKeys.GOLD),
        SILVER(RagiumMaterialKeys.SILVER),

        // tier 3
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        BAUXITE(RagiumMaterialKeys.BAUXITE),
        DIAMOND(RagiumMaterialKeys.DIAMOND),
        EMERALD(RagiumMaterialKeys.EMERALD),
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder("${name.lowercase()}_dust")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.DUST
    }

    enum class Gears(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 1
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        IRON(RagiumMaterialKeys.IRON),

        // tier 2
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        GOLD(RagiumMaterialKeys.GOLD),
        STEEL(RagiumMaterialKeys.STEEL),

        // tier 3
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),
        DIAMOND(RagiumMaterialKeys.DIAMOND),
        EMERALD(RagiumMaterialKeys.EMERALD),
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder("${name.lowercase()}_gear")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.GEAR
    }

    enum class Gems(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 3
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        CINNABAR(RagiumMaterialKeys.CINNABAR),
        CRYOLITE(RagiumMaterialKeys.CRYOLITE),
        FLUORITE(RagiumMaterialKeys.FLUORITE),
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder(name.lowercase())
        override val tagPrefix: HTTagPrefix = HTTagPrefix.GEM
    }

    enum class Ingots(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 1
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),

        // tier 2
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        STEEL(RagiumMaterialKeys.STEEL),

        // tier 3
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),

        // tier 4
        RAGIUM(RagiumMaterialKeys.RAGIUM),
        ECHORIUM(RagiumMaterialKeys.ECHORIUM),
        FIERIUM(RagiumMaterialKeys.FIERIUM),
        DRAGONIUM(RagiumMaterialKeys.DRAGONIUM),
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder("${name.lowercase()}_ingot")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.INGOT
    }

    enum class RawMaterials(override val material: HTMaterialKey) : HTItemContent.Material {
        // tier 1
        CRUDE_RAGINITE(RagiumMaterialKeys.CRUDE_RAGINITE),
        NITER(RagiumMaterialKeys.NITER),
        REDSTONE(RagiumMaterialKeys.REDSTONE),
        SALT(RagiumMaterialKeys.SALT),
        SULFUR(RagiumMaterialKeys.SULFUR),

        // tier2
        RAGINITE(RagiumMaterialKeys.RAGINITE),
        GALENA(RagiumMaterialKeys.GALENA),
        PYRITE(RagiumMaterialKeys.PYRITE),
        SPHALERITE(RagiumMaterialKeys.SPHALERITE),

        // tier 3
        BAUXITE(RagiumMaterialKeys.BAUXITE),
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder("raw_${name.lowercase()}")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.RAW_MATERIAL
    }

    @JvmField
    val MATERIALS: List<HTItemContent.Material> = buildList {
        addAll(Dusts.entries)
        addAll(Gears.entries)
        addAll(Gems.entries)
        addAll(Ingots.entries)
        addAll(RawMaterials.entries)
    }

    //    Parts    //

    enum class Circuits(override val machineTier: HTMachineTier) : HTItemContent.Tier {
        SIMPLE(HTMachineTier.SIMPLE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ;

        override val holder: DeferredHolder<Item, out Item> = HTContent.itemHolder("${name.lowercase()}_circuit")
    }

    //    Register    //

    @JvmField
    val REGISTER: DeferredRegister.Items = DeferredRegister.createItems(RagiumAPI.MOD_ID)

    @JvmStatic
    internal fun register(bus: IEventBus) {
        fun DeferredRegister.Items.registerSimple(content: HTItemContent) {
            content.registerSimpleItem(this)
        }

        // materials
        MATERIALS.forEach(REGISTER::registerSimple)
        // parts
        Circuits.entries.forEach(REGISTER::registerSimple)

        REGISTER.register(bus)
    }
}
