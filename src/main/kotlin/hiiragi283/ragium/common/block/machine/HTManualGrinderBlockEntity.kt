package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.capability.LimitedItemHandler
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.dropStacks
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.extension.replaceBlockState
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.property.ifPresent
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.items.ItemStackHandler

class HTManualGrinderBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.MANUAL_GRINDER, pos, state),
    HTBlockEntityHandlerProvider {
    private val itemHandler: ItemStackHandler = object : ItemStackHandler(1) {
        override fun onContentsChanged(slot: Int) {
            super.onContentsChanged(slot)
            setChanged()
        }
    }
    private val recipeCache: HTRecipeCache<HTMachineInput, HTMachineRecipe> = HTRecipeCache(RagiumRecipes.MACHINE_TYPE)

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(ITEM_KEY, itemHandler.serializeNBT(registries))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        itemHandler.deserializeNBT(registries, tag.getCompound(ITEM_KEY))
    }

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        val step: Int = state.getOrNull(RagiumBlockProperties.LEVEL_7) ?: return InteractionResult.FAIL
        if (step == 7) {
            process(level, pos, player)
        }
        if (!level.isClientSide) {
            level.replaceBlockState(pos) { stateIn: BlockState ->
                stateIn.setValue(RagiumBlockProperties.LEVEL_7, (step + 1) % 8)
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    private fun process(level: Level, pos: BlockPos, player: Player) {
        val stackIn: ItemStack = itemHandler.getStackInSlot(0)
        recipeCache
            .getFirstMatch(
                HTMachineInput.createSimple(pos, RagiumMachineKeys.GRINDER, stackIn),
                level,
            ).onSuccess { recipe: HTMachineRecipe ->
                // Drop output
                dropStackAt(player, recipe.getResultItem(level.registryAccess()))
                // Shrink input
                stackIn.shrink(recipe.itemInputs.getOrNull(0)?.count() ?: 0)
                // Play sound if present
                RagiumMachineKeys.GRINDER
                    .getEntry()
                    .ifPresent(HTMachinePropertyKeys.SOUND) { level.playSound(null, pos, it, SoundSource.BLOCKS) }
            }.onFailure { _: Throwable ->
                // Drop input
                dropStackAt(player, stackIn)
                itemHandler.setStackInSlot(0, ItemStack.EMPTY)
            }
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        itemHandler.dropStacks(level, pos)
    }

    //    HTBlockEntityHandlerProvider    //

    override fun getItemHandler(direction: Direction?): LimitedItemHandler =
        LimitedItemHandler(mapOf(0 to HTStorageIO.INPUT), ::itemHandler)
}
