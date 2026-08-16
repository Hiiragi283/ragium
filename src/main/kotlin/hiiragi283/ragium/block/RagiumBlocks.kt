package hiiragi283.ragium.block

import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.registry.HTDeferredBlockAndItemRegister
import hiiragi283.lib.registry.HTDeferredBlockRegister
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTBlockPart
import hiiragi283.ragium.api.tag.HTMaterial
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus

data object RagiumBlocks {
    @JvmStatic
    private val BLOCK_ONLY = HTDeferredBlockRegister(RagiumAPI.MOD_ID)

    @JvmField
    val REGISTER = HTDeferredBlockAndItemRegister(BLOCK_ONLY)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    @JvmField
    val TABLE_COMPARATOR: Comparator<Pair<HTBlockPart, HTMaterial>> = compareBy<Pair<HTBlockPart, HTMaterial>> { it.first }.thenBy { it.second.materialName }

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun properties(hardness: Float, resistance: Float = hardness): BlockBehaviour.Properties = BlockBehaviour.Properties.of().strength(hardness, resistance)

    //    Ingredient    //

    @JvmField
    val MATERIAL_BLOCKS: Table<HTBlockPart, HTMaterial, HTSimpleDeferredBlockAndItem> = buildTable(sortedMapOf(TABLE_COMPARATOR)) {
        fun register(part: HTBlockPart, material: HTMaterial, blockProp: BlockBehaviour.Properties, itemProp: Identity<Item.Properties> = identity()) {
            this[part, material] = REGISTER.registerSimple(part.createName(material), blockProp, itemProp)
        }

        register(HTBlockPart.STORAGE_BLOCK, HTMaterial.Fuel.CHARCOAL, copyOf(Blocks.COAL_BLOCK).sound(SoundType.TUFF))
        register(HTBlockPart.STORAGE_BLOCK, HTMaterial.Fuel.COAL_COKE, copyOf(Blocks.COAL_BLOCK).mapColor(MapColor.COLOR_GRAY))
    }

    @JvmStatic
    fun getOrThrow(part: HTBlockPart, material: HTMaterial): HTSimpleDeferredBlockAndItem = MATERIAL_BLOCKS[part, material] ?: error("Unregistered blocl: ${part.createName(material)}")
}
