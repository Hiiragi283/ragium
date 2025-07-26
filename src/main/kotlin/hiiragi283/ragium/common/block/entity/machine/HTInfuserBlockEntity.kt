package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.HTInfusingRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.inventory.HTSingleProcessMenu
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.EnchantingTableBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

class HTInfuserBlockEntity(pos: BlockPos, state: BlockState) : HTMachineBlockEntity(RagiumBlockEntityTypes.INFUSER, pos, state) {
    override val inventory: HTItemHandler = HTItemStackHandler(2, this::setChanged)
    override val energyUsage: Int = 0

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        super.writeNbt(writer)
        writer.write(ExtraCodecs.POSITIVE_FLOAT, "required_cost", requiredCost)
        writer.write(ExtraCodecs.POSITIVE_FLOAT, "current_cost", currentCost)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        super.readNbt(reader)
        reader
            .read(ExtraCodecs.POSITIVE_FLOAT, "required_cost")
            .ifSuccess { requiredCost = it }
        reader
            .read(ExtraCodecs.POSITIVE_FLOAT, "current_cost")
            .ifSuccess { currentCost = it }
    }

    //    Ticking    //

    private val recipeCache: HTRecipeCache<SingleRecipeInput, HTInfusingRecipe> =
        HTRecipeCache.simple(RagiumRecipeTypes.INFUSING.get())
    private var requiredCost: Float = 0f
    private var currentCost: Float = 0f

    override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // インプットに一致するレシピを探索する
        val input = SingleRecipeInput(inventory.getStackInSlot(0))
        val recipe: HTInfusingRecipe = recipeCache.getFirstRecipe(input, level) ?: return resetCost(0f)
        // コストが異なる場合，再指定する
        val recipeCost: Float = recipe.cost
        if (requiredCost != recipeCost) {
            resetCost(recipeCost)
        }
        // 周囲のブロックからエンチャントパワーを得る
        val aroundCost: Float = EnchantingTableBlock.BOOKSHELF_OFFSETS
            .map { posIn: BlockPos ->
                if (EnchantingTableBlock.isValidBookShelf(level, pos, posIn)) {
                    val posTo: BlockPos = pos.offset(posIn)
                    level.getBlockState(posTo).getEnchantPowerBonus(level, posTo)
                } else {
                    0f
                }
            }.sum()
        // コストを加算する
        currentCost += aroundCost
        if (aroundCost > 0f) {
            level.playSound(null, pos, SoundEvents.CHISELED_BOOKSHELF_INSERT_ENCHANTED, SoundSource.BLOCKS)
        }
        if (currentCost < requiredCost) return TriState.DEFAULT
        // アウトプットに搬出できるか判定する
        val output: ItemStack = recipe.assemble(input, level.registryAccess())
        if (!ItemHandlerHelper.insertItem(inventory, output, true).isEmpty) return TriState.FALSE
        // 実際にアウトプットに搬出する
        ItemHandlerHelper.insertItem(inventory, output, false)
        // インプットを減らす
        inventory.consumeStackInSlot(0, 1)
        // コストをリセットする
        currentCost = 0f
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS)
        return TriState.TRUE
    }

    private fun resetCost(newCost: Float): TriState {
        requiredCost = newCost
        currentCost = 0f
        return TriState.FALSE
    }

    override fun getItemHandler(direction: Direction?): HTFilteredItemHandler = HTFilteredItemHandler(
        inventory,
        object : HTItemFilter {
            override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = slot == 0

            override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = slot != 0
        },
    )

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTSingleProcessMenu = HTSingleProcessMenu(
        RagiumMenuTypes.INFUSER,
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
