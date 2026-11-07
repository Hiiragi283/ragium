package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.base.HTItemCombineRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTMemoryDiscData
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTItemCombineBlockEntity
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.TypedInstance
import net.minecraft.core.component.DataComponents
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.block.state.BlockState

class HTScannerBlockEntity(pos: BlockPos, state: BlockState) : HTItemCombineBlockEntity(RagiumBlockEntityTypes.SCANNER.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTItemCombineRecipe> = FakeRecipe.LOOKUP
    override fun onComplete() {
        playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.scanner

    //    FakeRecipe    //

    data object FakeRecipe : HTItemCombineRecipe {
        @JvmField
        val LOOKUP: HTRecipeLookup<HTItemCombineRecipe> = HTRecipeLookup { sequenceOf(HTRecipeHolder(RagiumAPI.id("scanning/fake"), FakeRecipe)) }

        override val firstIngredient: HTIngredient<Item, ItemInstance> = object : HTIngredient<Item, ItemInstance> {
            override fun test(instance: ItemInstance): Boolean = testOnlyType(instance)

            override fun testOnlyType(instance: TypedInstance<Item>): Boolean = !RagiumItems.MEMORY_DISC.isOf(instance)

            override fun getMatchingStack(instance: ItemInstance): ItemInstance = ItemStack.EMPTY
        }
        override val secondIngredient: HTIngredient<Item, ItemInstance> = object : HTIngredient<Item, ItemInstance> {
            override fun test(instance: ItemInstance): Boolean = !instance.has(RagiumDataComponents.MEMORY_DISC_DATA)

            override fun testOnlyType(instance: TypedInstance<Item>): Boolean = RagiumItems.MEMORY_DISC.isOf(instance)

            override fun getMatchingStack(instance: ItemInstance): ItemInstance = when (test(instance)) {
                true -> HTIngredientHelper.copyWithCount(instance, 1)
                false -> ItemStack.EMPTY
            }
        }

        override fun getProgressData(first: ItemInstance, second: ItemInstance): HTProgressData {
            val modifier: Int = first.getOrDefault(DataComponents.RARITY, Rarity.COMMON).ordinal + 1
            return HTProgressData.time(20 * 30 * modifier)
        }

        override fun apply(first: ItemInstance, second: ItemInstance): ItemStack {
            val template: ItemStack = HTIngredientHelper.unwrap(first)
            val result: ItemStack = HTIngredientHelper.unwrap(second).copyWithCount(1)
            result[RagiumDataComponents.MEMORY_DISC_DATA] = HTMemoryDiscData.from(template)
            return result
        }
    }
}
