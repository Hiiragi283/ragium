package hiiragi283.ragium.common.unused

import hiiragi283.ragium.api.extension.isAir
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.common.init.RagiumItems
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.fabricmc.fabric.api.util.Item2ObjectMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey

object HTEnergeticFuelRegistry : Item2ObjectMap<Int> {
    private val itemMap: MutableMap<Item, Int> = mutableMapOf()
    private val tagMap: MutableMap<TagKey<Item>, Int> = mutableMapOf()

    @JvmStatic
    val ENTRY_MAP: Map<HTItemIngredient, Int>
        get() = buildMap {
            itemMap.forEach { (item: Item, damage: Int) -> put(HTItemIngredient.Companion.of(item), damage) }
            tagMap.forEach { (tagKey: TagKey<Item>, damage: Int) -> put(HTItemIngredient.Companion.of(tagKey), damage) }
        }

    @JvmStatic
    fun changeComponent(stack: ItemStack): Boolean {
        if (stack.count > 1) return false
        val maxDamage: Int = get(stack.item)
        if (maxDamage <= 0) return false
        stack.set(DataComponentTypes.MAX_STACK_SIZE, 1)
        stack.set(DataComponentTypes.MAX_DAMAGE, maxDamage)
        stack.damage += 1
        if (stack.damage >= stack.maxDamage) {
            stack.decrement(1)
        }
        return true
    }

    //    Item2ObjectMap    //

    @Suppress("DEPRECATION")
    override fun get(item: ItemConvertible): Int {
        val item1: Item = item.asItem()
        if (item1.isAir) return 0
        tagMap.forEach { (tag: TagKey<Item>, damage: Int) ->
            if (item1.registryEntry.isIn(tag)) {
                return damage
            }
        }
        return itemMap.getOrDefault(item, 0)
    }

    override fun add(item: ItemConvertible, value: Int) {
        check(itemMap.put(item.asItem(), value) == null) { "Duplicated fuel: ${Registries.ITEM.getId(item.asItem())}" }
    }

    override fun add(tag: TagKey<Item>, value: Int) {
        check(tagMap.put(tag, value) == null) { "Duplicated fuel: ${tag.id}" }
    }

    override fun remove(item: ItemConvertible): Unit = throw UnsupportedOperationException()

    override fun remove(tag: TagKey<Item>): Unit = throw UnsupportedOperationException()

    override fun clear(item: ItemConvertible): Unit = throw UnsupportedOperationException()

    override fun clear(tag: TagKey<Item>): Unit = throw UnsupportedOperationException()

    init {
        // gems
        add(ConventionalItemTags.DIAMOND_GEMS, 64)
        add(ConventionalItemTags.EMERALD_GEMS, 64)
        add(ConventionalItemTags.LAPIS_GEMS, 24)
        add(ConventionalItemTags.QUARTZ_GEMS, 8)
        add(ConventionalItemTags.AMETHYST_GEMS, 16)

        add(ConventionalItemTags.ENDER_PEARLS, 16)
        add(Items.ENDER_EYE, 32)
        // nether
        add(Items.MAGMA_BLOCK, 1)
        add(Items.NETHER_WART_BLOCK, 1)
        add(Items.WARPED_WART_BLOCK, 1)
        // coral
        add(Items.BRAIN_CORAL, 3)
        add(Items.BRAIN_CORAL_BLOCK, 3)
        add(Items.BRAIN_CORAL_FAN, 3)
        add(Items.BUBBLE_CORAL, 3)
        add(Items.BUBBLE_CORAL_BLOCK, 3)
        add(Items.BUBBLE_CORAL_FAN, 3)
        add(Items.FIRE_CORAL, 3)
        add(Items.FIRE_CORAL_BLOCK, 3)
        add(Items.FIRE_CORAL_FAN, 3)
        add(Items.HORN_CORAL, 3)
        add(Items.HORN_CORAL_BLOCK, 3)
        add(Items.HORN_CORAL_FAN, 3)
        add(Items.TUBE_CORAL, 3)
        add(Items.TUBE_CORAL_BLOCK, 3)
        add(Items.TUBE_CORAL_FAN, 3)
        // misc
        add(ConventionalItemTags.REDSTONE_DUSTS, 1)
        add(Items.REDSTONE_BLOCK, 9)

        add(ConventionalItemTags.GLOWSTONE_DUSTS, 4)
        add(Items.GLOWSTONE, 16)

        add(Items.DRAGON_BREATH, 64)
        add(Items.END_CRYSTAL, 128)
        add(Items.GHAST_TEAR, 64)
        add(Items.HEART_OF_THE_SEA, 512)
        add(Items.NETHER_STAR, 1024)
        add(Items.PHANTOM_MEMBRANE, 32)
        add(Items.SHULKER_SHELL, 128)
        add(Items.ECHO_SHARD, 128)

        add(ItemTags.SKULLS, 16)

        add(Items.GOLDEN_CARROT, 8)
        add(Items.GLISTERING_MELON_SLICE, 8)
        add(Items.GOLDEN_APPLE, 64)
        add(Items.ENCHANTED_GOLDEN_APPLE, 1024)

        add(RagiumItems.LUMINESCENCE_DUST, 4)
        add(RagiumItems.OBSIDIAN_TEAR, 64)
    }
}
