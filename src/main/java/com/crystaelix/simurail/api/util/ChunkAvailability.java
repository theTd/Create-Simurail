package com.crystaelix.simurail.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkSource;

/**
 * Non-blocking chunk loadedness probes backed by {@link ChunkSource#hasChunk}.
 * Unlike {@link Level#getBlockState}/{@link Level#getBlockEntity}, these never schedule
 * or wait for chunk loads, so they are safe on physics/entity tick paths where a missing
 * chunk must be skipped instead of synchronously loaded (which can stall the server thread
 * forever on a broken region file).
 */
public class ChunkAvailability {

	public static boolean isChunkLoaded(Level level, BlockPos pos) {
		return level.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
	}

	public static boolean areChunksLoaded(Level level, int minX, int minZ, int maxX, int maxZ) {
		ChunkSource chunkSource = level.getChunkSource();
		for(int cx = SectionPos.blockToSectionCoord(minX); cx <= SectionPos.blockToSectionCoord(maxX); ++cx) {
			for(int cz = SectionPos.blockToSectionCoord(minZ); cz <= SectionPos.blockToSectionCoord(maxZ); ++cz) {
				if(!chunkSource.hasChunk(cx, cz)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean areChunksLoaded(Level level, BlockPos from, BlockPos to) {
		return areChunksLoaded(level, Math.min(from.getX(), to.getX()), Math.min(from.getZ(), to.getZ()), Math.max(from.getX(), to.getX()), Math.max(from.getZ(), to.getZ()));
	}

}
