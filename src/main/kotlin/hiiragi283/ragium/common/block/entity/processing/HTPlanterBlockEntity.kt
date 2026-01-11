package hiiragi283.ragium.common.block.entity.processing

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.math.div
import hiiragi283.core.api.math.times
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.HTFinderRecipeCache
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.api.upgrade.HTUpgradeKeys
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.config.RagiumFluidConfigType
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.lighting.LightEngine

class HTPlanterBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity(RagiumBlockEntityTypes.PLANTER, pos, state) {
    @DescSynced
    @Persisted(subPersisted = true)
    private val inputTank: HTBasicFluidTank =
        HTVariableFluidTank.input(getTankCapacity(RagiumFluidConfigType.FIRST_INPUT))

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder) {
        builder.addSlot(HTSlotInfo.EXTRA_INPUT, inputTank)
    }

    @DescSynced
    @Persisted(subPersisted = true)
    private val plantSlot: HTBasicItemSlot = HTBasicItemSlot.input(limit = 1)

    @DescSynced
    @Persisted(subPersisted = true)
    private val soilSlot: HTBasicItemSlot = HTBasicItemSlot.input(limit = 1)

    @DescSynced
    @Persisted(subPersisted = true)
    private val cropSlot: HTBasicItemSlot = HTBasicItemSlot.output()

    @DescSynced
    @Persisted(subPersisted = true)
    private val seedSlot: HTBasicItemSlot = HTBasicItemSlot.output()

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder) {
        builder.addSlot(HTSlotInfo.INPUT, plantSlot)
        builder.addSlot(HTSlotInfo.NONE, soilSlot)
        builder.addSlot(HTSlotInfo.OUTPUT, cropSlot)
        builder.addSlot(HTSlotInfo.EXTRA_OUTPUT, seedSlot)
    }

    private val plantInputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(plantSlot) }
    private val soilInputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(soilSlot) }
    private val fluidInputHandler: HTSlotInputHandler<HTFluidResourceType> by lazy { HTSlotInputHandler(inputTank) }

    private val cropOutputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(cropSlot) }
    private val seedOutputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(seedSlot) }

    override fun setupElements(root: UIElement) {}

    //    Processing    //

    override fun createRecipeComponent(): HTRecipeComponent<HTPlantingRecipe.Input, HTPlantingRecipe> =
        object : HTRecipeComponent<HTPlantingRecipe.Input, HTPlantingRecipe>(this) {
            private val cache: HTRecipeCache<HTPlantingRecipe.Input, HTPlantingRecipe> = HTFinderRecipeCache(RagiumRecipeTypes.PLANTING)

            override fun insertOutput(
                level: ServerLevel,
                pos: BlockPos,
                input: HTPlantingRecipe.Input,
                recipe: HTPlantingRecipe,
            ) {
                val access: RegistryAccess = level.registryAccess()
                cropOutputHandler.insert(recipe.getResultItem(access))
                seedOutputHandler.insert(recipe.getResultSeed(access))
            }

            override fun extractInput(
                level: ServerLevel,
                pos: BlockPos,
                input: HTPlantingRecipe.Input,
                recipe: HTPlantingRecipe,
            ) {
                plantInputHandler.consume(recipe.seedIngredient.getRequiredAmount())
                fluidInputHandler.consume(HTPlantingRecipe.FLUID_AMOUNT)
            }

            override fun applyEffect() {
            }

            override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTPlantingRecipe.Input =
                HTPlantingRecipe.Input(plantInputHandler.getItemStack(), soilInputHandler.getItemStack(), fluidInputHandler.getFluidStack())

            override fun getMatchedRecipe(input: HTPlantingRecipe.Input, level: ServerLevel): HTPlantingRecipe? =
                cache.getFirstRecipe(input, level)

            override fun getMaxProgress(recipe: HTPlantingRecipe): Int =
                modifyValue(HTUpgradeKeys.SPEED) { recipe.time * LightEngine.MAX_LEVEL / (it * getBaseMultiplier()) }

            override fun getProgress(level: ServerLevel, pos: BlockPos): Int {
                val blockLight: Int = level.getBrightness(LightLayer.BLOCK, pos.above())
                val skyLight: Int = level.getBrightness(LightLayer.SKY, pos.above())
                return when {
                    level.canSeeSky(pos.above()) -> skyLight
                    else -> minOf(blockLight, skyLight)
                }
            }

            override fun canProgressRecipe(level: ServerLevel, input: HTPlantingRecipe.Input, recipe: HTPlantingRecipe): Boolean =
                cropOutputHandler.canInsert(recipe.getResultItem(level.registryAccess()))
        }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.device.planter
}
